package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ubx.startlist.shared.FlightEntry;
import ch.ubx.startlist.shared.SendExcel;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class ExcelSenderTest {

	private final static LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
	private final static LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);

	private static FlightEntryDAO flightEntryDAO;
	private static SentFlightEntryDAO sentFlightEntryDAO;
	private static SendExcelDAO sendExcelDAO;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		flightEntryDAO = new FlightEntryDAOobjectify();
		sentFlightEntryDAO = new SentFlightEntryDAOobjectify();
		sendExcelDAO = new SendExcelDAOobjectify();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

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
			flightEntries.add(fe);
			millies = millies - twoHours;
			fe = new FlightEntry("pilot2", millies, millies + 4711, false, "blablaY", PLACE);
			flightEntries.add(fe);
		}
		flightEntryDAO.addFlightEntries(flightEntries);
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
			assertEquals("SentFlightEnties should disappear after 12 days", i < 13 ? 26 : 0, sentFlightEntryDAO.listFlightEntry(NAME).size());
			assertEquals("No flights should be sent", 0, filteredFlightEntriesCnt);

			// System.out.println("filteredFlightEntriesCnt="+filteredFlightEntriesCnt);
			// System.out.println("XXX-" + "i=" + i + "  sentFlightEntryDAO.listFlightEntry(NAME).size()=" + sentFlightEntryDAO.listFlightEntry(NAME).size());

		}

	}

}
