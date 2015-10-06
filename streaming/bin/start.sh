#!/bin/bash

CLASS=$1

spark-submit \
        --master yarn-client \
        --jars /usr/hdp/current/hbase-client/lib/hbase-common.jar,/usr/hdp/current/hbase-client/lib/hbase-client.jar,/usr/hdp/current/hbase-client/lib/hbase-server.jar \
        --class "$CLASS" \
        target/scala-2.10/Sparking-assembly-0.1.jar \
        "${@:2}"
