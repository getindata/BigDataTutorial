BigDataTutorial
=======
	
Kafka
-----

	# export varenvs
	export KAFKA=10.0.133.49:6667,10.0.138.115:6667,10.0.161.248:6667
	export ZOOKEEPER=$(cat /etc/kafka/conf/server.properties | grep zookeeper.connect= | cut -d'=' -f 2)

	# interact with Kafka
	/usr/hdp/current/kafka-broker/bin/kafka-topics.sh --list --zookeeper $ZOOKEEPER
	/usr/hdp/current/kafka-broker/bin/kafka-topics.sh --create --zookeeper $ZOOKEEPER --replication-factor 1 --partitions 1 --topic hello
	/usr/hdp/current/kafka-broker/bin/kafka-console-producer.sh --broker-list $KAFKA --topic hello
	/usr/hdp/current/kafka-broker/bin/kafka-console-consumer.sh --topic hello --zookeeper $ZOOKEEPER --from-beginning


Spark Streaming And Kafka
-------------------------

	# build with dependencies
	mvn package -Pfull

	# export varenvs
	export KAFKA=10.0.133.49:6667,10.0.138.115:6667,10.0.161.248:6667
	export ZOOKEEPER=$(cat /etc/kafka/conf/server.properties | grep zookeeper.connect= | cut -d'=' -f 2)

	# create the topic
	/usr/hdp/current/kafka-broker/bin/kafka-topics.sh --list --zookeeper $ZOOKEEPER
	/usr/hdp/current/kafka-broker/bin/kafka-topics.sh --create --zookeeper $ZOOKEEPER --replication-factor 1 --partitions 1 --topic logevent
	/usr/hdp/current/kafka-broker/bin/kafka-topics.sh --list --zookeeper $ZOOKEEPER

	# produce some data to the topic
	vim src/main/java/com/getindata/tutorial/bigdatatutorial/kafka/LogEventTsvProducer.java
	java -cp target/bigdatatutorial-0.0.1-SNAPSHOT-jar-with-dependencies.jar  com.getindata.tutorial.bigdatatutorial.kafka.LogEventTsvProducer $KAFKA $ZOOKEEPER logevent
	/usr/hdp/current/kafka-broker/bin/kafka-console-consumer.sh --topic logevent --zookeeper $ZOOKEEPER --from-beginning

	# start Spark Streaming app
	cd src/main/scala/streaming
	vim src/main/scala/TopSongs.scala
	sbt assembly
	./bin/start.sh TopSongs $ZOOKEEPER logevent

	# in a separate shell produce some data to the topic
