package com.getindata.hbase.exercises;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.hadoop.hbase.client.HTable;

import com.getindata.hbase.Connection;
import com.google.common.base.Splitter;

public class LoadDataToTable {

	public static void main(String[] args) throws IOException {
		System.out.println("Starting Program: "+ LoadDataToTable.class.getCanonicalName());
		System.out.println("Readning parameters...");
		String tableName="<your-username-here>.user";
		if(args.length>0){
			tableName=args[0];
		}
		System.out.println("tablename:"+tableName);
		loadUserData(tableName);
		System.out.println("End Of Program`");
	}

	private static boolean loadUserData(String tableName) throws IOException,
			FileNotFoundException {
		HTable table = new HTable(Connection.hbase(), tableName);

		BufferedReader br = getFileBuffer("src/main/resources/users.txt");

		String line = null;
		while ((line = br.readLine()) != null) {
			Iterable<String> split = Splitter.on(',').trimResults()
					.omitEmptyStrings().split(line);
			Iterator<String> iterator = split.iterator();
			String name = iterator.next();
			String city = iterator.next();
			String email = iterator.next();
			System.out.println("name=" + name + ",city=" + city + ", email="
					+ email);
			// ================================
			// TODO put your code here

			// ================================
		}

		br.close();
		return true;
	}

	private static BufferedReader getFileBuffer(String fin)
			throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(fin);

		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		return br;
	}

}
