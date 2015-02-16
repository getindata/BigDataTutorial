package com.getindata.tutorial.bigdatatutorial.kafka;

import java.util.*;

import com.getindata.tutorial.bigdatatutorial.avro.LogEvent;
import com.getindata.tutorial.bigdatatutorial.utils.LogEventAvroGenerator;
import com.getindata.tutorial.bigdatatutorial.utils.LogEventGenerator;
import com.linkedin.camus.etl.kafka.coders.KafkaAvroMessageEncoder;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class LogEventAvroProducer {
	public static void main(String[] args) {

		Properties props = new Properties();
		
		props.put("metadata.broker.list", args[0]);
		props.put("zk.connect", args[1]);
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("producer.type", "sync");
		props.put("request.required.acks", "1");
		props.put("topic", args[2]);

	

		ProducerConfig config = new ProducerConfig(props);
		Producer<String, String> producer = new Producer<String, String>(config);

		LogEventAvroGenerator generator = new LogEventAvroGenerator();
		
        KafkaAvroMessageEncoder enc = new KafkaAvroMessageEncoder(props.getProperty("topic"), null);

    
        enc.init(props, props.getProperty("topic"));

    	for (int i = 0; i < 100000; i++) {
			
			LogEvent logEvent = generator.next();
			String server = generator.getKey();

			 KeyedMessage<String, byte[]> data 
		        = new KeyedMessage<String, byte[]>(props.getProperty("topic"), enc.toBytes(logEvent));

			producer.send(data);
		}

		producer.close();
	}
}
