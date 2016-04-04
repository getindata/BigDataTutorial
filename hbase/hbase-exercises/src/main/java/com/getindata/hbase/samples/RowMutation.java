package com.getindata.hbase.samples;

import java.io.IOException;

import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RowMutations;
import org.apache.hadoop.hbase.util.Bytes;

import com.getindata.hbase.Connection;

public class RowMutation {
	public static void main(String[] args) throws IOException {
		HTable table = new HTable(Connection.hbase(), "user");

		RowMutations mutations = new RowMutations(Bytes.toBytes("grzegorz"));

		Put put = new Put(Bytes.toBytes("grzegorz"));

		put.add(Bytes.toBytes("job"), Bytes.toBytes("email"), new String("cccc@gmail.com").getBytes("UTF-8"));
		put.add(Bytes.toBytes("job"), Bytes.toBytes("city"), new String("Gdynia").getBytes("UTF-8"));

		mutations.add(put);

		table.mutateRow(mutations);
	}
}
