 #!/bin/bash -x
INPUT_YML=$1

if [ -z ${INPUT_YML} ]
then
  echo "Must be supplied an input .yml file"
  exit -1
fi

if [ ! -f ${INPUT_YML} ]
then
  echo "Input .yml file [${INPUT_YML}] does not exist!"
  exit -1
fi

PROJECT_NAME="D61+ Expert Connect"
TECHNICAL_PROJECT_NAME="d61plus"
DNS_DOCKER_NAME="${TECHNICAL_PROJECT_NAME}_dnsdock"
SUBSTITUTED_YAML_FILE="/tmp/${TECHNICAL_PROJECT_NAME}_substituted.yml"

docker kill ${DNS_DOCKER_NAME}
docker rm ${DNS_DOCKER_NAME}

docker run -d -v /var/run/docker.sock:/var/run/docker.sock --name ${DNS_DOCKER_NAME} -p 53:53/udp tonistiigi/dnsdock

DNS_IP=`docker inspect --format='{{.NetworkSettings.IPAddress}}' ${DNS_DOCKER_NAME}`

if [ -z ${DNS_IP} ]
then
  echo "No ip address for dns container."
  exit -1
fi

sed -e "s,%DNS_IP%,${DNS_IP},g" $INPUT_YML > "${SUBSTITUTED_YAML_FILE}"

docker-compose -f "${SUBSTITUTED_YAML_FILE}" up
