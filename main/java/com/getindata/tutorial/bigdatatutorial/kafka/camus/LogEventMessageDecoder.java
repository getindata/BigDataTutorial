package com.getindata.tutorial.bigdatatutorial.kafka.camus;

import com.linkedin.camus.coders.CamusWrapper;
import com.linkedin.camus.coders.MessageDecoder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;

import java.io.IOException;

public class LogEventMessageDecoder extends MessageDecoder<byte[], GenericData.Record> {

  DecoderFactory factory = DecoderFactory.get();

  @Override
  public CamusWrapper<GenericData.Record> decode(byte[] bytes) {

    DatumReader<GenericData.Record> reader = new GenericDatumReader<GenericData.Record>(LogEvent.SCHEMA$);

    try {
      GenericData.Record record = reader.read(null, factory.binaryDecoder(bytes, null));
      return new CamusWrapper<GenericData.Record>(record);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Exception!", e);
    }
  }
}