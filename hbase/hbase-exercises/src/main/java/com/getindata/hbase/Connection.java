package com.getindata.hbase;

import org.apache.hadoop.hbase.HBaseConfiguration;

public class Connection {

	private static org.apache.hadoop.conf.Configuration hbaseTepmDeprecated() {
		org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
		conf.set("hbase.zookeeper.quorum", "localhost");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", "hdfs://EikonPAbeta/hbase");
		conf.set("hbase.client.socks.proxy.host.name", "127.0.0.1");
		conf.set("hbase.client.socks.proxy.port", "7788");
		return HBaseConfiguration.create(conf);
	}

	
	
	public static org.apache.hadoop.conf.Configuration hbase() {
		org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
		conf.set("hbase.master", "hdfs://ec2-54-184-56-148.us-west-2.compute.amazonaws.com:8020/hbase");
		conf.set("hbase.zookeeper.quorum", "ip-10-204-133-137.us-west-2.compute.internal,ip-10-132-4-227.us-west-2.compute.internal,ip-10-46-12-222.us-west-2.compute.internal,ip-10-46-0-86.us-west-2.compute.internal,ip-10-230-25-249.us-west-2.compute.internal");
		conf.set("hbase.zookeeper.property.clientPort", "2181");

//		conf.set("hbase.zookeeper.quorum", "ec2-54-184-56-148.us-west-2.compute.amazonaws.com,ec2-54-185-181-21.us-west-2.compute.amazonaws.com,ec2-54-184-0-167.us-west-2.compute.amazonaws.com,ec2-54-188-245-78.us-west-2.compute.amazonaws.com,ec2-54-190-210-85.us-west-2.compute.amazonaws.com");
//		conf.set("hbase.master", "hdfs://ip-10-204-133-137.us-west-2.compute.internal:8020/hbase");
		return HBaseConfiguration.create(conf);
	}
}
