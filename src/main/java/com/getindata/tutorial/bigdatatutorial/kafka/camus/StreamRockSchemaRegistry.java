package com.getindata.tutorial.bigdatatutorial.kafka.camus;

import com.linkedin.camus.schemaregistry.AvroMemorySchemaRegistry;
import com.getindata.tutorial.bigdatatutorial.avro.LogEvent;

public class StreamRockSchemaRegistry extends AvroMemorySchemaRegistry {
	public StockSchemaRegistry() {
		super();
		super.register("logevent", LogEvent.SCHEMA$);
	}
}
