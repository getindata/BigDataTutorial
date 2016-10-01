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
./bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic user_activity
```

Create output topic:

```
./bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic top_songs
```

Start kafka producer:

```
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic user_activity
```

Start kafka consumer:

```
./bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic top_songs
```


Credits:
Marcin Kuthan -> https://github.com/mkuthan/example-flink-kafka