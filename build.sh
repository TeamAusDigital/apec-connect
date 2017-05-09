#!/bin/bash

set -e

PROJECT_NAME="APEC Connect"
TECHNICAL_PROJECT_NAME="apec-connect"

echo "Building ${PROJECT_NAME}"

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# making sure jq - commandline JSON processor is installed
command -v jq >/dev/null 2>&1 || { echo >&2 "Please install jq. Aborting."; exit 1; }

CONFY=${SCRIPT_DIR}/tools/confy-assembly-2.5.jar

if [ ! -f $CONFY ]
then
  echo '$CONFY does not exist.'
  exit 1
fi

# # making sure sbtvolume docker container is running - START
# SBT_CID=$(docker ps -a | grep sbtvolume | head -1)
# if [ "$SBT_CID" = "" ]
# then
#   docker create -v /root/.sbt --name sbtvolume tianon/true \bin\true
# fi
# # making sure sbtvolume docker container is running - END

# making sure npm-lazy docker container is running - START
NPM_LAZY_RUNNING_CID=$(docker ps | grep npm-lazy | head -1 | awk '{print $1}')
NPM_LAZY_CID=$(docker ps -a | grep npm-lazy | head -1 | awk '{print $1}')

UNAME=`uname`

# set host ip Linux
if [ $UNAME != 'Darwin' ]
then
  echo "Platform is Linux"
  DEVICE=$(nmcli dev | grep ethernet | head -1 | awk '{print $1}')

  if [ "${DEVICE}" = "" ]
  then
     echo "Unable to locate an ethernet device to configure address. Have you connected a network device?"
     exit 1
  fi

  CONNECTION=$(nmcli -t -f device,uuid,name con | grep ${DEVICE} | head -1 | awk -F : '{print $2}')

  if [ "${CONNECTION}" != "" ]
  then
     echo ""
     echo -n "IP: "
     HOST_IP=$(ip addr list $DEVICE | grep "inet" | sed -e 's/  */ /g' | grep brd | cut -d' ' -f3 | cut -f1 -d"/")
  else
     echo "No active connection. Set the address first."
     exit 1
  fi
fi

# set host ip for Mac
if [ $UNAME = 'Darwin' ]
 then
 echo "Platform is Mac"
 HOST_IP=$(echo $DOCKER_HOST | sed -e "s/tcp:\/\///g" | cut -f1 -d":")
fi

if [ "$HOST_IP" = "" ]
then
  echo "Cannot resolve host ip"
  exit 1
fi

# starting up npm-lazy container
if [ "$NPM_LAZY_CID" = "" ]
then
  echo "running npm-lazy container"
  docker run -d -p 8080:8080 --name npm-lazy langrisha/npm-lazy --show-config --port=8080 --external-url=http://${HOST_IP}:8080
elif [ "$NPM_LAZY_RUNNING_CID" = "" ]
then
  echo "npm-lazy exists but isnt running. Restarting"
  docker start npm-lazy
fi

#polling to check if the container is running
NUM=0
while [ $NUM -lt 30 ]
do
  let NUM+=1
  NPM_LAZY_CID=$(docker ps | grep npm-lazy | head -1 | awk '{print $1}')
  if [ "$NPM_LAZY_CID" != "" ]; then
    break;
  fi
  sleep 2
  echo "Waiting for container npm-lazy to be ready"
done

NPM_LAZY_CID=$(docker ps | grep npm-lazy | head -1 | awk '{print $1}')
if [ "$NPM_LAZY_CID" = "" ]; then
  echo "Container npm-lazy did not start".
  echo ""
  exit -1
fi

# making sure npm-lazy docker container is running - END

DOCKER_ARGS="-v ${HOME}/.dockercfg:/root/.dockercfg -v ${HOME}/.docker/config.json:/root/.docker/config.json"
DOCKER_ARGS="$DOCKER_ARGS -v ${HOME}/.cache/bower:/root/.cache/bower -v ${HOME}/.buildy/${TECHNICAL_PROJECT_NAME}/.sbt"
DOCKER_ARGS="$DOCKER_ARGS -e npm_config_registry=http://${HOST_IP}:8080"
DOCKER_ARGS="$DOCKER_ARGS -e npm_config_cache-min=9999999999"

SOURCE_DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

cd ${SOURCE_DIR}

ZIP_FILE=${SOURCE_DIR}/../${TECHNICAL_PROJECT_NAME}.zip

echo "Updating [${ZIP_FILE}]..."

zip -r -0 --exclude=*.DS_Store* --exclude=*.git* --exclude=*node_modules/phantom* --filesync ${ZIP_FILE} .

echo "Updated [${ZIP_FILE}]."

java -cp $CONFY buildy.app.BuildyApp -i ${SOURCE_DIR}/environment.conf -s ${ZIP_FILE} -o /tmp/${TECHNICAL_PROJECT_NAME}.yml --dockerArgs "${DOCKER_ARGS}" --dockerBin `which docker` --outputDir ~/.buildy/tmp/${TECHNICAL_PROJECT_NAME}-output/ "$@"

VOLUME_IDS=`java -cp $CONFY confy.app.ConfigurationApp -i ${SOURCE_DIR}/environment.conf -p resources | jq -r '.[] | select(.resource_type=="disk") | .id'`
for VOLUME_ID in $VOLUME_IDS
do
  ID=`echo $VOLUME_ID | tr -cd '[[:alnum:]]'`
  DOCKER_COMMAND="docker create"
  PATHS=`java -cp $CONFY confy.app.ConfigurationApp -i ${SOURCE_DIR}/environment.conf -p resources | jq -r ".[] | select(.id==\"$VOLUME_ID\") | .settings.paths[]"`
  for VOLUME_PATH in $PATHS
  do
    echo $VOLUME_PATH
    DOCKER_COMMAND+=" -v $VOLUME_PATH"
  done

  DOCKER_COMMAND+=" --name $ID tianon/true \bin\true"

  EXISTS=`docker ps -a | grep "${ID}" | wc -l`
  if [[ $EXISTS -gt 0 ]]
  then
    echo "Volume [${ID}] already exists, not creating it."
  else
    $DOCKER_COMMAND
  fi

done

echo "Built. Start with './run.sh /tmp/${TECHNICAL_PROJECT_NAME}.yml'"
