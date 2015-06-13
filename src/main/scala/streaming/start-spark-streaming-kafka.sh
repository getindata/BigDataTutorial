#!/bin/bash

ZOOKEEPER=$1
CLASS=$2
TOPIC=$3

spark-submit \
        --master yarn-client \
        --class "$CLASS" \
        target/scala-2.10/Sparking-assembly-0.1.jar \
        $ZOOKEEPER $TOPIC
