import kafka.serializer.StringDecoder
import org.apache.spark.SparkContext
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import it.nerdammer.spark.hbase._

/**
 * Created by mwiewior on 30.07.15.
 */
class KafkaHbase {
  def main(args: Array[String]) {

    if (args.length < 2) {
      System.err.println("Usage: KafkaHBase <kafkaBroker1:port,kafkaBroker2:port> <topic> <phoenixURL>")
      System.exit(1)
    }


    // parsing parameters
    val kafkaBrokers = args(0)
    val topic = args(1)
    val phoenixURL = args(2)
    val kafkaParams = Map("metadata.broker.list" -> kafkaBrokers,"auto.offset.reset"->"smallest")
    val sc = new SparkContext("local", "KafkaHBase")
    val ssc = new StreamingContext(sc, Seconds(60) )

    val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, Set(topic))
    kafkaStream.foreachRDD{
      rdd=>
        rdd
          .map(_._2.split("\t"))
          .map{case Array(ts:String,serv:String,userId:String,event:String,songId:String,duration:String) =>
            (ts+userId+songId,ts,serv,userId.toInt,event,songId.toInt,duration.toInt) }
          .toHBaseTable(topic+"_hbase")
          .toColumns("timestamp", "server","user_id","event","song_id","song_duration")
          .inColumnFamily("main")
          .save()
    }


  }

}
