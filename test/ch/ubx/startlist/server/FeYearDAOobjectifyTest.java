package ch.ubx.startlist.server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.ubx.startlist.shared.FeStore;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;

public class FeYearDAOobjectifyTest {

    private static final String OLD = "Old";
    private static final String ACTIVE = "Active";
    private static final LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
    private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);
    private static FeYearDAOobjectify yearDAO;
    private static FeStoreDAOobjectify storeDAO;

    @Before
    public void setUp() throws Exception {
        helper.setUp();
        yearDAO = new FeYearDAOobjectify();
        storeDAO = new FeStoreDAOobjectify();
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @Test
    public void testGetOrCreateKey() {
        yearDAO.getOrCreateKey(2008, ACTIVE);
        yearDAO.getOrCreateKey(2009, ACTIVE);
        yearDAO.getOrCreateKey(2010, ACTIVE);
        yearDAO.getOrCreateKey(2011, ACTIVE);
        yearDAO.getOrCreateKey(2011, ACTIVE);
        yearDAO.getOrCreateKey(2001, OLD);
        yearDAO.getOrCreateKey(2002, OLD);
        yearDAO.getOrCreateKey(2003, OLD);
        yearDAO.getOrCreateKey(2004, OLD);
        assertEquals(8, yearDAO.list().size()); // total
        assertEquals(4, yearDAO.list(ACTIVE).size());
        assertEquals(4, yearDAO.list(OLD).size());
        assertEquals(2, storeDAO.list().size());

        // move 2001 from OLD to ACTIVE
        yearDAO.getOrCreateKey(2001, ACTIVE);
        assertEquals(8, yearDAO.list().size()); // total
        assertEquals(5, yearDAO.list(ACTIVE).size());
        assertEquals(3, yearDAO.list(OLD).size());
        assertEquals(2, storeDAO.list().size());

        assertEquals(2010, yearDAO.getOrCreateKey(2010, ACTIVE).getId());
        assertEquals(2008, yearDAO.getOrCreateKey(2008, ACTIVE).getId());
        assertEquals(2010, yearDAO.getOrCreateKey(2010, OLD).getId());

        // TODO -- move to another test
        // FeYear y = yearDAO.getOrCreate(2010, ACTIVE);
        // assertEquals(2010, y.getYear().longValue());
        // assertEquals(ACTIVE, y.getStore().getName());
    }

    @Test
    public void testGetOrCreateKey2() {
        Key<FeStore> sk0 = storeDAO.getOrCreateKey(ACTIVE);
        Key<FeStore> sk1 = storeDAO.getOrCreateKey(OLD);
        yearDAO.getOrCreateKey(2008, sk0);
        yearDAO.getOrCreateKey(2009, sk0);
        yearDAO.getOrCreateKey(2010, sk0);
        yearDAO.getOrCreateKey(2011, sk0);
        yearDAO.getOrCreateKey(2011, sk0);
        yearDAO.getOrCreateKey(2001, sk1);
        yearDAO.getOrCreateKey(2002, sk1);
        yearDAO.getOrCreateKey(2003, sk1);
        yearDAO.getOrCreateKey(2004, sk1);
        assertEquals(8, yearDAO.list().size()); // total
        assertEquals(4, yearDAO.list(ACTIVE).size());
        assertEquals(4, yearDAO.list(OLD).size());
        assertEquals(2, storeDAO.list().size());

        // move 2001 from OLD to ACTIVE
        yearDAO.getOrCreateKey(2001, sk0);
        assertEquals(8, yearDAO.list().size()); // total
        assertEquals(5, yearDAO.list(sk0).size());
        assertEquals(3, yearDAO.list(OLD).size());
        assertEquals(2, storeDAO.list().size());

        assertEquals(2010, yearDAO.getOrCreateKey(2010, sk0).getId());
        assertEquals(2008, yearDAO.getOrCreateKey(2008, sk0).getId());
        assertEquals(2010, yearDAO.getOrCreateKey(2010, sk1).getId());

        // TODO -- move to another test
        // FeYear y = yearDAO.getOrCreate(2010, ACTIVE);
        // assertEquals(2010, y.getYear().longValue());
        // assertEquals(ACTIVE, y.getStore().getName());
    }

    @Test
    public void testGetOrCreateKey3() {
        for (long year = 1000; year < 2000; year++) {
            yearDAO.getOrCreateKey(year, ACTIVE);
        }
        assertEquals(1000, yearDAO.list().size()); // total
        assertEquals(1000, yearDAO.list(ACTIVE).size());
    }

    @Test
    public void testGetOrCreate() {
        fail("Not yet implemented");
    }

    @Test
    public void testGet() {
        fail("Not yet implemented");
    }

    @Test
    public void testDeleteKeyOfFeYear() {
        fail("Not yet implemented");
    }

    @Test
    public void testDeleteFeYear() {
        fail("Not yet implemented");
    }

    @Test
    public void testList() {
        fail("Not yet implemented");
    }

}
