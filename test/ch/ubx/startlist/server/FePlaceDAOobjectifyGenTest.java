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
    private static FeGenDAOobjectify<FeYear> yearDAO;
    private static FeGenDAOobjectify<FePlace> placeDAO;
    private static Key<FeYear> currentYear, lastYear;

    @Before
    public void setUp() throws Exception {
        helper.setUp();
        yearDAO = new FeGenDAOobjectify<FeYear>(FeYear.class);
        currentYear = yearDAO.getOrCreateKey("2011");
        lastYear = yearDAO.getOrCreateKey("2010");
        placeDAO = new FeGenDAOobjectify<FePlace>(FePlace.class);
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @Test
    public void testGetOrCreateKey() {
        placeDAO.getOrCreateKey("Place1", currentYear);
        placeDAO.getOrCreateKey("Place2", currentYear);
        placeDAO.getOrCreateKey("Place3", currentYear);
        placeDAO.getOrCreateKey("Place1", lastYear);
        placeDAO.getOrCreateKey("Place2", lastYear);
        placeDAO.getOrCreateKey("Place8", lastYear);
        placeDAO.getOrCreateKey("Place9", lastYear);
        assertEquals(2, yearDAO.list().size()); // total years
        assertEquals(5, placeDAO.list().size()); // total places
        assertEquals(3, placeDAO.list(currentYear).size());
        assertEquals(4, placeDAO.list(lastYear).size());
    }

}
