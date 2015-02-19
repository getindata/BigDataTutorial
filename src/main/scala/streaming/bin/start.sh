#!/bin/bash

${SPARK_DIR}/bin/spark-submit \
	--jars target/bigdatatutorial-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
	--master local[4]
	--class "KafkaRealtimeKPIs" \
	target/scala-2.10/Sparking-assembly-0.1.jar
