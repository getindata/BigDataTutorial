import java.util.Properties

import kafka.producer._

import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf
import StreamingContext._
import org.apache.spark.rdd._
import org.apache.spark.SparkContext._

import org.apache.spark.Logging
import org.apache.log4j.{Level, Logger}


object KafkaRealtimeKPIs {

	def countTopSongs(rdd: RDD[String]): RDD[(Long, String)] = {
  		rdd
			.map(line => line.split("\t"))
			.map(fields => fields(1))
    			.map((_, 1L))
			.reduceByKey(_ + _)
			.map(fields => fields.swap)
			.sortByKey(false)
	}

	def main(args: Array[String]) {
	
		if (args.length < 2) {
			System.err.println("Usage: KafkaRealtimeKPIs <zookeeper> <topic> ")
			System.exit(1)
		}

		// parsing parameters
		val zookeeper = args(0)
		val topic = args(1)

		// initialization and configuration
		val sparkConf = new SparkConf().setAppName("KafkaRealtimeKPIs")
		val ssc =  new StreamingContext(sparkConf, Seconds(10))

		// connect to Kafka to fetch the log events
		val topicMap = Map(topic -> 1)
		val lines = KafkaUtils.createStream(ssc, zookeeper, "spark-streaming", topicMap).map(_._2)

		// calulcate top songs in the real-time
		val songCounts = lines.transform(rdd => countTopSongs(rdd))

		// display the output
		songCounts.print()

		// start streaming process
		ssc.start()
		ssc.awaitTermination()
	}
}
