Produce TSV events to Kafka Topic using Java Producer
=======

Run the demo
-------------------------
In both terminals:

	# export varenvs
	export ZOOKEEPER=$(hostname):2181/kafka
	export KAFKA=$(hostname):9092
	export JAVA_HOME=$(cat /etc/hadoop/conf/hadoop-env.sh | grep JAVA_HOME= | cut -d'=' -f 2)
	export TOPIC=logevent

	# print varenvs
	echo $KAFKA
	echo $ZOOKEEPER
	echo $JAVA_HOME
	echo $TOPIC

In terminal (1):

	# build with dependencies
	mvn clean package -Pfull

	# create the topic
	kafka-topics --list --zookeeper $ZOOKEEPER
	kafka-topics --delete --zookeeper $ZOOKEEPER --topic $TOPIC
	kafka-topics --create --zookeeper $ZOOKEEPER --replication-factor 1 --partitions 2 --topic $TOPIC
	kafka-topics --list --zookeeper $ZOOKEEPER

	# produce some data to the topic
	vim src/main/java/com/getindata/tutorial/bigdatatutorial/kafka/LogEventTsvProducer.java
	$JAVA_HOME/bin/java -cp target/bigdatatutorial-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.getindata.tutorial.bigdatatutorial.kafka.LogEventTsvProducer $KAFKA $TOPIC true

In terminal (2):

	# consume data using Kafka console consumer
	kafka-console-consumer --topic $TOPIC --zookeeper $ZOOKEEPER --from-beginning
