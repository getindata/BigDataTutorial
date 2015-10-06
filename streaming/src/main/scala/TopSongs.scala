import kafka.serializer.StringDecoder
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.rogach.scallop.exceptions.{Help, ScallopException, Exit, RequiredOptionNotFound}
import org.rogach.scallop.{LazyScallopConf, ScallopConf}

object TopSongs {

  def main(args: Array[String]) {

    val conf = new InputParams(args)

    // Parsing parameters
    val zookeeper = conf.zookeeper()
    val topic = conf.topic()
    val writeToHbase = conf.toHbase()
    val kafkaBrokers = conf.kafkaBrokers()

    // Defining the application name
    val sparkConf = new SparkConf().setAppName("TopSongs")

    // SparkThe main entry point for all streaming functionality:
    // later used to start, stop or recover the computation, also specifies the batch duration
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    // Prepare Kafka parameters
    val kafkaParams = Map(
      // specify the list of kafka brokers addresses
      "metadata.broker.list" -> kafkaBrokers,
      // wheter to start consuming from 'largest' or 'smallest' offset of Kafka partitions
      "auto.offset.reset" -> "largest"
    )

    // Connect to Kafka to fetch the log events
    val lines = KafkaUtils.createDirectStream[
      String, String, StringDecoder, StringDecoder
      ] (ssc, kafkaParams, Set(topic)).map(_._2)

    // Calulcate top songs in the real-time
    val songCounts = lines.transform(rdd => countTopSongs(rdd))

    if (writeToHbase) {
      // Store results into HBase
      songCounts.foreachRDD(rdd => saveToHBase(rdd.map(_.swap), zookeeper, topic.toUpperCase))
    } else {
      // Display the output
      songCounts.transform(rdd => rdd.zipWithIndex.filter(_._2 < 5).map(_._1)).print()
    }

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
      .sortByKey(ascending = false)
  }

  // Stores contents of given RDD into a HBase table
  def saveToHBase(rdd: RDD[(String, Long)], zkQuorum: String, tableName: String) = {
    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.quorum", zkQuorum)

    val jobConfig = new JobConf(conf)
    jobConfig.set(TableOutputFormat.OUTPUT_TABLE, tableName)
    jobConfig.setOutputFormat(classOf[TableOutputFormat])

    rdd.map { case ((songName, listenedCount)) => createHBaseRow(songName, listenedCount) }
      .saveAsHadoopDataset(jobConfig)
  }

  // Transforms objects to Put operations on a single HBaseRow
  def createHBaseRow(songName: String, listenedCount: Long) = {
    val record = new Put(Bytes.toBytes(songName))

    record.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("listenedTo"), Bytes.toBytes(listenedCount.toString))

    (new ImmutableBytesWritable, record)
  }

  private class InputParams(args: Array[String]) extends ScallopConf(args) {
    banner("Usage: TopSongs [--to-hbase] <zookeeper> <topic> <kafkaBrokers>")
    val toHbase = toggle(default = Some(false), noshort = true, descrYes = "Writes output to HBase instead of stdout",
      prefix = "not-", descrNo = "default")
    val zookeeper = trailArg[String](descr = "Zookeeper quorum address", required = true)
    val kafkaBrokers = trailArg[String](descr = "List of kafka brokers: kafkaBroker1:port,kafkaBroker2:port",
      required = true)
    val topic = trailArg[String](descr = "Name of the Kafka topic to read from. Also the name of table in hbase to " +
      "store to in case of --to-hbase option", required = true)

    errorMessageHandler = (msg) => {
      println(s"$printedName Error: $msg")
      printHelp()
      sys.exit(1)
    }
  }

}
