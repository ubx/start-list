package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ClassUtils;
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
	private static String sfName = "testdata/" + ClassUtils.getPackageName(UseCaseTest.class) + "/local4junit_db.bin";
	private static String tfName = "testdata/" + ClassUtils.getPackageName(UseCaseTest.class) + "/temp.bin";
	private static File sf = new File(sfName);
	private static File tf = new File(tfName);

	@BeforeClass
	public static void setUpBeforeClass() {

		// copy local4junit_db.bin to a temporary db
		try {
			FileUtils.copyFile(sf, tf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		datastore = new LocalDatastoreServiceTestConfig();
		datastore.setBackingStoreLocation(tfName);
		datastore.setNoStorage(false);
		helper = new LocalServiceTestHelper(datastore);
		helper.setEnvAppId("start-list");
		helper.setEnvAuthDomain("gmail.com");
	}

	@AfterClass
	public static void tearDownAfterClass() {
		FileUtils.deleteQuietly(tf);
	}

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		dao = new FlightEntryDAOobjectify();
		// new PilotDAOobjectify();
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
		assertEquals(4644, fesBefore.size());
		assertEquals(3060, dao.listflightEntry(2011).size());
		assertEquals(1578, dao.listflightEntry(2010).size());
		assertEquals(6, dao.listflightEntry(2009).size());
		assertEquals(0, dao.listflightEntry(2008).size());

		assertEquals(141, dao.listflightEntry(2011, 2011, "Bellechasse").size());
		assertEquals(245, dao.listflightEntry(2010, 2011, "Bellechasse").size());
		assertEquals(245, dao.listflightEntry(2009, 2011, "Bellechasse").size());
		assertEquals(245, dao.listflightEntry(2000, 2011, "Bellechasse").size());

		assertEquals(17, dao.listAirfields(2011).size());
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
	}

}
