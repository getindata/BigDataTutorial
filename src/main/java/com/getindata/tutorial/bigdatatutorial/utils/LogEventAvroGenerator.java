package com.getindata.tutorial.bigdatatutorial.utils;

import java.text.SimpleDateFormat;
import java.util.*;

import com.getindata.tutorial.bigdatatutorial.avro.LogEvent;
import com.google.common.base.Joiner;

public class LogEventAvroGenerator {

	private final static String[] servers = { "ny.stream.rock.net",
			"wa.stream.rock.net", "bos.stream.rock.net", "phi.stream.rock.net" };

	private Random random = new Random();
	private String lastServer;

	public LogEvent next() {

		LogEvent.Builder logEventBuilder = LogEvent.newBuilder()
				.setDuration(getSongDuration()).setServer(pickServer())
				.setSongid(getRandomSongId()).setUserid(getRandomUserId())
				.setTimestamp(getCurrentTimestamp("yyyy.MM.dd.HH.mm.ss"));

		LogEvent logEvent = logEventBuilder.build();

		return logEvent;
	}

	private String getCurrentTimestamp(String format) {
		return new SimpleDateFormat(format).format(new Date());

	}

	private String pickServer() {
		return servers[random.nextInt(servers.length)];
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