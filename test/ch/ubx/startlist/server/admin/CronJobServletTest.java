package ch.ubx.startlist.server.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import ch.ubx.startlist.server.PeriodicJobDAO;
import ch.ubx.startlist.server.PeriodicJobDAOobjectify;
import ch.ubx.startlist.server.TestUtil;
import ch.ubx.startlist.shared.PeriodicJob;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

public class CronJobServletTest {

	private static final String NULL_TIME_STR = "01.01.1970 00:00 utc";
	private final static LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
	private final static LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);
	private static PeriodicJobDAO periodicJobDAO;

	private PeriodicJob periodicJob;
	private CronJobServlet cronJobServlet;

	private static SimpleDateFormat timeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm z");

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		cronJobServlet = new CronJobServlet();
		periodicJobDAO = new PeriodicJobDAOobjectify();
		timeFormat.setTimeZone(TimeZone.getTimeZone("utc"));
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
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
		String expectedStr = NULL_TIME_STR;
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

	@Test
	public void testAdjustTimeInMillisNextScheduleFridaysOnly() {
		boolean[] days = new boolean[] { false, false, false, false, false, true, false };
		String timeStr = "08:00";
		testAdjustTime(true, days, "19.01.2011 10:30 utc", timeStr, "21.01.2011 08:00 utc"); // MI
		testAdjustTime(false, days, "20.01.2011 10:30 utc", timeStr, "21.01.2011 08:00 utc"); // DO
		testAdjustTime(false, days, "21.01.2011 10:30 utc", timeStr, "28.01.2011 08:00 utc"); // FR
		testAdjustTime(false, days, "22.01.2011 10:30 utc", timeStr, "28.01.2011 08:00 utc"); // SA
		testAdjustTime(false, days, "23.01.2011 10:30 utc", timeStr, "28.01.2011 08:00 utc"); // SO
		testAdjustTime(false, days, "24.01.2011 10:30 utc", timeStr, "28.01.2011 08:00 utc"); // MO
		testAdjustTime(false, days, "25.01.2011 10:30 utc", timeStr, "28.01.2011 08:00 utc"); // DI
		testAdjustTime(false, days, "26.01.2011 10:30 utc", timeStr, "28.01.2011 08:00 utc"); // MI
		testAdjustTime(false, days, "27.01.2011 10:30 utc", timeStr, "28.01.2011 08:00 utc"); // DO
		testAdjustTime(false, days, "28.01.2011 10:30 utc", timeStr, "04.02.2011 08:00 utc"); // FR
		testAdjustTime(false, days, "29.01.2011 10:30 utc", timeStr, "04.02.2011 08:00 utc"); // SA
		testAdjustTime(false, days, "30.01.2011 10:30 utc", timeStr, "04.02.2011 08:00 utc"); // SO
		testAdjustTime(false, days, "31.01.2011 10:30 utc", timeStr, "04.02.2011 08:00 utc"); // MO
		testAdjustTime(false, days, "01.02.2011 10:30 utc", timeStr, "04.02.2011 08:00 utc"); // DI
		testAdjustTime(false, days, "02.02.2011 10:30 utc", timeStr, "04.02.2011 08:00 utc"); // MI
	}

	@Test
	public void testDoGetHttpServletRequestHttpServletResponseEveryDay() {
		PeriodicJob periodicJob = new PeriodicJob("Test-Job");
		periodicJob.setTime("09:00");
		periodicJob.setDays(new boolean[] { true, true, true, true, true, true, true });
		periodicJobDAO.createOrUpdatePeriodicJob(periodicJob);

		startCronJob("19.01.2011 09:30 utc"); // MI
		periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
		assertEquals(TestUtil.parseTimeString("20.01.2011 09:00 utc").getTime(), periodicJob.getNextTimeInMillis());

		startCronJob("20.01.2011 09:30 utc"); // DO
		periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
		assertEquals(TestUtil.parseTimeString("21.01.2011 09:00 utc").getTime(), periodicJob.getNextTimeInMillis());

		startCronJob("21.01.2011 09:30 utc"); // FR
		periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
		assertEquals(TestUtil.parseTimeString("22.01.2011 09:00 utc").getTime(), periodicJob.getNextTimeInMillis());
	}

	@Test
	public void testDoGetHttpServletRequestHttpServletResponseFridaysOnly() {
		PeriodicJob periodicJob = new PeriodicJob("Test-Job");
		periodicJob.setTime("08:00");
		periodicJob.setDays(new boolean[] { false, false, false, false, false, true, false });
		periodicJobDAO.createOrUpdatePeriodicJob(periodicJob);

		startCronJob("19.01.2011 10:30 utc"); // MI
		periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
		assertEquals(TestUtil.parseTimeString("21.01.2011 08:00 utc").getTime(), periodicJob.getNextTimeInMillis());

		startCronJob("20.01.2011 10:30 utc"); // DO
		periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
		assertEquals(TestUtil.parseTimeString("21.01.2011 08:00 utc").getTime(), periodicJob.getNextTimeInMillis());

		startCronJob("21.01.2011 10:30 utc"); // FR
		periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
		assertEquals(TestUtil.parseTimeString("28.01.2011 08:00 utc").getTime(), periodicJob.getNextTimeInMillis());
	}

	@Test
	public void testAdjustTimeInMillisNextScheduleSundayOnly() {
		boolean[] days = new boolean[] { true, false, false, false, false, false, false };
		String timeStr = "17:00";
		testAdjustTime(true, days, "23.01.2011 17:05 utc", timeStr, "23.01.2011 17:00 utc"); // SO
		testAdjustTime(false, days, "24.01.2011 17:05 utc", timeStr, "30.01.2011 17:00 utc"); // MO
		testAdjustTime(false, days, "25.01.2011 17:05 utc", timeStr, "30.01.2011 17:00 utc"); // DI
		testAdjustTime(false, days, "26.01.2011 17:05 utc", timeStr, "30.01.2011 17:00 utc"); // MI
		testAdjustTime(false, days, "27.01.2011 17:05 utc", timeStr, "30.01.2011 17:00 utc"); // DO
		testAdjustTime(false, days, "28.01.2011 17:05 utc", timeStr, "30.01.2011 17:00 utc"); // FR
		testAdjustTime(false, days, "29.01.2011 17:05 utc", timeStr, "30.01.2011 17:00 utc"); // SA
		testAdjustTime(false, days, "30.01.2011 17:05 utc", timeStr, "06.02.2011 17:00 utc"); // SO
		testAdjustTime(false, days, "31.01.2011 17:05 utc", timeStr, "06.02.2011 17:00 utc"); // MO
		testAdjustTime(false, days, "01.02.2011 17:05 utc", timeStr, "06.02.2011 17:00 utc"); // DI
	}

	@Test
	public void testDoGetHttpServletRequestHttpServletResponseMultipleCronJobRun() {
		PeriodicJob periodicJob = new PeriodicJob("Test-Job");
		periodicJob.setTime("08:00");
		periodicJob.setDays(new boolean[] { false, false, false, false, false, true, false }); // Fridays only
		periodicJobDAO.createOrUpdatePeriodicJob(periodicJob);
		final String[] runs = new String[] { "19.01.2011 10:30 utc", "19.01.2011 11:30 utc", "19.01.2011 22:30 utc", "20.01.2011 10:30 utc",
				"21.01.2011 07:30 utc" };
		for (String run : runs) {
			startCronJob(run);
			periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
			assertEquals(TestUtil.parseTimeString("21.01.2011 08:00 utc").getTime(), periodicJob.getNextTimeInMillis());
		}
	}

	@Test
	public void testDoGetHttpServletRequestHttpServletResponseYearEnd() {
		boolean[] days = new boolean[] { false, false, false, false, false, true, false }; // FR
		String timeStr = "20:00";
		testAdjustTime(true, days, "29.12.2010 22:30 utc", timeStr, "31.12.2010 20:00 utc"); // MI
		testAdjustTime(false, days, "30.12.2010 22:30 utc", timeStr, "31.12.2010 20:00 utc"); // DO
		testAdjustTime(false, days, "31.12.2010 22:30 utc", timeStr, "07.01.2011 20:00 utc"); // FR
		testAdjustTime(false, days, "01.01.2011 22:30 utc", timeStr, "07.01.2011 20:00 utc"); // SA
		testAdjustTime(false, days, "02.01.2011 22:30 utc", timeStr, "07.01.2011 20:00 utc"); // SO
	}

	@Test
	public void testDoGetHttpServletRequestHttpServletEnablePeriodicJob() {
		PeriodicJob periodicJob = new PeriodicJob("Test-Job");
		periodicJob.setTime("09:00");
		periodicJob.setDays(new boolean[] { true, true, true, true, true, true, true });
		periodicJob.setEnabled(false);
		periodicJobDAO.createOrUpdatePeriodicJob(periodicJob);

		startCronJob("19.01.2011 09:30 utc"); // MI
		periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
		assertEquals(TestUtil.parseTimeString(NULL_TIME_STR).getTime(), periodicJob.getNextTimeInMillis());

		periodicJob.setEnabled(true);
		periodicJobDAO.createOrUpdatePeriodicJob(periodicJob);
		startCronJob("20.01.2011 09:30 utc"); // DO
		periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
		assertEquals(TestUtil.parseTimeString("21.01.2011 09:00 utc").getTime(), periodicJob.getNextTimeInMillis());

		periodicJob.setEnabled(false);
		periodicJobDAO.createOrUpdatePeriodicJob(periodicJob);
		startCronJob("21.01.2011 09:30 utc"); // FR
		periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
		assertEquals(TestUtil.parseTimeString(NULL_TIME_STR).getTime(), periodicJob.getNextTimeInMillis());
	}

	@Test
	public void testDoGetHttpServletRequestHttpServletResponseAllDayCombination() {

		String[] dateSerie = generateDateSerie("23.01.2011", 100); // start on SO

		for (int i = 0; i < Math.pow(2, 7) - 1; i++) {
			PeriodicJob periodicJob = new PeriodicJob("Test-Job");
			periodicJob.setTime("13:00");
			final boolean[] days = booleanArrayFromByte(i);
			periodicJob.setDays(days);
			periodicJob.setNextTimeInMillis(0);
			periodicJobDAO.createOrUpdatePeriodicJob(periodicJob);
			String[] expectedDates = generateExpected(dateSerie, days);

			for (int serieIdx = 0; serieIdx < dateSerie.length - 7; serieIdx++) {
				startCronJob(dateSerie[serieIdx] + " 15:00 utc");
				periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
				String exp = expectedDates[serieIdx] == null ? NULL_TIME_STR : expectedDates[serieIdx] + " 13:00 utc";
				// System.out.println("DEBUG NextTime=" + timeFormat.format(new Date(periodicJob.getNextTimeInMillis())));
				// System.out.println("DEBUG Expected=" + exp);
				assertEquals(TestUtil.parseTimeString(exp).getTime(), periodicJob.getNextTimeInMillis());
			}
		}
	}

	@Test
	public void testDoGetHttpServletRequestHttpServletResponse_Issue44() {

		// Cron job scheduling does not work is expended if time is 23:59

		PeriodicJob periodicJob = new PeriodicJob("Test-Job");
		periodicJob.setTime("23:59");
		periodicJob.setDays(new boolean[] { true, true, true, true, true, true, true });
		periodicJobDAO.createOrUpdatePeriodicJob(periodicJob);

		startCronJob("19.01.2011 24:00 utc");
		periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
		assertEquals("is:" + timeFormat.format(new Date(periodicJob.getNextTimeInMillis())), TestUtil.parseTimeString("20.01.2011 23:59 utc").getTime(),
				periodicJob.getNextTimeInMillis());

		startCronJob("20.01.2011 01:00 utc");
		periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
		assertEquals("is:" + timeFormat.format(new Date(periodicJob.getNextTimeInMillis())), TestUtil.parseTimeString("20.01.2011 23:59 utc").getTime(),
				periodicJob.getNextTimeInMillis());

		startCronJob("20.01.2011 24:00 utc");
		periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
		assertEquals("is:" + timeFormat.format(new Date(periodicJob.getNextTimeInMillis())), TestUtil.parseTimeString("21.01.2011 23:59 utc").getTime(),
				periodicJob.getNextTimeInMillis());

		startCronJob("21.01.2011 24:00 utc");
		periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
		assertEquals("is:" + timeFormat.format(new Date(periodicJob.getNextTimeInMillis())), TestUtil.parseTimeString("22.01.2011 23:59 utc").getTime(),
				periodicJob.getNextTimeInMillis());

		startCronJob("22.01.2011 24:00 utc");
		periodicJob = periodicJobDAO.getPeriodicJob(periodicJob.getName());
		assertEquals("is:" + timeFormat.format(new Date(periodicJob.getNextTimeInMillis())), TestUtil.parseTimeString("23.01.2011 23:59 utc").getTime(),
				periodicJob.getNextTimeInMillis());
	}

	private void startCronJob(String nowStr) {
		ServletRunner sr = new ServletRunner();
		sr.registerServlet("/cronjobs", "ch.ubx.startlist.server.admin.CronJobServlet");
		ServletUnitClient client = sr.newClient();
		try {
			client.sendRequest(new PostMethodWebRequest("http://localhost:8888/cronjobs?now=" + nowStr));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void testAdjustTime(boolean init, boolean[] days, String nowStr, String timeStr, String expectedTimeStr) {
		if (init) {
			periodicJob = new PeriodicJob();
			periodicJob.setName("Test");
			periodicJob.setDays(days);
			periodicJob.setTime(timeStr);
			periodicJob.setEnabled(true);
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
		// System.out.println("DEBUG ExpeTime=" + expectedTimeStr);
		// System.out.println("DEBUG NextTime=" + timeFormat.format(new Date(periodicJob.getNextTimeInMillis())));
		assertEquals(TestUtil.parseTimeString(expectedTimeStr).getTime(), periodicJob.getNextTimeInMillis());
	}

	private static String[] generateDateSerie(String startTimeStr, int weeks) {
		String[] timeStrs = new String[weeks * 7];
		Date nextTime = null;
		try {
			final SimpleDateFormat tf = new SimpleDateFormat("dd.MM.yyyy");
			nextTime = tf.parse(startTimeStr);
			for (int i = 0; i < timeStrs.length; i++) {
				timeStrs[i] = tf.format(nextTime);
				nextTime.setTime(nextTime.getTime() + 24 * 60 * 60 * 1000); // plus one day
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeStrs;
	}

	private static String[] generateExpected(String[] timeStrs, boolean[] days) {
		String[] retTimeStrs = new String[timeStrs.length];
		String last = null;
		for (int i = retTimeStrs.length - 1; i > 0; --i) {
			if (days[i % 7]) {
				retTimeStrs[i - 1] = timeStrs[i];
				last = timeStrs[i];
			} else {
				retTimeStrs[i - 1] = last;
			}
		}
		return retTimeStrs;
	}

	private static boolean[] booleanArrayFromByte(int x) {
		boolean bs[] = new boolean[7];
		bs[0] = ((x & 0x01) != 0);
		bs[1] = ((x & 0x02) != 0);
		bs[2] = ((x & 0x04) != 0);
		bs[3] = ((x & 0x08) != 0);
		bs[4] = ((x & 0x10) != 0);
		bs[5] = ((x & 0x20) != 0);
		bs[6] = ((x & 0x40) != 0);
		return bs;
	}

}
