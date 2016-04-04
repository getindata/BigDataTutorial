package com.getindata.hbase.samples;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

import com.getindata.hbase.Connection;

public class ScanExample {
	public static void main(String[] args) throws IOException {
		HTable table = new HTable(Connection.hbase(), "user");

		Scan scan = new Scan();
		scan.setCaching(50);
		// scan.addFamily(Bytes.toBytes("job"));
		// SingleColumnValueExcludeFilter filter = new
		// SingleColumnValueExcludeFilter(Bytes.toBytes("job"),
		// Bytes.toBytes("grzegorzkopuc@gmail.com"), CompareOp.EQUAL,
		// Bytes.toBytes("email"));

		// scan.setTimeRange(minStamp, maxStamp);
		// scan.setFilter(filter);

		ResultScanner scanner = table.getScanner(scan);
		Iterator<Result> iterator = scanner.iterator();
		while (iterator.hasNext()) {
			Result next = iterator.next();

			for (Entry<byte[], NavigableMap<byte[], byte[]>> columnFamily : next.getNoVersionMap().entrySet()) {
				for (Entry<byte[], byte[]> column : columnFamily.getValue().entrySet()) {
					;
					System.out.println("[rowKey:" + new String(next.getRow()) + "]  "
							+ new String(columnFamily.getKey()) + ":" + new String(column.getKey()) + "= "
							+ new String(column.getValue(), "UTF-8"));
				}
			}
		}

	}
}
