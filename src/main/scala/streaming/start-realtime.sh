#!/bin/bash

export ZOOKEEPER=ip-172-31-20-167.us-west-2.compute.internal:2181

spark-submit \
        --master yarn-client \
        --class "KafkaRealtimeKPIs" \
        target/scala-2.10/Sparking-assembly-0.1.jar \
        $ZOOKEEPER $1
