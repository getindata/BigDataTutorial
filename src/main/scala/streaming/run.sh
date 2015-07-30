#!/bin/bash
export ZOOKEEPER=$(cat /etc/kafka/conf/server.properties | grep zookeeper.connect= | cut -d'=' -f 2)

export KAFKA=ip-10-36-25-139.us-west-2.compute.internal:6667,ip-10-41-19-96.us-west-2.compute.internal:6667

SPARK_CLASSPATH=lib/hbase-protocol-1.1.1.jar spark-submit --class KafkaHbase target/scala-2.10/Sparking-assembly-0.1.jar $KAFKA "marek_logevent" $ZOOKEEPER  --master yarn-client --driver-memory 256m --executor-memory 128m --num-executors 2 

#--conf spark.executor.extraClassPath=lib/hbase-protocol-1.1.1.jar --conf spark.driver.extraClassPath=lib/hbase-protocol-1.1.1.jar
