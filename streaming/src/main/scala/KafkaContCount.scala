import java.util.Properties

import kafka.producer._

import org.apache.spark.streaming._
import org.apache.spark.HashPartitioner
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf
import StreamingContext._
import org.apache.spark.rdd._
import org.apache.spark.SparkContext._

import org.apache.spark.Logging
import org.apache.log4j.{Level, Logger}


object KafkaContCount {

	def main(args: Array[String]) {
	
		if (args.length < 2) {
			System.err.println("Usage: KafkaContCount <zookeeper> <topic> ")
			System.exit(1)
		}

		// parsing parameters
		val zookeeper = args(0)
		val topic = args(1)

		val updateFunc = (values: Seq[Int], state: Option[Int]) => {
			val previousCount = state.getOrElse(0)
			Some(values.sum + previousCount)
		}

		// initialization and configuration
		val sparkConf = new SparkConf().setAppName("KafkaRealtimeKPIs")
		val ssc =  new StreamingContext(sparkConf, Seconds(5))

		// connect to Kafka to fetch the log events
		val topicMap = Map(topic -> 1)
		val lines = KafkaUtils.createStream(ssc, zookeeper, "spark-streaming", topicMap).map(_._2)

		// calulcate top songs in the real-time
		ssc.checkpoint(".")
		val initialRDD = ssc.sparkContext.parallelize(List[(String, String)]())
		val batchSongCounts = lines.map(_.split("\t")(4)).map((_,1))
		val totalCounts = batchSongCounts.updateStateByKey(updateFunc)
		val top = totalCounts.map(_.swap).transform(rdd => rdd.sortByKey(ascending=false))

		// display the output
		// songCounts.print()
		top.transform(rdd => rdd.zipWithIndex.filter(_._2 < 5).map(_._1)).print()

		// start streaming process
		ssc.start()
		ssc.awaitTermination()
	}
}
