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
		final String PLACE = "place";
		final String NAME = "name";
		final int daysBehind = 12;
		final long oneDayMillies = 24 * 60 * 60 * 1000;
		// NOTE: period max. one year!
		Calendar now = Calendar.getInstance();
		List<FlightEntry> flightEntries = new ArrayList<FlightEntry>();
		for (long l = 0; l < 100; l++) {
			long millies = now.getTimeInMillis() - (l * oneDayMillies);
			FlightEntry fe = new FlightEntry("pilot", millies, millies + 360000, false, "blabla", PLACE);
			flightEntries.add(fe);
			millies = millies + (2 * 360000);
			fe = new FlightEntry("pilot", millies, millies + 360000, false, "blabla", PLACE);
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

		for (int i = 0; i < 5; i++) {
			try {
				ExcelSender.doSend(names, now);
			} catch (IOException e) {
				e.printStackTrace();
				fail();
			}
			assertEquals("Amount of SentFlightEnties should not change", 2 * daysBehind, sentFlightEntryDAO.listFlightEntry(NAME).size());
		}
	}

	@Test
	public void testDoSendModified() {
	}

}
