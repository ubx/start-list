package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import ch.ubx.startlist.shared.FlightEntry;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class PerformanceMeasureTest {

    private static final String REMARKS = "blabla";
    private static final String PILOT = "pilot1";
    private static final LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
    private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);
    private static final FlightEntryDAOobjectify2 flightEntryDAOobjectify;

    static {
        helper.setUp();
        flightEntryDAOobjectify = new FlightEntryDAOobjectify2();
    }

    private static Calendar[] nows;
    static {
        nows = new Calendar[3];
        int ys = 2007;
        int ds = 1;
        for (int i = 0; i < nows.length; i++) {
            nows[i] = Calendar.getInstance();
            nows[i].setTimeInMillis(TestUtil.parseTimeString(ds++ + ".02." + ys++ + " 08:30 utc").getTime());
        }
    }

    private static String[] places;
    static {
        places = new String[100];
        for (int i = 0; i < places.length; i++) {
            places[i] = "Place-" + i;
        }
    }

    private static final int cnt = 1000 * places.length;

    @Test
    public void testInitTestData() {
    }

    @Test
    public void testCreateData() {
        for (Calendar now : nows) {
            createFlightEntries(now, cnt, places);
        }
    }

    @Test
    public void testReadYears() {
        Set<Integer> years = flightEntryDAOobjectify.listYears();
        assertEquals(nows.length, years.size());
    }

    @Test
    public void testReadAirfields() {
        for (Calendar now : nows) {
            Set<String> pls = flightEntryDAOobjectify.listAirfields(now.get(Calendar.YEAR));
            assertEquals(places.length, pls.size());
        }
    }

    @Test
    public void testReatDates() {
        Set<Long> dates = flightEntryDAOobjectify.listDates(places[0], nows[0].get(Calendar.YEAR));
        assertEquals(70, dates.size());

    }

    @Test
    public void testReadAllFlightEntries() {
        assertEquals(cnt * nows.length, flightEntryDAOobjectify.listflightEntry().size());

    }

    @Test
    public void testReadFlightEntries() {
        assertEquals(9, flightEntryDAOobjectify.listflightEntry(nows[2], places[20]).size());
    }

    @Test
    public void testReadFlightEntries2() {
        assertEquals(
                10,
                flightEntryDAOobjectify.listflightEntry(nows[2].get(Calendar.YEAR), nows[2].get(Calendar.MONTH),
                        nows[2].get(Calendar.DAY_OF_MONTH), places[20]).size());
    }

    @Test
    public void testReadFlightEntries3() {
        for (Calendar now : nows) {
            for (int i = 0; i < places.length; i++) {
                assertEquals((cnt) / places.length,
                        flightEntryDAOobjectify.listflightEntry(now.get(Calendar.YEAR), now.get(Calendar.YEAR), places[i]).size());
            }
            return; // TODO -- temp
        }
    }

    private void createFlightEntries(Calendar now, int amount, String[] places) {
        final long dayMillies = 1000 * 60;
        // NOTE: period max. one year!
        List<FlightEntry> flightEntries = new ArrayList<FlightEntry>(amount);
        for (int l = 0; l < amount; l++) {
            long millies = now.getTimeInMillis() + (l * dayMillies);
            FlightEntry fe = new FlightEntry(PILOT, millies, millies + 1, false, REMARKS, places[l % places.length]);
            fe.setModified(now.getTimeInMillis());
            flightEntries.add(fe);
        }
        flightEntryDAOobjectify.addFlightEntries4Test(flightEntries);
    }

}
