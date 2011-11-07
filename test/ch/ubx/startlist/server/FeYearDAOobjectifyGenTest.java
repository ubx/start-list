package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.ubx.startlist.shared.FeStore;
import ch.ubx.startlist.shared.FeYear;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;

public class FeYearDAOobjectifyGenTest {

	private static final LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
	private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);
	private static FeGenDAOobjectify<FeYear, Long> yearDAO;
	private static FeGenDAOobjectify<FeStore, String> storeDAO;
	private static Key<FeStore> storeActiveKey;
	private static Key<FeStore> storeOldKey;

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		storeDAO = new FeGenDAOobjectify<FeStore, String>(FeStore.class);
		storeActiveKey = storeDAO.getOrCreateKey("Active");
		storeOldKey = storeDAO.getOrCreateKey("Old");
		yearDAO = new FeGenDAOobjectify<FeYear, Long>(FeYear.class);
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void testGetOrCreateKey() {
		yearDAO.getOrCreateKey(2008L, storeActiveKey);
		yearDAO.getOrCreateKey(2009L, storeActiveKey);
		yearDAO.getOrCreateKey(2010L, storeActiveKey);
		yearDAO.getOrCreateKey(2011L, storeActiveKey);
		yearDAO.getOrCreateKey(2011L, storeActiveKey);
		yearDAO.getOrCreateKey(2012L, storeActiveKey);
		assertEquals(5, yearDAO.list().size());

		yearDAO.getOrCreateKey(2001L, storeOldKey);
		yearDAO.getOrCreateKey(2002L, storeOldKey);
		yearDAO.getOrCreateKey(2003L, storeOldKey);
		yearDAO.getOrCreateKey(2008L, storeOldKey);

		assertEquals(2, storeDAO.list().size());
		assertEquals(9, yearDAO.list().size()); // total
		assertEquals(5, yearDAO.list(storeActiveKey).size());
		assertEquals(4, yearDAO.list(storeOldKey).size());

		yearDAO.getOrCreateKey(2001L, storeActiveKey);
		assertEquals(10, yearDAO.list().size()); // total
		assertEquals(6, yearDAO.list(storeActiveKey).size());
		assertEquals(4, yearDAO.list(storeOldKey).size());
		assertEquals(2, storeDAO.list().size());

		FeYear year = yearDAO.getOrCreate(2010L, storeActiveKey);
		assertEquals(2010, year.getValue().intValue());
		assertEquals(2008, yearDAO.getOrCreate(2008L, storeActiveKey).getValue().intValue());
		assertEquals(2010, yearDAO.getOrCreate(2010L, storeOldKey).getValue().intValue());
	}

	@Test
	public void testGetOrCreateKey2() {
		yearDAO.getOrCreateKey(2008L, storeActiveKey);
		yearDAO.getOrCreateKey(2009L, storeActiveKey);
		yearDAO.getOrCreateKey(2010L, storeActiveKey);
		yearDAO.getOrCreateKey(2011L, storeActiveKey);
		yearDAO.getOrCreateKey(2011L, storeActiveKey);
		yearDAO.getOrCreateKey(2001L, storeOldKey);
		yearDAO.getOrCreateKey(2002L, storeOldKey);
		yearDAO.getOrCreateKey(2003L, storeOldKey);
		yearDAO.getOrCreateKey(2004L, storeOldKey);
		assertEquals(2, storeDAO.list().size());
		assertEquals(8, yearDAO.list().size()); // total
		assertEquals(4, yearDAO.list(storeActiveKey).size());
		assertEquals(4, yearDAO.list(storeOldKey).size());
		yearDAO.getOrCreateKey(2001L, storeActiveKey);
		assertEquals(2, storeDAO.list().size());
		assertEquals(9, yearDAO.list().size()); // total
		assertEquals(5, yearDAO.list(storeActiveKey).size());
		assertEquals(4, yearDAO.list(storeOldKey).size());

		long y = yearDAO.getOrCreate(2010L, storeActiveKey).getValue();
		assertEquals(2010, y);
		assertEquals(2010, yearDAO.getOrCreate(2010L, storeOldKey).getValue().intValue());
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
		Key<FeYear> yk = yearDAO.getOrCreateKey(2001L, storeActiveKey);
		FeYear y = yearDAO.get(yk);
		y.setValue(2001L);
		assertEquals(2001L, y.getValue().intValue());
	}

	@Test
	public void testDeleteKeyOfFeYear() {
		Key<FeYear> yk = yearDAO.getOrCreateKey(2001L, storeActiveKey);
		yearDAO.delete(yk);
		assertEquals(0, yearDAO.list().size());
	}

	@Test
	public void testDeleteFeYear() {
		FeYear y = yearDAO.getOrCreate(2001L, storeActiveKey);
		assertEquals(1, yearDAO.list().size());
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
