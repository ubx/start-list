package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.ubx.startlist.shared.FlightEntry;
import ch.ubx.startlist.shared.SendExcel;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class ExcelSenderTest {

	//private static final Logger log = Logger.getLogger(ExcelSenderTest.class.getName());

	private final static LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
	private final static LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);
	private final static ErrorOutputTestHelper errOutHelper = new ErrorOutputTestHelper();

	private static FlightEntryDAO flightEntryDAO;
	private static SentFlightEntryDAO sentFlightEntryDAO;
	private static SendExcelDAO sendExcelDAO;

	@Before
	public void setUp() throws Exception {
		errOutHelper.setUp();
		helper.setUp();
		flightEntryDAO = new FlightEntryDAOobjectify();
		sentFlightEntryDAO = new SentFlightEntryDAOobjectify();
		sendExcelDAO = new SendExcelDAOobjectify();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
		errOutHelper.tearDown();
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
		List<FlightEntry> flightEntries = new ArrayList<FlightEntry>();
		for (long l = 0; l < 100; l++) {
			long millies = now.getTimeInMillis() - (l * oneDayMillies);
			FlightEntry fe = new FlightEntry("pilot1", millies, millies + 4711, false, "blablaX", PLACE);
			fe.setModified(now.getTimeInMillis());
			flightEntries.add(fe);
			millies = millies - twoHours;
			fe = new FlightEntry("pilot2", millies, millies + 4711, false, "blablaY", PLACE);
			fe.setModified(now.getTimeInMillis());
			flightEntries.add(fe);
		}
		flightEntryDAO.addFlightEntries4Test(flightEntries);
		assertEquals(200, flightEntryDAO.listflightEntry(now.get(Calendar.YEAR) - 1, now.get(Calendar.YEAR), PLACE).size());

		SendExcel sendExcel = new SendExcel(NAME, "testtesttest", PLACE, "mr.mail@test.com");
		sendExcel.setDaysBehind(daysBehind);
		sendExcelDAO.createOrUpdateSendExcel(sendExcel);
		assertEquals(1, sendExcelDAO.listAllSendExcel().size());

		// Sent all pending flights
		List<String> names = new ArrayList<String>();
		names.add(NAME);

		// TODO -- alternatively be we could parse console output:
		// ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		// System.setOut(new PrintStream(outContent));
		// assertEquals("xxxx", outContent.toString());
		// see: http://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
		assertEquals("No SentFlightEnties should be pending", 0, sentFlightEntryDAO.listFlightEntry(NAME).size());
		try {
			filteredFlightEntriesCnt = ExcelSender.doSend(names, now);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals("Flights should be sent", 26, filteredFlightEntriesCnt);

		for (int i = 0; i < 5; i++) {
			try {
				filteredFlightEntriesCnt = ExcelSender.doSend(names, now);
			} catch (IOException e) {
				e.printStackTrace();
				fail();
			}
			assertEquals("Amount of SentFlightEnties should not change", 26, sentFlightEntryDAO.listFlightEntry(NAME).size());
			assertEquals("No flights should be sent", 0, filteredFlightEntriesCnt);
		}

		// Check if old SentFlightEnties are purged.
		for (int i = 0; i < 100; i++) {
			try {
				now.setTimeInMillis(now.getTimeInMillis() + oneDayMillies);
				filteredFlightEntriesCnt = ExcelSender.doSend(names, now);
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
		List<FlightEntry> flightEntries = new ArrayList<FlightEntry>();
		long millies = now.getTimeInMillis();
		for (long l = 0; l < 50; l++) {
			FlightEntry fe = new FlightEntry("pilot12", millies, millies + 2000, false, "blablaX2", PLACE);
			fe.setModified(millies);
			flightEntries.add(fe);
			fe = new FlightEntry("pilot22", millies - sixHours, millies + 3000, false, "blablaY2", PLACE);
			fe.setModified(millies);
			flightEntries.add(fe);
			millies = millies - oneDayMillies;
		}
		flightEntryDAO.addFlightEntries4Test(flightEntries);
		assertEquals(100, flightEntryDAO.listflightEntry(now.get(Calendar.YEAR) - 1, now.get(Calendar.YEAR), PLACE).size());

		SendExcel sendExcel = new SendExcel(NAME, "testtesttest2", PLACE, "mr.mail@test.com");
		sendExcel.setDaysBehind(daysBehind);
		sendExcelDAO.createOrUpdateSendExcel(sendExcel);
		assertEquals(1, sendExcelDAO.listAllSendExcel().size());

		// Sent all pending flights
		List<String> names = new ArrayList<String>();
		names.add(NAME);

		assertEquals("Output buffer must be empty", 0, errOutHelper.getSysErr().length());

		assertEquals("No SentFlightEnties should be pending", 0, sentFlightEntryDAO.listFlightEntry(NAME).size());
		try {
			filteredFlightEntriesCnt = ExcelSender.doSend(names, now);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals("Flights should be sent", 16, filteredFlightEntriesCnt);
		assertTrue(errOutHelper.getSysErr(), errOutHelper.contains("INFO: Create excel for FlighEnties: 16"));

		// Simulate hourly cron job send
		Calendar now2 = Calendar.getInstance();
		now2.setTimeInMillis(TestUtil.parseTimeString("23.02.2011 04:30 utc").getTime());
		for (long i = 0; i < 5; i++) {
			now2.setTimeInMillis(now2.getTimeInMillis() + (i * oneHours));
			try {
				filteredFlightEntriesCnt = ExcelSender.doSend(names, now2);
			} catch (IOException e) {
				e.printStackTrace();
				fail();
			}
			assertEquals(i + "-Amount of SentFlightEnties should not change", 16, sentFlightEntryDAO.listFlightEntry(NAME).size());
			assertEquals("No flights should be sent", 0, filteredFlightEntriesCnt);
			assertTrue(errOutHelper.getSysErr(), errOutHelper.contains("INFO: Create excel for FlighEnties: 0"));
		}

		// Modify already sent flights
		Calendar now3 = Calendar.getInstance();
		now3.setTimeInMillis(TestUtil.parseTimeString("23.02.2011 04:30 utc").getTime());
		for (int i = 2; i < 6; i++) {
			List<FlightEntry> fes = flightEntryDAO.listflightEntry(PLACE, now3.getTimeInMillis() - (i * oneDayMillies), 0, 1);
			FlightEntry fe = fes.get(0);
			assertNotNull("At least one Flight Entry should be here", fe);
			fe.setModified(now3.getTimeInMillis() - (i * oneDayMillies));
			flightEntryDAO.createOrUpdateFlightEntry(fe);
		}

		try {
			filteredFlightEntriesCnt = ExcelSender.doSend(names, now3);
		} catch (IOException e) {
			fail();
		}
		assertEquals("New flights should be sent", 4, filteredFlightEntriesCnt);
		assertEquals("Still same SentFlightEnties", 16, sentFlightEntryDAO.listFlightEntry(NAME).size());
		assertTrue(errOutHelper.getSysErr(), errOutHelper.contains("INFO: Create excel for FlighEnties: 4"));

		// Modify very old flights
		Calendar now4 = Calendar.getInstance();
		now4.setTimeInMillis(TestUtil.parseTimeString("23.02.2011 04:30 utc").getTime());
		for (int i = 30; i < 40; i++) {
			List<FlightEntry> fes = flightEntryDAO.listflightEntry(PLACE, now4.getTimeInMillis() - (i * oneDayMillies), 0, 1);
			FlightEntry fe = fes.get(0);
			assertNotNull("At least one Flight Entry should be here", fe);
			fe.setModified(now4.getTimeInMillis() - (i * oneDayMillies));
			flightEntryDAO.createOrUpdateFlightEntry(fe);
		}

		try {
			filteredFlightEntriesCnt = ExcelSender.doSend(names, now4);
		} catch (IOException e) {
			fail();
		}
		assertEquals("No flights should be sent", 0, filteredFlightEntriesCnt);
		assertEquals("Still same SentFlightEnties", 16, sentFlightEntryDAO.listFlightEntry(NAME).size());
		assertTrue(errOutHelper.getSysErr(), errOutHelper.contains("INFO: Create excel for FlighEnties: 0"));

		// Advance current time as few month
		Calendar now5 = Calendar.getInstance();
		now5.setTimeInMillis(TestUtil.parseTimeString("03.09.2011 04:30 utc").getTime());

		try {
			filteredFlightEntriesCnt = ExcelSender.doSend(names, now5);
		} catch (IOException e) {
			fail();
		}
		assertEquals("No flights should be sent", 0, filteredFlightEntriesCnt);
		assertEquals("No SentFlightEnties", 0, sentFlightEntryDAO.listFlightEntry(NAME).size());
		assertTrue(errOutHelper.getSysErr(), errOutHelper.contains("INFO: Create excel for FlighEnties: 0"));
	}


}
