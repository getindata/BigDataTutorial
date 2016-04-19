package com.training.analyst.examples.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class ArtistCount extends Configured implements Tool {

	public static class ArtistMapper extends
			Mapper<LongWritable, Text, Text, IntWritable> {

		private final static IntWritable one = new IntWritable(1);
		private Text artist = new Text();

		/*
		 * The method to extract artist name from the log line
		 */
		private String getArtistName(String line) {
			return line.split("\t")[0];
		}

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			String artistName = getArtistName(value.toString());
			artist.set(artistName);
			// TODO: fix line below
			context.write(, );
		}
	}

	public static class IntSumReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		// TODO: fix line below
		public void reduce(, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	public int run(String[] args) throws Exception {
		
		if (args.length != 2) {
			System.err.println("Usage: <input> <output>");
			System.exit(2);
		}

		Configuration conf = getConf();
		Job job = Job.getInstance(conf);
		job.setJobName("Artist Count");
		job.setJarByClass(ArtistCount.class);

		// TODO: fix line below
		job.setMapperClass();
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		int result = job.waitForCompletion(true) ? 0 : 1;
		return result;

	}

	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(), new ArtistCount(), args);
	}
}
