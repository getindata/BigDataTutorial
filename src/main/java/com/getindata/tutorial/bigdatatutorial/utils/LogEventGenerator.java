package com.getindata.tutorial.bigdatatutorial.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public abstract class LogEventGenerator<L> {

	protected final static String[] servers = { "ny.stream.rock.net",
			"wa.stream.rock.net", "bos.stream.rock.net", "phi.stream.rock.net" };

	protected final static String[] events = { "SongPlayed"};

	protected Random random = new Random();
	protected String lastServer;
	protected L logEvent;
	
	protected abstract void handleNext();

	public L next() {
		handleNext();
		return logEvent;
	}

	protected String getCurrentTimestamp(String format) {
		return new SimpleDateFormat(format).format(new Date());

	}

	protected String pickServer() {
		return servers[random.nextInt(servers.length)];
	}
	
	protected String pickEvent() {
		return events[random.nextInt(events.length)];
	}

	public String getKey() {
		return lastServer;
	}

	protected int getRandomUserId() {
		return random.nextInt(10000);
	}

	protected int getRandomSongId() {
		return random.nextInt(1000);
	}

	protected int getSongDuration() {
		int duration = random.nextInt(420)
				+ (random.nextInt(20) == 0 ? 10000 : 0);
		return duration;
	}

}