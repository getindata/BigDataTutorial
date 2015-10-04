DataTutorial
=======

Kafka
-----

SSH into some slave node (the edgenode can not be used, because it does not have Kafka libraries and configuration)

	# export varenvs
	export KAFKA=$(hostname):6667
	export ZOOKEEPER=$(cat /etc/kafka/conf/server.properties | grep zookeeper.connect= | cut -d'=' -f 2)

	# print varenvs
	echo $KAFKA
	echo $ZOOKEEPER

	# interact with Kafka
	/usr/hdp/current/kafka-broker/bin/kafka-topics.sh --list --zookeeper $ZOOKEEPER
	/usr/hdp/current/kafka-broker/bin/kafka-topics.sh --create --zookeeper $ZOOKEEPER --replication-factor 1 --partitions 1 --topic hello
	/usr/hdp/current/kafka-broker/bin/kafka-console-consumer.sh --topic hello --zookeeper $ZOOKEEPER --from-beginning

	/usr/hdp/current/kafka-broker/bin/kafka-console-producer.sh --broker-list $KAFKA --topic hello
	# type some message and quit using CTRL+C
	/usr/hdp/current/kafka-broker/bin/kafka-console-consumer.sh --topic hello --zookeeper $ZOOKEEPER --from-beginning
	# quit using CTLR+C

Spark Streaming And Kafka
-------------------------

SSH into some slave node (the edgenode can not be used, because it does not have Kafka libraries and configuration)
Before the demo, please install following tools:

	curl https://bintray.com/sbt/rpm/rpm | tee /etc/yum.repos.d/bintray-sbt-rpm.repo
	yum install -y sbt vim

During the demo

	# build with dependencies
	mvn package -Pfull

	# export varenvs
	export KAFKA=$(hostname):6667
	export ZOOKEEPER=$(cat /etc/kafka/conf/server.properties | grep zookeeper.connect= | cut -d'=' -f 2)
	export JAVA_HOME=$(cat /etc/hadoop/conf/hadoop-env.sh | grep JAVA_HOME= | cut -d'=' -f 2)

	# print varenvs
	echo $KAFKA
	echo $ZOOKEEPER
	echo $JAVA_HOME

	# create the topic
	/usr/hdp/current/kafka-broker/bin/kafka-topics.sh --list --zookeeper $ZOOKEEPER
	/usr/hdp/current/kafka-broker/bin/kafka-topics.sh --create --zookeeper $ZOOKEEPER --replication-factor 1 --partitions 1 --topic logevent
	/usr/hdp/current/kafka-broker/bin/kafka-topics.sh --list --zookeeper $ZOOKEEPER

	# produce some data to the topic
	vim src/main/java/com/getindata/tutorial/bigdatatutorial/kafka/LogEventTsvProducer.java
	$JAVA_HOME/bin/java -cp target/bigdatatutorial-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.getindata.tutorial.bigdatatutorial.kafka.LogEventTsvProducer $KAFKA $ZOOKEEPER logevent

	# ---------------------
	# open the new terminal
	# ---------------------

	export ZOOKEEPER=$(cat /etc/kafka/conf/server.properties | grep zookeeper.connect= | cut -d'=' -f 2)

	# consume data using Kafka console consumer
	/usr/hdp/current/kafka-broker/bin/kafka-console-consumer.sh --topic logevent --zookeeper $ZOOKEEPER --from-beginning

	# start Spark Streaming app
	cd src/main/scala/streaming
	vim src/main/scala/TopSongs.scala
	sbt assembly
	./bin/start.sh TopSongs $ZOOKEEPER logevent
