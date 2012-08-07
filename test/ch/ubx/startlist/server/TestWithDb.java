package ch.ubx.startlist.server;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ClassUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class TestWithDb {

	private static LocalDatastoreServiceTestConfig datastore;
	private static LocalServiceTestHelper helper;
	private static String sfName = "testdata/" + ClassUtils.getPackageName(TestWithDb.class) + "/local_db-01-Aug-2012.bin";
	private static String tfName = "testdata/" + ClassUtils.getPackageName(TestWithDb.class) + "/temp.bin";
	private static File sf = new File(sfName);
	private static File tf = new File(tfName);

	@BeforeClass
	public static void setUpBeforeClass() {
	
		// copy local_db-01-Aug-2012.bin to a temporary db
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
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

}