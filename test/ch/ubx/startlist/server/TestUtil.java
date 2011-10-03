package ch.ubx.startlist.server;

import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TestUtil {

	private static SimpleDateFormat timeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm z");
	static {
		timeFormat.setTimeZone(TimeZone.getTimeZone("utc"));
	}

	public static Date parseTimeString(String timeStr) {
		Date nextTime = null;
		try {
			nextTime = timeFormat.parse(timeStr);
		} catch (ParseException e) {
			fail("Could not parse test time(nextTime)");
		}
		return nextTime;
	}

}
