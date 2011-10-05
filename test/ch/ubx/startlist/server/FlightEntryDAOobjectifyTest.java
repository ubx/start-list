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
		final String[] places = new String[100];
		for (int i = 0; i < places.length; i++) {
			places[i] = "Place-" + i;
		}
		final int cnt = places.length * 1000;
		createFlightEntries(now, cnt, places);
		for (int i = 0; i < places.length; i++) {
			assertEquals(cnt / places.length,
					flightEntryDAOobjectify.listflightEntry(now.get(Calendar.YEAR) - 100, now.get(Calendar.YEAR), places[i]).size());
		}
	}

	@Test
	public void testListflightEntryCalendarString() {
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(TestUtil.parseTimeString("20.02.2011 19:30 utc").getTime());
		final String[] places = new String[100];
		for (int i = 0; i < places.length; i++) {
			places[i] = "Place-" + i;
		}
		final int cnt = places.length * 1000;
		createFlightEntries(now, cnt, places);
		for (int i = 0; i < places.length; i++) {
			assertEquals(cnt / places.length, flightEntryDAOobjectify.listflightEntry(now, places[i]).size());
		}
	}

	@Test
	public void testListflightEntryInt() {
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(TestUtil.parseTimeString("20.02.2011 19:30 utc").getTime());
		final String[] places = new String[100];
		for (int i = 0; i < places.length; i++) {
			places[i] = "Place-" + i;
		}
		final int cnt = places.length * 1000;
		createFlightEntries(now, cnt, places);
		assertEquals(cnt, flightEntryDAOobjectify.listflightEntry(now.get(Calendar.YEAR)).size());

	}

	private void createFlightEntries(Calendar now, int size, String[] places) {
		final long dayMillies = 1;
		// NOTE: period max. one year!
		List<FlightEntry> flightEntries = new ArrayList<FlightEntry>(size);
		for (int l = 0; l < size; l++) {
			long millies = now.getTimeInMillis() - (l * dayMillies);
			FlightEntry fe = new FlightEntry(PILOT, millies, millies + 1, false, REMARKS, places[l % places.length]);
			fe.setModified(now.getTimeInMillis());
			flightEntries.add(fe);
		}
		flightEntryDAOobjectify.addFlightEntries4Test(flightEntries);
	}

}
