Flink Streaming Demo
=======

Code
-----
Review the code:
* Processing events: src/main/scala/songs/streaming/TopSongs.scala
* Parsing events: src/main/scala/songs/streaming/UserActivity.scala
* Connecting Kafka and submitting to cluster: src/main/scala/songs/streaming/KafkaFlinkStreaming.scala

Prerequisites
-----
This example runs on our CDH training cluster.

* Flink on YARN 1.1.3 (Hadoop 2.6.0, Scala 2.11)
  * It can be downloaded from http://ftp.piotrkosoft.net/pub/mirrors/ftp.apache.org/flink/flink-1.1.3/flink-1.1.3-bin-hadoop26-scala_2.11.tgz
* Kafka 0.9.0
* Hadoop 2.6.0

Export Variables
-----
```
export ZOOKEEPER=$(hostname):2181/kafka
export KAFKA=$(hostname):9092
export HADOOP_CONF_DIR=/etc/hadoop/conf
export JAVA_HOME=/usr/java/jdk1.8.0_60
```

Build Demo
-----
```
mvn clean package -Pbuild-jar
```

Submit To Cluster
-----
Go to the `flink-1.1.3` directory and sumbit the Flink app
```
./bin/flink run \
  -m yarn-cluster -yn 2 \
  -c songs.streaming.KafkaFlinkStreaming \
  ~/BigDataTutorial/flink/target/flink-scala-project-0.1.jar \
  --kafka $KAFKA --zookeeper $ZOOKEEPER --from-topic logevent --to-topic top_songs
```
Go to Flink App Master on YARN to see Flink Dashboard and metrics.

Consume events from the destination topic:
```
kafka-console-consumer --zookeeper $ZOOKEEPER --from-beginning --topic top_songs
```
Produce events to the source topic:
```
$JAVA_HOME/bin/java \
  -cp ~/BigDataTutorial/kafka/target/bigdatatutorial-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
  com.getindata.tutorial.bigdatatutorial.kafka.LogEventTsvProducer \
  $KAFKA logevent true
```
