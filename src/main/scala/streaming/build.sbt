name := "Sparking"

version := "0.1"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "1.3.1" % "provided",
  "org.apache.spark" % "spark-streaming_2.10" % "1.3.1" % "provided",
  "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.3.1",
  "org.apache.spark" % "spark-streaming-twitter_2.10" % "1.3.1",
  "org.apache.hbase" % "hbase-common" % "1.1.1" % "provided",
  "org.apache.hbase" % "hbase-client" % "1.1.1" % "provided",
  "org.apache.hbase" % "hbase-server" % "1.1.1" % "provided"
)


assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$") => MergeStrategy.discard
  case "log4j.properties" => MergeStrategy.discard
  case m if m.toLowerCase.startsWith("meta-inf/services/") => MergeStrategy.filterDistinctLines
  case "reference.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}
