package com.getindata.hbase.samples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Map.Entry;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.getindata.hbase.Connection;

public class GetExample {

	public static void main(String[] args) throws IOException {
		HTable table = new HTable(Connection.hbase(), "user");

		List<Get> getList = prepareGetOperationsList("grzegorz");
		Result[] results = table.get(getList);
		for (Result result : results) {

			for (Entry<byte[], NavigableMap<byte[], byte[]>> columnFamily : result.getNoVersionMap().entrySet()) {
				for (Entry<byte[], byte[]> column : columnFamily.getValue().entrySet()) {
					;
					System.out.println("[rowKey:" + new String(result.getRow()) + "]  "
							+ new String(columnFamily.getKey()) + ":" + new String(column.getKey()) + "= "
							+ new String(column.getValue(), "UTF-8"));
				}
			}

		}
	}

	public static List<Get> prepareGetOperationsList(String... keys) {
		List<Get> getOperations = new ArrayList<Get>();

		for (String rowId : keys) {
			Get get = new Get(Bytes.toBytes(rowId));
			getOperations.add(get);
		}

		return getOperations;
	}

}
