#!/bin/bash

SPARK_DIR=/root/spark

${SPARK_DIR}/bin/spark-submit \
	--master local[4] \
	--class "KafkaRealtimeKPIs" \
	target/scala-2.10/Sparking-assembly-0.1.jar \
	"$@"
