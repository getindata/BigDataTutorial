package com.getindata.hbase.samples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.getindata.hbase.Connection;

public class PutExample {

	public static void main(String[] args) throws IOException {
		HTable table = new HTable(Connection.hbase(), "user");
		List<Put> putList = new ArrayList<Put>();
		Put put = new Put(Bytes.toBytes("grzegorz2"));

		put.add(Bytes.toBytes("job"), Bytes.toBytes("email"), new String("c@gmail.com").getBytes("UTF-8"));

		putList.add(put);
		table.put(putList);
	}

}
