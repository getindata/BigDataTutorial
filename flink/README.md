Export variables

```
export ZOOKEEPER=$(hostname):2181/kafka
export KAFKA=$(hostname):9092
```

Start ZooKeeper server:

```
./bin/zookeeper-server-start.sh config/zookeeper.properties
```

Start Kafka server:

```
./bin/kafka-server-start.sh config/server.properties
```

Create input topic:

```
# ./bin/kafka-topics.sh --create --zookeeper ${ZOOKEEPER} --replication-factor 1 --partitions 3 --topic user_activity
kafka-topics --create --zookeeper ${ZOOKEEPER} --replication-factor 1 --partitions 3 --topic user_activity
```

Create output topic:

```
# ./bin/kafka-topics.sh --create --zookeeper ${ZOOKEEPER} --replication-factor 1 --partitions 3 --topic top_songs
kafka-topics --create --zookeeper ${ZOOKEEPER} --replication-factor 1 --partitions 3 --topic top_songs
```

Start kafka producer:

```
# ./bin/kafka-console-producer.sh --broker-list ${KAFKA} --topic user_activity
kafka-console-producer --broker-list ${KAFKA} --topic user_activity
```

Start kafka consumer:

```
# ./bin/kafka-console-consumer.sh --zookeeper ${ZOOKEEPER} --topic top_songs
kafka-console-consumer --zookeeper ${ZOOKEEPER} --topic top_songs
```


Credits:
Marcin Kuthan -> https://github.com/mkuthan/example-flink-kafka
