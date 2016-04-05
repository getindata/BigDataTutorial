#!/bin/bash

CLASS=$1

spark-submit \
        --master yarn-client \
        --jars /usr/hdp/current/hbase-client/lib/hbase-common.jar,/usr/hdp/current/hbase-client/lib/hbase-client.jar,/usr/hdp/current/hbase-client/lib/hbase-server.jar,/opt/cloudera/parcels/CDH/lib/hbase/hbase-client.jar,/opt/cloudera/parcels/CDH/lib/hbase/hbase-server.jar,/opt/cloudera/parcels/CDH/lib/hbase/hbase-common.jar \
        --class "$CLASS" \
        target/scala-2.10/Sparking-assembly-0.1.jar \
        "${@:2}"
