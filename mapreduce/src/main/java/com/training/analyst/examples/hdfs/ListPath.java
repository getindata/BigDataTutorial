package com.training.analyst.examples.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class ListPath extends Configured implements Tool {

	public int run(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage: <path>");
			System.exit(2);
		}

		Configuration conf = getConf();
		FileSystem fs = FileSystem.get(conf);

		Path path = new Path(args[0]);
		FileStatus[] statuses = fs.listStatus(path);
		for (FileStatus status : statuses) {
			System.out.println(status.getPath());
		}

		return 0;
	}

	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(), new ListPath(), args);
	}
}
