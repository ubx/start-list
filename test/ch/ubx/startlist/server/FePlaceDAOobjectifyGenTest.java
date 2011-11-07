package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.ubx.startlist.shared.FePlace;
import ch.ubx.startlist.shared.FeYear;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;

public class FePlaceDAOobjectifyGenTest {

    private static final LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
    private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);
    private static FeGenDAOobjectify<FeYear, Long> yearDAO;   
    private static FeGenDAOobjectify<FePlace, String> placeDAO;
    private static Key<FeYear> currentYear;
    private static Key<FeYear> lastYear;

    @Before
    public void setUp() throws Exception {
        helper.setUp();
        yearDAO = new FeGenDAOobjectify<FeYear, Long>(FeYear.class);
        currentYear = yearDAO.getOrCreateKey(2011L);
        lastYear = yearDAO.getOrCreateKey(2010L);
        placeDAO = new FeGenDAOobjectify<FePlace, String>(FePlace.class);
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @Test
    public void testGetOrCreateKey() {
        Key<FePlace> p1k = placeDAO.getOrCreateKey("Place1", currentYear);
        Key<FePlace> p2k = placeDAO.getOrCreateKey("Place2", currentYear);
        Key<FePlace> p3k = placeDAO.getOrCreateKey("Place3", currentYear);
        Key<FePlace> p4k = placeDAO.getOrCreateKey("Place1", lastYear);
        Key<FePlace> p5k = placeDAO.getOrCreateKey("Place2", lastYear);
        Key<FePlace> p6k = placeDAO.getOrCreateKey("Place8", lastYear);
        Key<FePlace> p7k = placeDAO.getOrCreateKey("Place9", lastYear);

        assertEquals(2, yearDAO.list().size()); // total years
        assertEquals(7, placeDAO.list().size()); // total places
        assertEquals(3, placeDAO.list(currentYear).size());
        assertEquals(4, placeDAO.list(lastYear).size());

        assertEquals("Place1", placeDAO.get(p1k).getValue());
        assertEquals("Place2", placeDAO.get(p2k).getValue());
        assertEquals("Place3", placeDAO.get(p3k).getValue());
        assertEquals("Place1", placeDAO.get(p4k).getValue());
        assertEquals("Place2", placeDAO.get(p5k).getValue());
        assertEquals("Place8", placeDAO.get(p6k).getValue());
        assertEquals("Place9", placeDAO.get(p7k).getValue());

    }

}
