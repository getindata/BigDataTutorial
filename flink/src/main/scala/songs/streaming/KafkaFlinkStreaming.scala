// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package songs.streaming

import java.util.concurrent.TimeUnit

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaProducer09,FlinkKafkaConsumer09}
import org.apache.flink.streaming.util.serialization.{DeserializationSchema, SerializationSchema}
import org.apache.flink.streaming.util.serialization.SimpleStringSchema


object KafkaFlinkStreaming {

  val window = Time.of(10, TimeUnit.SECONDS)

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val params: ParameterTool = ParameterTool.fromArgs(args)
    val kafkaBrokers = params.getRequired("kafka")
    val zkQuorum = params.getRequired("zookeeper")
    val fromTopic = params.get("from-topic", "user_activity")
    val toTopic = params.get("to-topic", "top_songs")
    val debug = params.getBoolean("debug", false)

    val kafkaConsumerProperties = Map(
      "zookeeper.connect" -> zkQuorum,
      "group.id" -> "flink",
      "bootstrap.servers" -> kafkaBrokers,
      "auto.offset.reset" -> "earliest"
    )

    val kafkaConsumer = new FlinkKafkaConsumer09[String](
      fromTopic,
      new SimpleStringSchema(),
      kafkaConsumerProperties
    )

    val kafkaProducer = new FlinkKafkaProducer09[String](
      kafkaBrokers,
      toTopic,
      new SimpleStringSchema()
    )

    val events = env.addSource(kafkaConsumer)

    val topSongs = TopSongs.topSongs(events, window).map(_.toString)

    if (debug) {
      // print result on stdout
      topSongs.print()
    } else {
      // write result to Kafka
      topSongs.addSink(kafkaProducer)
    }

    // execute the transformation pipeline
    env.execute("Top Songs")
  }

  implicit def map2Properties(map: Map[String, String]): java.util.Properties = {
    (new java.util.Properties /: map) { case (props, (k, v)) => props.put(k, v); props }
  }
}
