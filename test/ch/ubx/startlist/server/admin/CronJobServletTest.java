package ch.ubx.startlist.server.admin;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ubx.startlist.shared.PeriodicJob;

public class CronJobServletTest {

	private CronJobServlet cronJobServlet;
	private PeriodicJob periodicJob;
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm z");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		cronJobServlet = new CronJobServlet();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoGetHttpServletRequestHttpServletResponse() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() {
		fail("Not yet implemented");
	}

	@Test
	public void testAdjustTimeInMillisWrongDays() {
		PeriodicJob periodicJob = new PeriodicJob();
		try {
			periodicJob.setDays(new boolean[] { false, true, false, false, false, false, false, false });
		} catch (Exception e) {
			return;
		}
		fail("Exception excepted");
	}

	@Test
	public void testAdjustTimeInMillisCorrectDays() {
		PeriodicJob periodicJob = new PeriodicJob();
		try {
			periodicJob.setDays(new boolean[] { true, false, false, false, false, false, false });
		} catch (Exception e) {
			fail("No Exception excepted");
		}
	}

	@Test
	public void testAdjustTimeInMillisNoDays() {
		boolean[] days = new boolean[] { false, false, false, false, false, false, false };
		String timeStr = "17:00";
		String nowStr = "19.01.2011 17:05 utc"; // MI
		String expectedStr = "01.01.1970 00:00 utc";
		testAdjustTime(true, days, nowStr, timeStr, expectedStr);
	}

	@Test
	public void testAdjustTimeInMillisNextSchedule() {
		boolean[] days = new boolean[] { false, false, false, false, true, false, false };
		String timeStr = "17:00";
		String nowStr = "19.01.2011 17:05 utc"; // MI
		String expectedStr = "20.01.2011 17:00 utc"; // DO
		testAdjustTime(true, days, nowStr, timeStr, expectedStr);
	}

	@Test
	public void testAdjustTimeInMillisNextSchedule2() {
		boolean[] days = new boolean[] { false, false, false, false, false, true, false };
		String timeStr = "17:00";
		String nowStr = "19.01.2011 17:05 utc"; // MI
		String expectedStr = "21.01.2011 17:00 utc"; // DO
		testAdjustTime(true, days, nowStr, timeStr, expectedStr);
	}

	@Test
	public void testAdjustTimeInMillisNextSchedule3() {
		boolean[] days = new boolean[] { true, false, false, false, false, false, false };
		String timeStr = "17:00";
		String nowStr = "19.01.2011 17:05 utc"; // MI
		String expectedStr = "23.01.2011 17:00 utc"; // SO
		testAdjustTime(true, days, nowStr, timeStr, expectedStr);
	}

	@Test
	public void testAdjustTimeInMillisNextSchedule4() {
		boolean[] days = new boolean[] { false, false, false, true, false, false, false };
		String timeStr = "17:00";
		String nowStr = "19.01.2011 17:05 utc"; // MI
		String expectedStr = "19.01.2011 17:00 utc"; // MI
		testAdjustTime(true, days, nowStr, timeStr, expectedStr);
	}

	@Test
	public void testAdjustTimeInMillisNextSchedule5() {
		boolean[] days = new boolean[] { false, false, true, false, false, false, false };
		String timeStr = "17:00";
		String nowStr = "19.01.2011 17:05 utc"; // MI
		String expectedStr = "25.01.2011 17:00 utc"; // DI
		testAdjustTime(true, days, nowStr, timeStr, expectedStr);
	}

	@Test
	public void testAdjustTimeInMillisNextSchedule6() {
		boolean[] days = new boolean[] { true, true, true, true, true, true, true };
		String timeStr = "17:00";
		String nowStr = "19.01.2011 17:05 utc"; // MI
		String expectedStr = "19.01.2011 17:00 utc"; // MI
		testAdjustTime(true, days, nowStr, timeStr, expectedStr);

		testAdjustTime(false, null, "20.01.2011 17:05 utc", null, "21.01.2011 17:00 utc"); // DO
		testAdjustTime(false, null, "21.01.2011 17:05 utc", null, "22.01.2011 17:00 utc"); // FR
		testAdjustTime(false, null, "22.01.2011 17:05 utc", null, "23.01.2011 17:00 utc"); // SA
		testAdjustTime(false, null, "23.01.2011 17:05 utc", null, "24.01.2011 17:00 utc"); // SO
		testAdjustTime(false, null, "24.01.2011 17:05 utc", null, "25.01.2011 17:00 utc"); // MO
		testAdjustTime(false, null, "25.01.2011 17:05 utc", null, "26.01.2011 17:00 utc"); // DI
		testAdjustTime(false, null, "26.01.2011 17:05 utc", null, "27.01.2011 17:00 utc"); // MI
		testAdjustTime(false, null, "27.01.2011 17:05 utc", null, "28.01.2011 17:00 utc"); // DO
		testAdjustTime(false, null, "28.01.2011 17:05 utc", null, "29.01.2011 17:00 utc"); // FR
		testAdjustTime(false, null, "29.01.2011 17:05 utc", null, "30.01.2011 17:00 utc"); // SA
		testAdjustTime(false, null, "30.01.2011 17:05 utc", null, "31.01.2011 17:00 utc"); // SO
		testAdjustTime(false, null, "31.01.2011 17:05 utc", null, "01.02.2011 17:00 utc"); // MO
		testAdjustTime(false, null, "01.02.2011 17:05 utc", null, "02.02.2011 17:00 utc"); // DI
		testAdjustTime(false, null, "02.02.2011 17:05 utc", null, "03.02.2011 17:00 utc"); // MI
	}

	@Test
	public void testAdjustTimeInMillisNextSchedule7() {
		boolean[] days = new boolean[] { false, false, true, false, false, false, true };
		String timeStr = "17:00";
		String nowStr = "31.12.2010 17:05 utc"; // FR
		String expectedStr = "01.01.2011 17:00 utc"; // SA
		testAdjustTime(true, days, nowStr, timeStr, expectedStr);
	}

	private void testAdjustTime(boolean init, boolean[] days, String nowStr, String timeStr, String expectedTimeStr) {
		if (init) {
			periodicJob = new PeriodicJob();
			periodicJob.setName("Test");
			periodicJob.setDays(days);
			periodicJob.setTime(timeStr);
			assertEquals(0, periodicJob.getNextTimeInMillis());
		}

		Date now = null;
		try {
			now = timeFormat.parse(nowStr);
		} catch (ParseException e) {
			fail("Could not parse test time(now)");
		}

		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(now.getTime());
		cronJobServlet.adjustTimeInMillis(periodicJob, time);

		Date nextTime = null;
		try {
			nextTime = timeFormat.parse(expectedTimeStr);
		} catch (ParseException e) {
			fail("Could not parse test time(nextTime)");
		}
		assertEquals(nextTime.getTime(), periodicJob.getNextTimeInMillis());
	}
}
