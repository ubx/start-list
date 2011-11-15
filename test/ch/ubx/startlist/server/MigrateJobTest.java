package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ClassUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ubx.startlist.server.maint.MigrateJobs;
import ch.ubx.startlist.shared.ImportOLC;
import ch.ubx.startlist.shared.Job;
import ch.ubx.startlist.shared.PeriodicJob;
import ch.ubx.startlist.shared.PeriodicJob2;
import ch.ubx.startlist.shared.SendExcel;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class MigrateJobTest {

	private static LocalDatastoreServiceTestConfig datastore;
	private static LocalServiceTestHelper helper;
	private static PeriodicJobDAO dao;
	private static PeriodicJobDAO2 dao2;
	private static SendExcelDAO sendExceldao;
	private static ImportOLCDAO importOLCdao;
	private static JobDAO jobdao;

	private static String sfName = "testdata/" + ClassUtils.getPackageName(MigrateJobTest.class) + "/local4junit_db.bin";
	private static String tfName = "testdata/" + ClassUtils.getPackageName(MigrateJobTest.class) + "/temp.bin";
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
		dao = new PeriodicJobDAOobjectify();
		dao2 = new PeriodicJobDAOobjectify2();
		sendExceldao = new SendExcelDAOobjectify();
		importOLCdao = new ImportOLCDAOobjectify();
		jobdao = new JobDAOobjectify();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void testMigrate() {
		List<PeriodicJob> periodicJobs = dao.listAllPeriodicJob();
		List<SendExcel> sendExcels = sendExceldao.listAllSendExcel();
		List<ImportOLC> olcImport = importOLCdao.listAllImportOLC();

		MigrateJobs.migrate();

		List<PeriodicJob2> periodicJobs2 = dao2.listAllPeriodicJob();
		assertEquals(periodicJobs.size(), periodicJobs2.size());

		List<Job> jobs = jobdao.listAllJob();
		assertEquals(sendExcels.size() + olcImport.size(), jobs.size());

		assertEquals(0, dao.listAllPeriodicJob().size());
		assertEquals(0, sendExceldao.listAllSendExcel().size());
		assertEquals(0, importOLCdao.listAllImportOLC().size());

	}

}
