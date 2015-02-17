package com.getindata.tutorial.bigdatatutorial.utils;

public class LogEventTsvGenerator extends LogEventGenerator<String> {

	protected void handleNext() {
		if ((logEvent == null) || (random.nextInt(100) != 0)) {

			logEvent = pickServer() + "\t"
					+ getCurrentTimestamp("yyyy-MM-dd HH:mm:ss") + "\t"
					+ getRandomUserId() + "\t" + pickEvent() + "\t"
					+ getRandomSongId() + "\t" + getSongDuration();

		}
	}
}