package com.getindata.hbase;

import org.apache.hadoop.hbase.HBaseConfiguration;

public class Connection {

	public static org.apache.hadoop.conf.Configuration hbase() {
		org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
		conf.set("hbase.zookeeper.quorum", "localhost");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", "hdfs://EikonPAbeta/hbase");
		conf.set("hbase.client.socks.proxy.host.name", "127.0.0.1");
		conf.set("hbase.client.socks.proxy.port", "7788");
		return HBaseConfiguration.create(conf);
	}

}
