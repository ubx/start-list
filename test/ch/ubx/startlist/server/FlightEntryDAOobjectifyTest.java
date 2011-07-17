package ch.ubx.startlist.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.ubx.startlist.shared.FlightEntry;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class FlightEntryDAOobjectifyTest {

	private static final String REMARKS = "blabla";
	private static final String PLACE = "placeName";
	private static final String PILOT = "pilot1";
	private static final LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
	private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);
	private static FlightEntryDAOobjectify flightEntryDAOobjectify;

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		flightEntryDAOobjectify = new FlightEntryDAOobjectify();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void testListflightEntryStringLongIntInt() {
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(TestUtil.parseTimeString("20.02.2011 19:30 utc").getTime());
		createFlightEntries(now, 20000);
		assertEquals(20000, flightEntryDAOobjectify.listflightEntry(now.get(Calendar.YEAR) - 100, now.get(Calendar.YEAR), PLACE).size());
	}

	private void createFlightEntries(Calendar now, int size) {
		final long oneDayMillies = 24 * 60 * 60 * 1000;
		// NOTE: period max. one year!
		List<FlightEntry> flightEntries = new ArrayList<FlightEntry>(size);
		for (long l = 0; l < size; l++) {
			long millies = now.getTimeInMillis() - (l * oneDayMillies);
			FlightEntry fe = new FlightEntry(PILOT, millies, millies + 1, false, REMARKS, PLACE);
			fe.setModified(now.getTimeInMillis());
			flightEntries.add(fe);
		}
		flightEntryDAOobjectify.addFlightEntries4Test(flightEntries);
	}

}
