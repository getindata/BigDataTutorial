import kafka.serializer.StringDecoder
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import it.nerdammer.spark.hbase._
import org.apache.phoenix.spark._
/**
 * Created by mwiewior on 30.07.15.
 */
object KafkaHbase {
  def main(args: Array[String]) {

    if (args.length < 2) {
      System.err.println("Usage: KafkaHBase <kafkaBroker1:port,kafkaBroker2:port> <topic> <zkQuorum>")
      System.exit(1)
    }


    // parsing parameters
    val kafkaBrokers = args(0)
    val topic = args(1)
    val zkQuorum = args(2)
    val kafkaParams = Map("metadata.broker.list" -> kafkaBrokers,"auto.offset.reset"->"smallest")
    val sparkConf = new SparkConf()
    sparkConf.setMaster("local")
    sparkConf.setAppName("KafkaHBase")
    sparkConf.set("spark.hbase.host", zkQuorum)
    val sc = new SparkContext(sparkConf)
    val ssc = new StreamingContext(sc, Seconds(10) )

    val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, Set(topic))
    kafkaStream.foreachRDD{
      rdd=>
	/*Write to HBase*/	 
       rdd
          .map(_._2.split("\t"))
          .map{case Array(ts:String,serv:String,userId:String,event:String,songId:String,duration:String) =>
            (ts+"|"+userId+"|"+songId,ts,serv,userId.toInt,event,songId.toInt,duration.toInt) }
          .toHBaseTable(topic.toUpperCase+"_HBASE")
          .toColumns("timestamp", "server","user_id","event","song_id","song_duration")
          .inColumnFamily("main")
          .save();
      /*Write to Phoenix*/
      rdd
       .map(_._2.split("\t"))
       .map{case Array(ts:String,serv:String,userId:String,event:String,songId:String,duration:String) =>
            (ts,userId.toInt,songId.toInt,serv,event,duration.toInt) }
       .saveToPhoenix(
      topic.toUpperCase+"_PHOENIX",
    Seq("TIMESTAMP","USER_ID","SONG_ID","SERVER","EVENT","SONG_DURATION"),
    zkUrl = Some(zkQuorum.replace(":",";"))
    )

 
    }
    ssc.start()
    ssc.awaitTermination() 

  }

}
