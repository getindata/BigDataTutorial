package com.getindata.tutorial.bigdatatutorial.kafka.camus;

import com.linkedin.camus.schemaregistry.AvroMemorySchemaRegistry;

public class StreamRockSchemaRegistry extends AvroMemorySchemaRegistry {
  public StockSchemaRegistry() {
    super();

    super.register("logevent", LogEvent.SCHEMA$);
  }
}