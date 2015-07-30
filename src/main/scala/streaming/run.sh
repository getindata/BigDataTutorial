#!/bin/bash -x
if [ "$#" -ne 1 ]; then
  echo "Usage: $0 <your "animal" login>" >&2
  exit 1
fi

NICKNAME=`echo $1 | tr '[:lower:]' '[:upper:]'`

export ZOOKEEPER=$(cat /etc/kafka/conf/server.properties | grep zookeeper.connect= | cut -d'=' -f 2)

export KAFKA=ip-10-36-25-139.us-west-2.compute.internal:6667,ip-10-41-19-96.us-west-2.compute.internal:6667

export PHOENIX=`echo ${ZOOKEEPER} | sed s'/,/;/g'`

sed "s/{{LOGIN}}/${NICKNAME}/g" sql/create_table.sql > sql/create_table_$1.sql
/opt/phoenix/bin/psql.py $PHOENIX sql/create_table_$1.sql


#creare hbase table
TABLE_NOT_EXISTS=`echo "exists '"${NICKNAME}_LOGEVENT_HBASE"'" | hbase shell | grep -ic 'not'`
if [[ $TABLE_NOT_EXISTS -eq 0 ]]; then
  echo "disable '"${NICKNAME}"_LOGEVENT_HBASE'" | hbase shell
  echo "drop '"${NICKNAME}"_LOGEVENT_HBASE'" | hbase shell
fi
echo "create '"${NICKNAME}"_LOGEVENT_HBASE', {NAME=>'main', VERSIONS => 1}" | hbase shell

SPARK_CLASSPATH=lib/hbase-protocol-1.1.1.jar spark-submit --class KafkaHbase target/scala-2.10/Sparking-assembly-0.1.jar $KAFKA "$1_logevent" $ZOOKEEPER  --master yarn-client --driver-memory 256m --executor-memory 128m --num-executors 2 

#--conf spark.executor.extraClassPath=lib/hbase-protocol-1.1.1.jar --conf spark.driver.extraClassPath=lib/hbase-protocol-1.1.1.jar
