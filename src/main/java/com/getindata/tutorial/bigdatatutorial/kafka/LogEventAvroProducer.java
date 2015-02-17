package com.getindata.tutorial.bigdatatutorial.kafka;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

import com.getindata.tutorial.bigdatatutorial.avro.LogEvent;
import com.getindata.tutorial.bigdatatutorial.utils.LogEventAvroGenerator;

public class LogEventAvroProducer {
	public static void main(String[] args) {

		Properties props = new Properties();

		props.put("metadata.broker.list", args[0]);
		props.put("zk.connect", args[1]);
		props.put("serializer.class", "kafka.serializer.DefaultEncoder");
		props.put("producer.type", "sync");
		props.put("request.required.acks", "1");
		props.put("topic", args[2]);

		ProducerConfig config = new ProducerConfig(props);
		Producer<String, byte[]> producer = new Producer<String, byte[]>(config);

		LogEventAvroGenerator generator = new LogEventAvroGenerator();

		for (int i = 0; i < 100000; i++) {

			LogEvent logEvent = generator.next();

			KeyedMessage<String, byte[]> data = new KeyedMessage<String, byte[]>(
					props.getProperty("topic"), generator.getKey(), toBytes(logEvent));

			producer.send(data);
		}

		producer.close();
	}

	private static final EncoderFactory encoderFactory = EncoderFactory.get();

	public static byte[] toBytes(LogEvent logEvent) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		BinaryEncoder encoder = encoderFactory.directBinaryEncoder(
				outputStream, null);

		DatumWriter<LogEvent> userDatumWriter = new SpecificDatumWriter<LogEvent>(
				LogEvent.class);
		try {
			userDatumWriter.write(logEvent, encoder);
			encoder.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return outputStream.toByteArray();
	}
}
