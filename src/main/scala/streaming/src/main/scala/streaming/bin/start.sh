#!/bin/bash

SPARK_DIR=/root/spark

${SPARK_DIR}/bin/spark-shell \
	--libjars /root/BigDataTutorial/target/bigdatatutorial-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
	--master local[4]

	--class "KafkaRealtimeKPIs" \
	/root/BigDataTutorial/src/main/scala/streaming/target/scala-2.10/Sparking-assembly-0.1.jar
