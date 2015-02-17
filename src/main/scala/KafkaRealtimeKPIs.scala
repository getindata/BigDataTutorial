import org.apache.spark.streaming.kafka._


object KafkaRealtimeKPIs {
	def main(args: Array[String]) {

	val kafkaStream = KafkaUtils.createStream(
 		streamingContext, localhost:2170, "spark-streaming", 1)
	}
}
