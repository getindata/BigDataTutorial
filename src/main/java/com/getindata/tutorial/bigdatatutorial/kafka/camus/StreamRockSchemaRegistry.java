package com.getindata.tutorial.bigdatatutorial.kafka.camus;

import com.linkedin.camus.schemaregistry.MemorySchemaRegistry;
import com.getindata.tutorial.bigdatatutorial.avro.LogEvent;

import org.apache.avro.Schema;


public class StreamRockSchemaRegistry extends MemorySchemaRegistry<Schema> {
	public StreamRockSchemaRegistry() {
		super();
		super.register("logevent", LogEvent.SCHEMA$);
	}
}
