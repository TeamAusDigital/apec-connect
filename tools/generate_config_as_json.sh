#!/bin/bash
# Processes environment.conf from the root of the project and produces a
# fully substituted JSON verion.

OUTPUT_FILE=$1
OUTPUT_DIR=$(dirname ${OUTPUT_FILE})

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [ ! -d $OUTPUT_DIR ]
then
  mkdir -p $OUTPUT_DIR
fi

CONFY=${SCRIPT_DIR}/confy-assembly-2.5.jar

java -cp $CONFY confy.app.ConfigurationApp -i ${SCRIPT_DIR}/../environment.conf > $OUTPUT_FILE
