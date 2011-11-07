package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.ubx.startlist.shared.FeStore;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;

public class FeStoreDAOobjectifyGenTest {

	private final static LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
	private final static LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);
	private static IFeGenDAO<FeStore, String> daoStore;

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		daoStore = new FeGenDAOobjectify<FeStore, String>(FeStore.class);
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void testGetOrCreateKey() {
		daoStore.getOrCreateKey("Active");
		daoStore.getOrCreateKey("Archive");
		daoStore.getOrCreateKey("Archive");
		assertEquals(2, daoStore.list().size());
	}

	@Test
	public void testGetOrCreate() {
		daoStore.getOrCreate("Active");
		daoStore.getOrCreate("Archive");
		daoStore.getOrCreate("Archive");
		assertEquals(2, daoStore.list().size());
	}

	@Test
	public void testDeleteKey() {
		Key<FeStore> key = daoStore.getOrCreateKey("Active");
		assertEquals(1, key.getId()); // TODO - ???
		FeStore store = daoStore.get(key);
		assertEquals("Active", store.getValue());
		assertEquals(1, daoStore.list().size());
		Key<FeStore> key2 = daoStore.getOrCreateKey("Active2");
		assertEquals(2, key2.getId()); // TODO - ???
		FeStore store2 = daoStore.get(key2);
		assertEquals("Active2", store2.getValue());
		assertEquals(2, daoStore.list().size());
		daoStore.delete(key);
		assertEquals(1, daoStore.list().size());
		daoStore.delete(key2);
		assertEquals(0, daoStore.list().size());
	}

	@Test
	public void testDelete() {
		FeStore elem = daoStore.getOrCreate("Active", null);
		assertEquals(1, daoStore.list().size());
		FeStore elem2 = daoStore.getOrCreate("Active2", null);
		assertEquals(2, daoStore.list().size());
		daoStore.delete(elem);
		assertEquals(1, daoStore.list().size());
		daoStore.delete(elem2);
		assertEquals(0, daoStore.list().size());
	}

}
