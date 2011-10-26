package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;

public class ObjectifyTest extends DAOBase {


	private final static LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
	private final static LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);
	private final static int SIZE = 10000;
	private final static List<DummyEntry> list = new ArrayList<DummyEntry>(SIZE);
	
	static {
		ObjectifyService.register(DummyEntry.class);
		for (int i = 0; i < SIZE; i++) {
			list.add(new DummyEntry("Label-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx-" + i));
		}
	}

	@Before
	public void setUp() throws Exception {
		helper.setUp();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void testList1() {
		assertEquals(SIZE, list.size());
		ofy().put(list);
		Query<DummyEntry> query = ofy().query(DummyEntry.class);
		List<DummyEntry> list2 = query.list();
		assertEquals(list.size(), list2.size());
		for (DummyEntry dummyEntry : list2) {
			assertTrue(list.contains(dummyEntry));
		}
	}

	@Test
	public void testList2() {
		assertEquals(SIZE, list.size());
		for (DummyEntry dummyEntry : list) {
			ofy().put(dummyEntry);
		}
		Query<DummyEntry> query = ofy().query(DummyEntry.class);
		List<DummyEntry> list2 = query.list();
		assertEquals(list.size(), list2.size());
		for (DummyEntry dummyEntry : list2) {
			assertTrue(list.contains(dummyEntry));
		}
	}

}
