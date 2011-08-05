package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ubx.startlist.shared.FlightEntry;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class UseCaseTest {

	private static LocalDatastoreServiceTestConfig datastore;
	private static LocalServiceTestHelper helper;
	private static FlightEntryDAOobjectify dao;
	@BeforeClass
	public static void setUpBeforeClass() {
		datastore = new LocalDatastoreServiceTestConfig();
		datastore.setBackingStoreLocation("war/WEB-INF/appengine-generated/local4junit_db.bin");
		datastore.setNoStorage(false);
		helper = new LocalServiceTestHelper(datastore);
		helper.setEnvAppId("start-list");
		helper.setEnvAuthDomain("gmail.com");
	}

	@AfterClass
	public static void tearDownAfterClass() {
	}

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		dao = new FlightEntryDAOobjectify();
		new PilotDAOobjectify();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void testX() {
		Integer x = 2009;
		Set<Integer> ys = dao.listYears();
		assertEquals(3, ys.size());
		for (Integer i : ys) {
			assertEquals(x++, i);
		}

		List<FlightEntry> fesBefore = dao.listflightEntry();
		assertEquals(3796, fesBefore.size());
		assertEquals(2212, dao.listflightEntry(2011).size());
		assertEquals(1578, dao.listflightEntry(2010).size());
		assertEquals(6, dao.listflightEntry(2009).size());
		assertEquals(0, dao.listflightEntry(2008).size());

		assertEquals(112, dao.listflightEntry(2011, 2011, "Bellechasse").size());
		assertEquals(216, dao.listflightEntry(2010, 2011, "Bellechasse").size());
		assertEquals(216, dao.listflightEntry(2009, 2011, "Bellechasse").size());
		assertEquals(216, dao.listflightEntry(2000, 2011, "Bellechasse").size());

		assertEquals(16, dao.listAirfields(2011).size());
		assertEquals(13, dao.listAirfields(2010).size());
		assertEquals(1, dao.listAirfields(2009).size());
		assertEquals(0, dao.listAirfields(2008).size());

		assertEquals(31, dao.listflightEntry(2011, 6, 2, "Grenchen Gld").size());
		assertEquals(14, dao.listflightEntry(2011, 6, 9, "Grenchen Gld").size());

		// do something

		List<FlightEntry> fesAfter = dao.listflightEntry();
		assertEquals(fesBefore.size(), fesAfter.size());

		for (FlightEntry fe0 : fesAfter) {
			for (FlightEntry fe1 : fesBefore) {
				if (fe1.compareTo(fe0) == 0) {
					fesBefore.remove(fe1);
					break;
				}
			}
		}
		assertEquals(0, fesBefore.size());

		// assertEquals(1, daoP.getPilots().size());

		// Pilot p = new Pilot();
		// p.setName("FFFF");
		// daoP.addPilot(p);
		// Pilot p2 = daoP.getPilot("FFFF");
		// assertEquals("FFFF", p2.getName());

		// assertEquals(1, ys.size());

		// assertEquals(3,dao.listflightEntry(2011).size());

	}

}
