name := "Sparking"
 
version := "0.1"
 
scalaVersion := "2.9.1"
 
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.6.1" % "test"
)

libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "1.2.0"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.2.0"
