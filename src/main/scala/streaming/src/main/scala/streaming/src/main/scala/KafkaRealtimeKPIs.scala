import java.util.Properties

import kafka.producer._

import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf
import StreamingContext._ 

/*
import org.apache.avro.generic.GenericData._
import com.getindata.tutorial.bigdatatutorial.kafka.camus._
*/

object KafkaRealtimeKPIs {
	def main(args: Array[String]) {
		val sparkConf = new SparkConf().setAppName("KafkaRealtimeKPIs")
    		val ssc =  new StreamingContext(sparkConf, Seconds(2))
/*
		val decoder = new LogEventMessageDecoder()
*/
		val topicMap = Map("logevent" -> 1) 

		val events = KafkaUtils.createStream(ssc, "localhost:2181", "spark-streaming", topicMap).map(_._2)
		/* 
		val decoded = events.map(event => decoder.decode(event.getBytes()).getRecord())

    		val wordCounts = decoded.map(record => (record.get("songid"), 1L)).reduceByKey(_ + _)
    		wordCounts.print()
		*/

    		ssc.start()
    		ssc.awaitTermination()
	}
}
