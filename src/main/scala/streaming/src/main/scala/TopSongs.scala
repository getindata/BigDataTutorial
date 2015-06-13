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

object TopSongs {

	def main(args: Array[String]) {
	
		if (args.length < 2) {
			System.err.println("Usage: TopSongs <zookeeper> <topic> ")
			System.exit(1)
		}

		// Parsing parameters
		val zookeeper = args(0)
		val topic = args(1)

                // Defining the application name
		val sparkConf = new SparkConf().setAppName("TopSongs")
                // SparkThe main entry point for all streaming functionality:
                //   later used to start, stop or recover the computation, also specifies the batch duration
		val ssc =  new StreamingContext(sparkConf, Seconds(5))

		// Connect to Kafka to fetch the log events
		val topicMap = Map(topic -> 1)
		val lines = KafkaUtils.createStream(ssc, zookeeper, "spark-streaming", topicMap).map(_._2)

		// Calulcate top songs in the real-time
		val songCounts = lines.transform(rdd => countTopSongs(rdd))

		// Display the output
		songCounts.transform(rdd => rdd.zipWithIndex.filter(_._2 < 5).map(_._1)).print()

		// Start streaming process
		ssc.start()
		ssc.awaitTermination()
	}


        // RDD-to-RDD function that can on operates on DStream (discretized stream)
        // DStream is a sequence of data arriving over time

        def countTopSongs(rdd: RDD[String]): RDD[(Long, String)] = {
                rdd
                        .map(line => line.split("\t"))
                        .map(fields => fields(4))
                        .map((_, 1L))
                        .reduceByKey(_ + _)
                        .map(fields => fields.swap)
                        .sortByKey(ascending=false)
        }
}
