package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.ubx.startlist.shared.FeStore;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;

public class FeStoreDAOobjectifyTest {

    private final static LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
    private final static LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);
    private static FeStoreDAOobjectify dao;

    @Before
    public void setUp() throws Exception {
        helper.setUp();
        dao = new FeStoreDAOobjectify();
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @Test
    public void testGetOrCreateKey() {
        dao.getOrCreateKey("Active");
        dao.getOrCreateKey("Archive");
        dao.getOrCreateKey("Archive");
        assertEquals(2, dao.list().size());
    }

    @Test
    public void testGetOrCreate() {
        dao.getOrCreate("Active");
        dao.getOrCreate("Archive");
        dao.getOrCreate("Archive");
        assertEquals(2, dao.list().size());
    }

    @Test
    public void testGet() {
        FeStore elem = dao.get(dao.getOrCreateKey("Active"));
        assertEquals("Active", elem.getName());
        FeStore elem2 = dao.get(dao.getOrCreateKey("Archive"));
        assertEquals("Archive", elem2.getName());
        FeStore elem3 = dao.get(dao.getOrCreateKey("Archive"));
        assertEquals("Archive", elem3.getName());
        assertEquals(2, dao.list().size());
    }

    @Test
    public void testDeleteKey() {
        Key<FeStore> key = dao.getOrCreateKey("Active");
        assertEquals("Active", key.getName());
        assertEquals(1, dao.list().size());
        Key<FeStore> key2 = dao.getOrCreateKey("Active2");
        assertEquals("Active2", key2.getName());
        assertEquals(2, dao.list().size());
        dao.delete(key);
        assertEquals(1, dao.list().size());
        dao.delete(key2);
        assertEquals(0, dao.list().size());
    }

    @Test
    public void testDelete() {
        FeStore elem = dao.getOrCreate("Active");
        assertEquals(1, dao.list().size());
        FeStore elem2 = dao.getOrCreate("Active2");
        assertEquals(2, dao.list().size());
        dao.delete(elem);
        assertEquals(1, dao.list().size());
        dao.delete(elem2);
        assertEquals(0, dao.list().size());
    }

}
