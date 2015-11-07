package com.getindata.tutorial.bigdatatutorial.kafka;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import com.getindata.tutorial.bigdatatutorial.utils.LogEventTsvGenerator;

public class LogEventTsvProducer {
	
	private static int NUM_EVENTS = 10000;
	
	private static Properties configureProperties(String bootstrapServers) {
		
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class.getName());
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class.getName());
		
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		
		return props;
		
	}
	
	private static Callback getCallback() {
		Callback callback = new Callback() {
            public void onCompletion(RecordMetadata metadata, Exception e) {
                if(e != null)
                    e.printStackTrace();
                	System.out.println("The offset of the record we just sent is: " + metadata.offset());
            };
       };
       return callback;
	}
	
	public static void main(String[] args) throws InterruptedException,
			ExecutionException {

		if (args.length < 3) {
			System.err
					.println("Usage: LogEventTsvProducer <broker1:port,broker2:port> <topic> <is_sync>");
			System.exit(1);
		}
		
		// set parameters
String bootstrapServers = args[0];
String topic = args[1];
boolean isSync = Boolean.parseBoolean(args[2]);


			 
		Properties props = configureProperties(bootstrapServers);
		
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
		
		LogEventTsvGenerator generator = new LogEventTsvGenerator();
		
		for (int i = 0; i < NUM_EVENTS; ++i) {
			
			String key = generator.getKey();
			String logEvent = generator.next();

			ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(
					topic, key, logEvent);
			
			Future<RecordMetadata> future = producer.send(producerRecord, getCallback());
			if (isSync) {
				future.get();
			}

			if (i % 200 == 0) {
				System.out.println("Produced events: " + i);
			}
		}
		producer.close();

	}
}