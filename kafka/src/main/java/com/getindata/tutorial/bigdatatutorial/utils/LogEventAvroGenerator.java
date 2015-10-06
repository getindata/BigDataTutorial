package com.getindata.tutorial.bigdatatutorial.utils;

import com.getindata.tutorial.bigdatatutorial.avro.LogEvent;

public class LogEventAvroGenerator extends LogEventGenerator<LogEvent> {

	protected void handleNext() {
		if ((logEvent == null) || (random.nextInt(100) != 0)) {

			LogEvent.Builder logEventBuilder = LogEvent.newBuilder()
					.setDuration(getSongDuration()).setServer(pickServer())
					.setSongid(getRandomSongId()).setUserid(getRandomUserId())
					.setTimestamp(getCurrentTimestamp("yyyy-MM-dd HH:mm:ss"))
					.setName(pickEvent());

			logEvent = logEventBuilder.build();
		}
	}
}