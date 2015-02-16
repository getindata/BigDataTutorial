package com.getindata.tutorial.bigdatatutorial.utils;

import java.text.SimpleDateFormat;
import java.util.*;

import com.google.common.base.Joiner;

public class LogEventGenerator {

	private final static String SEP = "\t";

	private final static String[] servers = { "ny.stream.rock.net",
			"wa.stream.rock.net", "bos.stream.rock.net", "phi.stream.rock.net" };

	private Random random = new Random();
	private String lastServer;
	private String userId;

	public String next() {

		String event = (random.nextInt(20) == 0 ? nextFollow() : nextStream());
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
				.format(new Date());

		lastServer = pickServer();
		userId = getRandomUserId();

		return Joiner.on(SEP).join(
				Arrays.asList(lastServer, timeStamp, userId, event));
	}

	private String nextStream() {
		return Joiner.on(SEP).join(
				Arrays.asList("endsong", getRandomSongId(), getSongDuration()));
	}

	private String nextFollow() {
		return Joiner.on(SEP).join(Arrays.asList("follow", getRandomUserId()));
	}

	private String pickServer() {
		return servers[random.nextInt(servers.length)];
	}

	public String getKey() {
		return lastServer;
	}

	private String getRandomUserId() {
		return Integer.toString(random.nextInt(1000));
	}

	private String getRandomSongId() {
		return Integer.toString(random.nextInt(1000));
	}

	private String getSongDuration() {
		int duration = random.nextInt(420)
				+ (random.nextInt(20) == 0 ? 10000 : 0);
		return Integer.toString(duration);
	}
}