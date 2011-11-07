package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.ubx.startlist.shared.FeDate;
import ch.ubx.startlist.shared.FeNode;
import ch.ubx.startlist.shared.FePlace;
import ch.ubx.startlist.shared.FeStore;
import ch.ubx.startlist.shared.FeYear;
import ch.ubx.startlist.shared.FeFlightEntry;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class FlightEntryDAOobjectify2Test {

	private static final String REMARKS = "blabla";
	private static final String PILOT = "pilot1";
	private static final LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
	private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);
	private static FlightEntryDAOobjectify2 flightEntryDAOobjectify;

	private static FeGenDAOobjectify<FeDate, Long> dateDAO;
	private static FeGenDAOobjectify<FeStore, String> storeDAO;
	private static FeGenDAOobjectify<FeYear, Long> yearDAO;
	private static FeGenDAOobjectify<FePlace, String> placeDAO;

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		flightEntryDAOobjectify = new FlightEntryDAOobjectify2();
		dateDAO = new FeGenDAOobjectify<FeDate, Long>(FeDate.class);
		storeDAO = new FeGenDAOobjectify<FeStore, String>(FeStore.class);
		yearDAO = new FeGenDAOobjectify<FeYear, Long>(FeYear.class);
		placeDAO = new FeGenDAOobjectify<FePlace, String>(FePlace.class);
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void testListflightEntryFeDateIntInt() {
		final String[] places = new String[10];
		for (int i = 0; i < places.length; i++) {
			places[i] = "Place-" + i;
		}
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(TestUtil.parseTimeString("20.02.2011 19:30 utc").getTime());
		final Long[] dates = new Long[200];
		for (int y = 0; y < 10; y++) {
			int d = 1;
			for (int i = y * 10; i < dates.length; i++) {
				now.setTimeInMillis(TestUtil.parseTimeString((d++) + ".01." + (2000 + y) + " 19:30 utc").getTime());
				dates[i] = now.getTimeInMillis();
			}
		}

		createFlightEntries(dates, places, 1);
		assertEquals(dates.length * places.length, flightEntryDAOobjectify.listflightEntry().size());
		assertEquals(dates.length * 10, dateDAO.list().size());
		assertEquals(100, placeDAO.list().size());
		assertEquals(10, yearDAO.list().size());
		assertEquals(1, storeDAO.list().size());
	}

	@Test
	public void testRemoveFlightEntry() {
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(TestUtil.parseTimeString("20.02.2011 02:30 utc").getTime());
		Calendar now2 = Calendar.getInstance();
		now2.setTimeInMillis(TestUtil.parseTimeString("20.06.2011 02:30 utc").getTime());
		Calendar now3 = Calendar.getInstance();
		now3.setTimeInMillis(TestUtil.parseTimeString("01.06.2011 02:30 utc").getTime());
		final Long[] dates = { now.getTimeInMillis(), now2.getTimeInMillis(), now3.getTimeInMillis() };
		final String[] places = { "P1", "P2", "P3", "P4" };
		final int cnt = 2;
		createFlightEntries(dates, places, cnt);
		assertEquals(dates.length * places.length * cnt, flightEntryDAOobjectify.listflightEntry().size());
		assertEquals(1, flightEntryDAOobjectify.listYear().size());

		FeYear year = flightEntryDAOobjectify.listYear().get(0);
		assertEquals(places.length, flightEntryDAOobjectify.listAirfield(year).size());
		FePlace place = flightEntryDAOobjectify.listAirfield(year).get(0);
		assertEquals(dates.length, flightEntryDAOobjectify.listDate(place).size());

		FeDate date = flightEntryDAOobjectify.listDate(place).get(1);

		List<FeFlightEntry> flightEntries = flightEntryDAOobjectify.listflightEntry(date, 0, 1000);
		assertEquals(cnt, flightEntries.size());
		flightEntryDAOobjectify.removeFlightEntry(flightEntries.get(0));
		flightEntries = flightEntryDAOobjectify.listflightEntry(date, 0, 1000);
		assertEquals(cnt - 1, flightEntries.size());
		assertEquals(dates.length, flightEntryDAOobjectify.listDate(place).size());

		assertEquals(3, flightEntryDAOobjectify.listDate(place).size());
		flightEntryDAOobjectify.removeFlightEntry(flightEntries.get(0));
		assertEquals(dates.length - 1, flightEntryDAOobjectify.listDate(place).size());

		flightEntries = flightEntryDAOobjectify.listflightEntry(date, 0, 1000);
		assertEquals(0, flightEntries.size());
		assertEquals(2, flightEntryDAOobjectify.listDate(place).size());
	}

	@Test
	public void testRemoveAllFlightEntries() {
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(TestUtil.parseTimeString("20.02.2011 02:30 utc").getTime());
		Calendar now2 = Calendar.getInstance();
		now2.setTimeInMillis(TestUtil.parseTimeString("20.06.2011 02:30 utc").getTime());
		Calendar now3 = Calendar.getInstance();
		now3.setTimeInMillis(TestUtil.parseTimeString("01.06.2011 02:30 utc").getTime());
		final Long[] dates = { now.getTimeInMillis(), now2.getTimeInMillis(), now3.getTimeInMillis() };
		final String[] places = { "P1", "P2", "P3", "P4" };
		final int cnt = 2;
		createFlightEntries(dates, places, cnt);
		assertEquals(dates.length * places.length * cnt, flightEntryDAOobjectify.listflightEntry().size());
		assertEquals(1, flightEntryDAOobjectify.listYear().size());

		List<FeFlightEntry> flightEntries = flightEntryDAOobjectify.listflightEntry();
		assertEquals(24, flightEntries.size());

		for (FeFlightEntry flightEntry : flightEntries) {
			flightEntryDAOobjectify.removeFlightEntry(flightEntry);
		}
		flightEntries = flightEntryDAOobjectify.listflightEntry();
		assertEquals(0, flightEntries.size());
		assertEquals(0, flightEntryDAOobjectify.listYear().size());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		IFeGenDAO<?, ?> gDAO = new FeGenDAOobjectify(FeNode.class);
		assertEquals("Only storeActiveKey left", 1, gDAO.list().size());

	}

	private void createFlightEntries(Long[] dates, String[] places, int cnt) {
		List<FeFlightEntry> flightEntries = new ArrayList<FeFlightEntry>();
		for (int i = 0; i < dates.length; i++) {
			for (int j = 0; j < places.length; j++) {
				for (int k = 0; k < cnt; k++) {
					FeFlightEntry fe = new FeFlightEntry(PILOT, dates[i] + k, dates[i] + k + 3600000, false, REMARKS, places[j]);
					flightEntries.add(fe);
				}
			}
		}
		flightEntryDAOobjectify.addFlightEntries4Test(flightEntries);
	}

}
