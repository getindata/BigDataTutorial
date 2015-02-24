#!/bin/bash

SPARK_DIR=/root/spark

${SPARK_DIR}/bin/spark-submit \
        --master yarn-client \
        --class "$1" \
        target/scala-2.10/Sparking-assembly-0.1.jar \
        "${@:2}"
