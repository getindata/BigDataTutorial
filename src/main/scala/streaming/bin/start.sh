#!/bin/bash

CLASS=$1

spark-submit \
        --master yarn-client \
        --class "$CLASS" \
        target/scala-2.10/Sparking-assembly-0.1.jar \
        "${@:2}"
