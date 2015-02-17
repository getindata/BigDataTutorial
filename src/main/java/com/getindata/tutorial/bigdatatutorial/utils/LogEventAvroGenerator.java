package com.getindata.tutorial.bigdatatutorial.utils;

import java.text.SimpleDateFormat;
import java.util.*;

import com.getindata.tutorial.bigdatatutorial.avro.LogEvent;
import com.google.common.base.Joiner;

public class LogEventAvroGenerator {

	private final static String[] servers = { "ny.stream.rock.net",
			"wa.stream.rock.net", "bos.stream.rock.net", "phi.stream.rock.net" };

	private final static String[] events = { "stream"};

	private Random random = new Random();
	private String lastServer;
	private LogEvent logEvent;

	public LogEvent next() {
		handleNext();
		return logEvent;
	}

	private void handleNext() {
		if ((logEvent == null) || (random.nextInt(100) != 0)) {

			LogEvent.Builder logEventBuilder = LogEvent.newBuilder()
					.setDuration(getSongDuration()).setServer(pickServer())
					.setSongid(getRandomSongId()).setUserid(getRandomUserId())
					.setTimestamp(getCurrentTimestamp("yyyy-MM-dd HH:mm:ss"))
					.setName(pickEvent());

			logEvent = logEventBuilder.build();
		}
	}

	private String getCurrentTimestamp(String format) {
		return new SimpleDateFormat(format).format(new Date());

	}

	private String pickServer() {
		return servers[random.nextInt(servers.length)];
	}
	
	private String pickEvent() {
		return events[random.nextInt(events.length)];
	}

	public String getKey() {
		return lastServer;
	}

	private int getRandomUserId() {
		return random.nextInt(1000);
	}

	private int getRandomSongId() {
		return random.nextInt(1000);
	}

	private int getSongDuration() {
		int duration = random.nextInt(420)
				+ (random.nextInt(20) == 0 ? 10000 : 0);
		return duration;
	}

}