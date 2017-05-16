#!/bin/bash

set -e
set -x

POSTGRES_PORT_5432=`../tools/docker-port.sh tmp_apecconnectdb_1 5432`
KAMON_PORT_8125=`../tools/docker-port.sh tmp_apecconnectstatsd_1 8125 udp`
MAILDEV_PORT_25=`../tools/docker-port.sh tmp_apecconnectmailrelay_1 25` 

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

sbt \
 -Dkamon.auto.start=true \
 -Dplay.http.context=/ \
 -Dkamon.statsd.hostname=$HOST_IP \
 -Dkamon.statsd.port=$KAMON_PORT_8125 \
 -Dslick.dbs.default.db.hostname=$HOST_IP \
 -Dslick.dbs.default.db.port=$POSTGRES_PORT_5432 \
 -Dslick.dbs.default.db.name=postgres \
 -Dplay.mailer.host=$HOST_IP \
 -Dplay.mailer.port=$MAILDEV_PORT_25 \
 -Dsbt.task.forcegc=false $@
