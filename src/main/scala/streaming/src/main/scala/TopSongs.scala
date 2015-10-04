import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, HBaseConfiguration}
import org.apache.hadoop.hbase.client.{HBaseAdmin, Put}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._

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
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    // Connect to Kafka to fetch the log events
    val topicMap = Map(topic -> 1)
    val lines = KafkaUtils.createStream(ssc, zookeeper, "spark-streaming", topicMap).map(_._2)
    lines.print(2)

    // Calulcate top songs in the real-time
    val songCounts = lines.transform(rdd => countTopSongs(rdd))

    // Display the output
    songCounts.transform(rdd => rdd.zipWithIndex.filter(_._2 < 5).map(_._1)).print()

    // Store results into HBase
    songCounts.foreachRDD(rdd => saveToHBase(rdd.map(_.swap), zookeeper, topic.toUpperCase + "_HBASE"))

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

}
