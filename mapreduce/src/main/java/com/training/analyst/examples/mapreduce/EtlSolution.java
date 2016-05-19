package com.training.analyst.examples.mapreduce;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
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

public class EtlSolution extends Configured implements Tool {

	public static class EtlSolutionMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

		private final static IntWritable ONE = new IntWritable(1);

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			String[] split = value.toString().split("\t");

			if (split[3].equals("SongPlayed") && isDurationCorrect(split[5])) {
				context.write(join(removeBraces(split[0]), split[1], split[2], split[4], split[5]), ONE);
			}
		}

		protected Text join(String... s) {
			Text text = new Text();
			text.set(StringUtils.join(Arrays.asList(s), "\t"));
			return text;
		}

		protected boolean isDurationCorrect(String durationString) {
			int duration = Integer.parseInt(durationString);
			return duration >= 30 && duration <= 1200;
		}

		protected String removeBraces(String string) {
			return string.substring(1, string.length() - 1);
		}
	}

	public static class EtlSolutionReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		IntWritable intWritable = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException,
				InterruptedException {
			context.write(key, intWritable);
		}
	}

	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			System.err.println("Usage: <input> <output>");
			System.exit(2);
		}

		Configuration conf = getConf();
		Job job = Job.getInstance(conf);
		job.setJobName("Etl Solution Map reduce");
		job.setJarByClass(EtlSolution.class);

		job.setMapperClass(EtlSolutionMapper.class);
		job.setReducerClass(EtlSolutionReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		int result = job.waitForCompletion(true) ? 0 : 1;
		return result;

	}

	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(), new EtlSolution(), args);
	}
}
