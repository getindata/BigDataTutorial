Produce TSV events to a Kafka topic using Kafka Java Producer
=======

Run the demo
-------------------------
Remember to set necessary var envs in both terminals:

	# export varenvs
	export ZOOKEEPER=$(hostname):2181/kafka
	export KAFKA=$(hostname):9092
	export JAVA_HOME=/usr/java/jdk1.8.0_60

	export TOPIC=logevent

	# print varenvs
	echo $KAFKA
	echo $ZOOKEEPER
	echo $JAVA_HOME
	echo $TOPIC

(Re)create a Kafka topic:

	# create the topic
	kafka-topics --list --zookeeper $ZOOKEEPER
	kafka-topics --delete --zookeeper $ZOOKEEPER --topic $TOPIC
	kafka-topics --create --zookeeper $ZOOKEEPER --replication-factor 1 --partitions 3 --topic $TOPIC
	kafka-topics --list --zookeeper $ZOOKEEPER

Produce events using Kafka Java Producer:

	# build with dependencies
	mvn clean package -Pfull

	# produce some data to the topic
	vim src/main/java/com/getindata/tutorial/bigdatatutorial/kafka/LogEventTsvProducer.java
	$JAVA_HOME/bin/java -cp target/bigdatatutorial-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.getindata.tutorial.bigdatatutorial.kafka.LogEventTsvProducer $KAFKA $TOPIC true

Consume events using Kafka console consumer:

	# consume data using Kafka console consumer
	kafka-console-consumer --topic $TOPIC --zookeeper $ZOOKEEPER --from-beginning
