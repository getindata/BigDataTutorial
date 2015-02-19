import java.util.Properties

import kafka.producer._

import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf
import StreamingContext._ 

object KafkaRealtimeKPIs {
	def main(args: Array[String]) {
		val sparkConf = new SparkConf().setAppName("KafkaRealtimeKPIs")
    		val ssc =  new StreamingContext(sparkConf, Seconds(2))
		val topicMap = Map("logevent" -> 1) 

		val events = KafkaUtils.createStream(ssc, "localhost:2181", "spark-streaming", topicMap).map(_._2)

    		val wordCounts = events.map(x => (x, 1L)).reduceByKey(_ + _)
    		wordCounts.print()

    		ssc.start()
    		ssc.awaitTermination()
	}
}
