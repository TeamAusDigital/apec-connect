#!/bin/bash

set -e

CONTAINER=$1
PORT=$2
PORT_TYPE=$3

if [ -z ${PORT_TYPE} ]
then
  PORT_TYPE="tcp"
fi

FILTER='.[0] | .NetworkSettings.Ports."'$2'/'$PORT_TYPE'" | .[0] | .HostPort'

docker inspect ${1} | jq -r "${FILTER}"
