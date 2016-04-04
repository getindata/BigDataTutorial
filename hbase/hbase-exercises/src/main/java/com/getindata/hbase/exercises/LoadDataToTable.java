package com.getindata.hbase.exercises;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.hadoop.hbase.client.HTable;

import com.getindata.hbase.Connection;
import com.google.common.base.Splitter;

public class LoadDataToTable {

	public static void main(String[] args) throws IOException {
		HTable table = new HTable(Connection.hbase(), "user");

		String fin = "src/main/resources/users.txt";
		FileInputStream fis = new FileInputStream(fin);

		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line = null;
		while ((line = br.readLine()) != null) {
			Iterable<String> split = Splitter.on(',').trimResults().omitEmptyStrings().split(line);
			Iterator<String> iterator = split.iterator();
			String name = iterator.next();
			String city = iterator.next();
			String email = iterator.next();
			System.out.println("name=" + name + ",city=" + city + ", email=" + email);
			// ================================
			// TODO put your code here

			// ================================
		}

		br.close();
	}

}
