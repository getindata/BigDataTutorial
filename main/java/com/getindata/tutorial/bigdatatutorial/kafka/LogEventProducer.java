package com.getindata.tutorial.bigdatatutorial.kafka;

import java.util.*;

import com.getindata.tutorial.bigdatatutorial.utils.LogEventGenerator;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class LogEventProducer {
	public static void main(String[] args) {

		Properties props = new Properties();
		
		props.put("metadata.broker.list", args[0]);
		props.put("zk.connect", args[1]);
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("producer.type", "sync");
		props.put("request.required.acks", "1");

		String topic = args[2];

		ProducerConfig config = new ProducerConfig(props);
		Producer<String, String> producer = new Producer<String, String>(config);

		LogEventGenerator generator = new LogEventGenerator();

		for (int i = 0; i < 100000; i++) {
			
			String logEvent = generator.next();
			String server = generator.getKey();

			KeyedMessage<String, String> data = new KeyedMessage<String, String>(
					topic, server, logEvent);
			
			producer.send(data);
		}

		producer.close();
	}
}
