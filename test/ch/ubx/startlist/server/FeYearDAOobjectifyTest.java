package ch.ubx.startlist.server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.ubx.startlist.shared.FeStore;
import ch.ubx.startlist.shared.FeYear;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;

public class FeYearDAOobjectifyTest {

    private static final LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
    private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);
    private static FeYearDAOobjectify yearDAO;
    private static FeStoreDAOobjectify storeDAO;
    private static Key<FeStore> storeActiveKey;
    private static Key<FeStore> storeOldKey;

    @Before
    public void setUp() throws Exception {
        helper.setUp();
        yearDAO = new FeYearDAOobjectify();
        storeDAO = new FeStoreDAOobjectify();
        storeActiveKey = storeDAO.getOrCreateKey("Active");
        storeOldKey = storeDAO.getOrCreateKey("Old");
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @Test
    public void testGetOrCreateKey() {
        yearDAO.getOrCreateKey(2008, storeActiveKey);
        yearDAO.getOrCreateKey(2009, storeActiveKey);
        yearDAO.getOrCreateKey(2010, storeActiveKey);
        yearDAO.getOrCreateKey(2011, storeActiveKey);
        yearDAO.getOrCreateKey(2011, storeActiveKey);
        yearDAO.getOrCreateKey(2001, storeOldKey);
        yearDAO.getOrCreateKey(2002, storeOldKey);
        yearDAO.getOrCreateKey(2003, storeOldKey);
        yearDAO.getOrCreateKey(2004, storeOldKey);
        assertEquals(8, yearDAO.list().size()); // total
        assertEquals(4, yearDAO.list(storeActiveKey).size());
        assertEquals(4, yearDAO.list(storeOldKey).size());
        assertEquals(2, storeDAO.list().size());

        // move 2001 from OLD to ACTIVE
        yearDAO.getOrCreateKey(2001, storeActiveKey);
        assertEquals(8, yearDAO.list().size()); // total
        assertEquals(5, yearDAO.list(storeActiveKey).size());
        assertEquals(3, yearDAO.list(storeOldKey).size());
        assertEquals(2, storeDAO.list().size());

        assertEquals(2010, yearDAO.getOrCreateKey(2010, storeActiveKey).getId());
        assertEquals(2008, yearDAO.getOrCreateKey(2008, storeActiveKey).getId());
        assertEquals(2010, yearDAO.getOrCreateKey(2010, storeOldKey).getId());
    }

    @Test
    public void testGetOrCreateKey2() {
        yearDAO.getOrCreateKey(2008, storeActiveKey);
        yearDAO.getOrCreateKey(2009, storeActiveKey);
        yearDAO.getOrCreateKey(2010, storeActiveKey);
        yearDAO.getOrCreateKey(2011, storeActiveKey);
        yearDAO.getOrCreateKey(2011, storeActiveKey);
        yearDAO.getOrCreateKey(2001, storeOldKey);
        yearDAO.getOrCreateKey(2002, storeOldKey);
        yearDAO.getOrCreateKey(2003, storeOldKey);
        yearDAO.getOrCreateKey(2004, storeOldKey);
        assertEquals(8, yearDAO.list().size()); // total
        assertEquals(4, yearDAO.list(storeActiveKey).size());
        assertEquals(4, yearDAO.list(storeOldKey).size());
        assertEquals(2, storeDAO.list().size());

        // move 2001 from OLD to ACTIVE
        yearDAO.getOrCreateKey(2001, storeActiveKey);
        assertEquals(8, yearDAO.list().size()); // total
        assertEquals(5, yearDAO.list(storeActiveKey).size());
        assertEquals(3, yearDAO.list(storeOldKey).size());
        assertEquals(2, storeDAO.list().size());

        assertEquals(2010, yearDAO.getOrCreateKey(2010, storeActiveKey).getId());
        assertEquals(2008, yearDAO.getOrCreateKey(2008, storeActiveKey).getId());
        assertEquals(2010, yearDAO.getOrCreateKey(2010, storeOldKey).getId());
    }

    @Test
    public void testGetOrCreateKey3() {
        for (long year = 1000; year < 2000; year++) {
            yearDAO.getOrCreateKey(year, storeActiveKey);
        }
        assertEquals(1000, yearDAO.list().size()); // total
        assertEquals(1000, yearDAO.list(storeActiveKey).size());
    }

    @Test
    public void testGet() {
        Key<FeYear> yk = yearDAO.getOrCreateKey(2001, storeActiveKey);
        assertEquals(2001, yearDAO.get(yk).getYear().longValue());
    }

    @Test
    public void testDeleteKeyOfFeYear() {
        Key<FeYear> yk = yearDAO.getOrCreateKey(2001, storeActiveKey);
        yearDAO.delete(yk);
        assertEquals(0, yearDAO.list().size());
    }

    @Test
    public void testDeleteFeYear() {
        FeYear y = yearDAO.getOrCreate(2001, storeActiveKey);
        yearDAO.delete(y);
        assertEquals(0, yearDAO.list().size());
    }

    @Test
    public void testList() {
        for (long i = 2000; i < 2011; i++) {
            yearDAO.getOrCreateKey(i, storeActiveKey);
        }
        assertEquals(11, yearDAO.list().size());
    }

}
