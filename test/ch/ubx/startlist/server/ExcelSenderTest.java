package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.ubx.startlist.shared.FeFlightEntry;
import ch.ubx.startlist.shared.Job;
import ch.ubx.startlist.shared.JobSendExcel;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class ExcelSenderTest {

	private final static LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
	private final static LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);
	private final static ErrorOutputTestHelper errOutHelper = new ErrorOutputTestHelper();

	private static FlightEntryDAO2 flightEntryDAO;
	private static SentFlightEntryDAO sentFlightEntryDAO;
	private static JobDAO jobDAO = new JobDAOobjectify();

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		flightEntryDAO = new FlightEntryDAOobjectify2(true); // Note: create an instance for test
		sentFlightEntryDAO = new SentFlightEntryDAOobjectify();
		errOutHelper.setUp();
	}

	@After
	public void tearDown() throws Exception {
		errOutHelper.tearDown();
		helper.tearDown();
	}

	/**
	 * Test case:<br>
	 * - add 200 flights today, all with the modification date now. <br>
	 * - test if flights are send only once.<br>
	 * - test if SentFlightEntries disappear after 12 days.
	 */
	@Test
	public void testDoSend() {
		final String PLACE = "placeName";
		final String NAME = "sendExcelName";
		final int daysBehind = 12;
		final long oneDayMillies = 24 * 60 * 60 * 1000;
		final long twoHours = 2 * 60 * 60 * 1000;
		int filteredFlightEntriesCnt = 0;
		// NOTE: period max. one year!
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(TestUtil.parseTimeString("20.02.2011 19:30 utc").getTime());
		List<FeFlightEntry> flightEntries = new ArrayList<FeFlightEntry>();
		for (long l = 0; l < 100; l++) {
			long millies = now.getTimeInMillis() - (l * oneDayMillies);
			FeFlightEntry fe = new FeFlightEntry("pilot1", millies, millies + 4711, false, "blablaX", PLACE);
			fe.setModified(now.getTimeInMillis());
			flightEntries.add(fe);
			millies = millies - twoHours;
			fe = new FeFlightEntry("pilot2", millies, millies + 4711, false, "blablaY", PLACE);
			fe.setModified(now.getTimeInMillis());
			flightEntries.add(fe);
		}
		flightEntryDAO.addFlightEntries4Test(flightEntries);
		assertEquals(200, flightEntryDAO.listflightEntry(now.get(Calendar.YEAR) - 1, now.get(Calendar.YEAR), PLACE).size());

		JobSendExcel sendExcel = new JobSendExcel(NAME, "testtesttest", PLACE, "mr.mail@test.com");
		sendExcel.setDaysBehind(daysBehind);
		jobDAO.createOrUpdateJob(sendExcel);
		assertEquals(1, jobDAO.listAllJob().size());

		// Sent all pending flights
		List<String> names = new ArrayList<String>();
		names.add(NAME);
		Set<Entry<String, Job>> entrySet = jobDAO.listJob(names).entrySet();

		filteredFlightEntriesCnt = 0;
		try {
			for (Entry<String, Job> entry : entrySet) {
				JobSendExcel job = (JobSendExcel) entry.getValue();
				filteredFlightEntriesCnt = filteredFlightEntriesCnt + ExcelSender.execute(job, now);
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals("Flights should be sent", 26, filteredFlightEntriesCnt);

		filteredFlightEntriesCnt = 0;
		for (int i = 0; i < 5; i++) {
			try {
				for (Entry<String, Job> entry : entrySet) {
					JobSendExcel job = (JobSendExcel) entry.getValue();
					filteredFlightEntriesCnt = filteredFlightEntriesCnt + ExcelSender.execute(job, now);
				}
			} catch (IOException e) {
				e.printStackTrace();
				fail();
			}
			assertEquals("Amount of SentFlightEnties should not change", 26, sentFlightEntryDAO.listFlightEntry(NAME).size());
			assertEquals("No flights should be sent", 0, filteredFlightEntriesCnt);
		}

		// Check if old SentFlightEnties are purged.
		filteredFlightEntriesCnt = 0;
		for (int i = 0; i < 100; i++) {
			try {
				now.setTimeInMillis(now.getTimeInMillis() + oneDayMillies);
				for (Entry<String, Job> entry : entrySet) {
					JobSendExcel job = (JobSendExcel) entry.getValue();
					filteredFlightEntriesCnt = filteredFlightEntriesCnt + ExcelSender.execute(job, now);
				}
			} catch (IOException e) {
				e.printStackTrace();
				fail();
			}
			assertEquals(i + "-SentFlightEnties should disappear after 12 days", i < 12 ? 26 : 0, sentFlightEntryDAO.listFlightEntry(NAME).size());
			assertEquals("No flights should be sent", 0, filteredFlightEntriesCnt);

		}
	}

	/**
	 * Test case: <br>
	 * - add 200 flights spread over the last 100 day (2 per day) <br>
	 * - test if flights are send only once.<br>
	 * - test if modified flight are resent.<br>
	 * - test if old modified flight are not resent.<br>
	 * - test if SentFlightEntries disappear after 12 days.
	 */
	@Test
	public void testDoSend2() {
		final String PLACE = "placeName2";
		final String NAME = "sendExcelName2";
		final int daysBehind = 7;
		final long oneDayMillies = 24 * 60 * 60 * 1000;
		final long sixHours = 6 * 60 * 60 * 1000;
		final long oneHours = 1 * 60 * 60 * 1000;
		int filteredFlightEntriesCnt = 0;
		// NOTE: period max. one year!
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(TestUtil.parseTimeString("23.02.2011 14:30 utc").getTime());
		List<FeFlightEntry> flightEntries = new ArrayList<FeFlightEntry>();
		long millies = now.getTimeInMillis();
		for (long l = 0; l < 50; l++) {
			FeFlightEntry fe = new FeFlightEntry("pilot12", millies, millies + 2000, false, "blablaX2", PLACE);
			fe.setModified(millies);
			flightEntries.add(fe);
			fe = new FeFlightEntry("pilot22", millies - sixHours, millies + 3000, false, "blablaY2", PLACE);
			fe.setModified(millies);
			flightEntries.add(fe);
			millies = millies - oneDayMillies;
		}

		flightEntryDAO.addFlightEntries4Test(flightEntries);
		assertEquals(100, flightEntryDAO.listflightEntry(now.get(Calendar.YEAR) - 1, now.get(Calendar.YEAR), PLACE).size());

		JobSendExcel sendExcel = new JobSendExcel(NAME, "testtesttest2", PLACE, "mr.mail@test.com");
		sendExcel.setDaysBehind(daysBehind);
		jobDAO.createOrUpdateJob(sendExcel);
		assertEquals(1, jobDAO.listAllJob().size());

		// Sent all pending flights
		List<String> names = new ArrayList<String>();
		names.add(NAME);
		Set<Entry<String, Job>> entrySet = jobDAO.listJob(names).entrySet();

		// String syserr = errOutHelper.getSysErr();
		// assertEquals("Output buffer must be empty, got>>" + syserr + "<<", 0, syserr.length());

		assertEquals("No SentFlightEnties should be pending", 0, sentFlightEntryDAO.listFlightEntry(NAME).size());
		filteredFlightEntriesCnt = 0;
		try {
			for (Entry<String, Job> entry : entrySet) {
				JobSendExcel job = (JobSendExcel) entry.getValue();
				filteredFlightEntriesCnt = filteredFlightEntriesCnt + ExcelSender.execute(job, now);
			}
		} catch (IOException e) {
			fail();
		}
		assertEquals("Flights should be sent", 16, filteredFlightEntriesCnt);

		// syserr = errOutHelper.takeSysErr();
		// TODO - does not work: assertTrue("Unexpected output>>" + syserr + "<<",
		// syserr.contains("INFO: Create excel for FlighEnties: 16"));

		// Simulate hourly cron job send
		Calendar now2 = Calendar.getInstance();
		now2.setTimeInMillis(TestUtil.parseTimeString("23.02.2011 04:30 utc").getTime());
		for (long i = 0; i < 5; i++) {
			filteredFlightEntriesCnt = 0;
			now2.setTimeInMillis(now2.getTimeInMillis() + (i * oneHours));
			try {
				for (Entry<String, Job> entry : entrySet) {
					JobSendExcel job = (JobSendExcel) entry.getValue();
					filteredFlightEntriesCnt = filteredFlightEntriesCnt + ExcelSender.execute(job, now2);
				}
			} catch (IOException e) {
				e.printStackTrace();
				fail();
			}
			assertEquals(i + "-Amount of SentFlightEnties should not change", 16, sentFlightEntryDAO.listFlightEntry(NAME).size());
			assertEquals("No flights should be sent", 0, filteredFlightEntriesCnt);

			// syserr = errOutHelper.takeSysErr();
			// TODO - does not work: assertTrue("Unexpected output>>" + syserr + "<<",
			// syserr.contains("INFO: Create excel for FlighEnties: 0"));
		}

		// Modify already sent flights
		Calendar now3 = Calendar.getInstance();
		now3.setTimeInMillis(TestUtil.parseTimeString("23.02.2011 04:30 utc").getTime());
		for (int i = 2; i < 6; i++) {
			long mil = now3.getTimeInMillis() - (i * oneDayMillies);
			Calendar date = Calendar.getInstance();
			date.setTimeInMillis(mil);
			date.set(Calendar.HOUR_OF_DAY, 0);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
			date.set(Calendar.MILLISECOND, 0);
			List<FeFlightEntry> fes = flightEntryDAO.listflightEntry(date.getTimeInMillis(), date.getTimeInMillis(), PLACE);
			assertTrue("At least one Flight Entry should be here", fes.size() > 0);
			FeFlightEntry fe = fes.get(0);
			assertNotNull("At least one Flight Entry should be here", fe);
			fe.setModified(mil);
			flightEntryDAO.createOrUpdateFlightEntry(fe);
		}

		filteredFlightEntriesCnt = 0;
		try {
			for (Entry<String, Job> entry : entrySet) {
				JobSendExcel job = (JobSendExcel) entry.getValue();
				filteredFlightEntriesCnt = filteredFlightEntriesCnt + ExcelSender.execute(job, now3);
			}
		} catch (IOException e) {
			fail();
		}
		assertEquals("New flights should be sent", 4, filteredFlightEntriesCnt);
		assertEquals("Still same SentFlightEnties", 16, sentFlightEntryDAO.listFlightEntry(NAME).size());

		// syserr = errOutHelper.takeSysErr();
		// TODO - does not work: assertTrue("Unexpected output>>" + syserr + "<<",
		// syserr.contains("INFO: Create excel for FlighEnties: 4"));

		// Modify very old flights
		Calendar now4 = Calendar.getInstance();
		now4.setTimeInMillis(TestUtil.parseTimeString("23.02.2011 04:30 utc").getTime());
		for (int i = 30; i < 40; i++) {
			long mil = now4.getTimeInMillis() - (i * oneDayMillies);
			Calendar date = Calendar.getInstance();
			date.setTimeInMillis(mil);
			date.set(Calendar.HOUR_OF_DAY, 0);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
			date.set(Calendar.MILLISECOND, 0);
			List<FeFlightEntry> fes = flightEntryDAO.listflightEntry(date.getTimeInMillis(), date.getTimeInMillis(), PLACE);
			FeFlightEntry fe = fes.get(0);
			assertNotNull("At least one Flight Entry should be here", fe);
			fe.setModified(mil);
			flightEntryDAO.createOrUpdateFlightEntry(fe);
		}

		filteredFlightEntriesCnt = 0;
		try {
			for (Entry<String, Job> entry : entrySet) {
				JobSendExcel job = (JobSendExcel) entry.getValue();
				filteredFlightEntriesCnt = filteredFlightEntriesCnt + ExcelSender.execute(job, now4);
			}
		} catch (IOException e) {
			fail();
		}
		assertEquals("No flights should be sent", 0, filteredFlightEntriesCnt);
		assertEquals("Still same SentFlightEnties", 16, sentFlightEntryDAO.listFlightEntry(NAME).size());

		// syserr = errOutHelper.takeSysErr();
		// TODO - does not work: assertTrue("Unexpected output>>" + syserr + "<<",
		// syserr.contains("INFO: Create excel for FlighEnties: 0"));

		// Advance current time as few month
		Calendar now5 = Calendar.getInstance();
		now5.setTimeInMillis(TestUtil.parseTimeString("03.09.2999 04:30 utc").getTime());

		filteredFlightEntriesCnt = 0;
		try {
			for (Entry<String, Job> entry : entrySet) {
				JobSendExcel job = (JobSendExcel) entry.getValue();
				filteredFlightEntriesCnt = filteredFlightEntriesCnt + ExcelSender.execute(job, now5);
			}
		} catch (IOException e) {
			fail();
		}
		assertEquals("No flights should be sent", 0, filteredFlightEntriesCnt);
		assertEquals("No SentFlightEnties", 0, sentFlightEntryDAO.listFlightEntry(NAME).size());

		// syserr = errOutHelper.takeSysErr();
		// TODO - does not work: assertTrue("Unexpected output>>" + syserr + "<<",
		// syserr.contains("INFO: Create excel for FlighEnties: 0"));
	}

}
