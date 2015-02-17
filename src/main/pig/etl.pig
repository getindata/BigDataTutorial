REGISTER ${LIBJARS_PATH}/avro-1.7.4.jar
REGISTER ${LIBJARS_PATH}/piggybank.jar

events = LOAD '$input' USING AvroStorage ();

streams = FILTER events BY name == 'stream';

streams_projected = FOREACH streams GENERATE server, timestamp, userid, songid, duration;

streams_correct = FILTER streams_projected BY 30 <= duration AND duration <= 1200;

streams_unique = DISTINCT streams_correct;

STORE streams_unique INTO '$output' USING PigStorage('\t');
