name := "Sparking"
 
version := "0.1"
 
scalaVersion := "2.10.4"
 
libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "1.3.1"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "1.3.1"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.3.1"
libraryDependencies += "org.apache.spark" % "spark-streaming-twitter_2.10" % "1.3.1"
libraryDependencies += "it.nerdammer.bigdata" % "spark-hbase-connector_2.10" % "1.0.0"
libraryDependencies +=  "org.apache.hbase" % "hbase-common" % "0.98.11-hadoop2" excludeAll(ExclusionRule(organization = "javax.servlet", name="javax.servlet-api"), ExclusionRule(organization = "org.mortbay.jetty", name="jetty"), ExclusionRule(organization = "org.mortbay.jetty", name="servlet-api-2.5"))
libraryDependencies +=  "org.apache.hbase" % "hbase-client" % "0.98.11-hadoop2" excludeAll(ExclusionRule(organization = "javax.servlet", name="javax.servlet-api"), ExclusionRule(organization = "org.mortbay.jetty", name="jetty"), ExclusionRule(organization = "org.mortbay.jetty", name="servlet-api-2.5"))
libraryDependencies +=  "org.apache.hbase" % "hbase-server" % "0.98.11-hadoop2" excludeAll(ExclusionRule(organization = "javax.servlet", name="javax.servlet-api"), ExclusionRule(organization = "org.mortbay.jetty", name="jetty"), ExclusionRule(organization = "org.mortbay.jetty", name="servlet-api-2.5"))
libraryDependencies += "org.apache.phoenix" % "phoenix-spark" % "4.4.0-HBase-0.98"
libraryDependencies +=  "org.apache.phoenix" % "phoenix-core" % "4.4.0-HBase-0.98"
libraryDependencies +=  "sqlline" % "sqlline" % "1.1.9"


mergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf")          => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")      => MergeStrategy.discard
  case "log4j.properties"                                  => MergeStrategy.discard
  case m if m.toLowerCase.startsWith("meta-inf/services/") => MergeStrategy.filterDistinctLines
  case "reference.conf"                                    => MergeStrategy.concat
  case _                                                   => MergeStrategy.first
}
