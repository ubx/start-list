package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ClassUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ch.ubx.startlist.server.maint.Migrate;
import ch.ubx.startlist.shared.FeDate;
import ch.ubx.startlist.shared.FeFlightEntry;
import ch.ubx.startlist.shared.FePlace;
import ch.ubx.startlist.shared.FeYear;
import ch.ubx.startlist.shared.FlightEntry;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class MigrateAndTestTest {

	private static LocalDatastoreServiceTestConfig datastore;
	private static LocalServiceTestHelper helper;
	private static FlightEntryDAOobjectify dao;
	private static FlightEntryDAOobjectify2 dao2;
	private static String sfName = "testdata/" + ClassUtils.getPackageName(MigrateAndTestTest.class) + "/local4junit_db.bin";
	private static String tfName = "testdata/" + ClassUtils.getPackageName(MigrateAndTestTest.class) + "/temp.bin";
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
		helper.setEnvVersionId("x-2-0-0");
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
		dao2 = new FlightEntryDAOobjectify2();
		new PilotDAOobjectify();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test @Ignore
	public void testMigrate() {
		performace();
		performace();
		Migrate.migrate();
		performace2();
		performace2();
	}

	private void performace() {
		long beginTime = System.currentTimeMillis();

		Integer x = 2009;
		Set<Integer> ys = dao.listYears();
		System.out.println("performace  listYears elapsed: " + (System.currentTimeMillis() - beginTime));
		assertEquals(3, ys.size());
		for (Integer i : ys) {
			assertEquals(x++, i);
		}

		List<FlightEntry> fesBefore = dao.listflightEntry();
		System.out.println("performace  listflightEntry elapsed: " + (System.currentTimeMillis() - beginTime));
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

		System.out.println("performace  total elapsed: " + (System.currentTimeMillis() - beginTime));
	}

	private void performace2() {
		long beginTime = System.currentTimeMillis();

		Long x = 2011L;
		List<FeYear> ys = dao2.listYear();
		System.out.println("performace2 listYears elapsed: " + (System.currentTimeMillis() - beginTime));
		assertEquals(3, ys.size());
		for (FeYear feYear : ys) {
			assertEquals(x, feYear.getValue());
			x = x - 1;
		}

		List<FeFlightEntry> fesBefore = dao2.listflightEntry();
		System.out.println("performace2 listflightEntry elapsed: " + (System.currentTimeMillis() - beginTime));
		assertEquals(4644, fesBefore.size());

		List<FePlace> ps = new ArrayList<FePlace>();
		for (FeYear feYear : ys) {
			ps.addAll(dao2.listAirfield(feYear));
			x = x - 1;
		}
		List<FeDate> ds = new ArrayList<FeDate>();
		for (FePlace feplace : ps) {
			ds.addAll(dao2.listDate(feplace));
			x = x - 1;
		}

		List<FeFlightEntry> fes = new ArrayList<FeFlightEntry>();
		for (FeDate feDate : ds) {
			fes.addAll(dao2.listflightEntry(feDate));
		}
		assertEquals(4644, fes.size());

		ps = dao2.listAirfield(2011);
		ds = new ArrayList<FeDate>();
		for (FePlace feplace : ps) {
			ds.addAll(dao2.listDate(feplace));
			x = x - 1;
		}
		fes = new ArrayList<FeFlightEntry>();
		for (FeDate feDate : ds) {
			fes.addAll(dao2.listflightEntry(feDate));
		}
		assertEquals(3060, fes.size());

		ps = dao2.listAirfield(2010);
		ds = new ArrayList<FeDate>();
		for (FePlace feplace : ps) {
			ds.addAll(dao2.listDate(feplace));
			x = x - 1;
		}
		fes = new ArrayList<FeFlightEntry>();
		for (FeDate feDate : ds) {
			fes.addAll(dao2.listflightEntry(feDate));
		}
		assertEquals(1578, fes.size());

		ps = dao2.listAirfield(2009);
		ds = new ArrayList<FeDate>();
		for (FePlace feplace : ps) {
			ds.addAll(dao2.listDate(feplace));
			x = x - 1;
		}
		fes = new ArrayList<FeFlightEntry>();
		for (FeDate feDate : ds) {
			fes.addAll(dao2.listflightEntry(feDate));
		}
		assertEquals(6, fes.size());

		assertEquals(6, dao2.listflightEntry(2009).size());
		assertEquals(0, dao2.listflightEntry(2008).size());

		assertEquals(141, dao2.listflightEntry(2011, 2011, "Bellechasse").size());
		assertEquals(245, dao2.listflightEntry(2010, 2011, "Bellechasse").size());
		assertEquals(245, dao2.listflightEntry(2009, 2011, "Bellechasse").size());
		assertEquals(245, dao2.listflightEntry(2000, 2011, "Bellechasse").size());

		assertEquals(17, dao2.listAirfield(2011).size());
		assertEquals(13, dao2.listAirfield(2010).size());
		assertEquals(1, dao2.listAirfield(2009).size());
		assertEquals(0, dao2.listAirfield(2008).size());

		assertEquals(31, dao2.listflightEntry(2011, 6, 2, "Grenchen Gld").size());
		assertEquals(14, dao2.listflightEntry(2011, 6, 9, "Grenchen Gld").size());

		// do something

		List<FeFlightEntry> fesAfter = dao2.listflightEntry();
		assertEquals(fesBefore.size(), fesAfter.size());

		for (FeFlightEntry fe0 : fesAfter) {
			for (FeFlightEntry fe1 : fesBefore) {
				if (fe1.compareTo(fe0) == 0) {
					fesBefore.remove(fe1);
					break;
				}
			}
		}
		assertEquals(0, fesBefore.size());

		System.out.println("performace2 total elapsed: " + (System.currentTimeMillis() - beginTime));
	}

}
