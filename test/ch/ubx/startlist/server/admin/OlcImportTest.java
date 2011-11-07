package ch.ubx.startlist.server.admin;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.ubx.startlist.shared.FeFlightEntry;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class OlcImportTest {

	private final static LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
	private final static LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);

	@Before
	public void setUp() throws Exception {
		helper.setUp();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void testImportFromOLC() {
		List<FeFlightEntry> list = OlcImport.importFromOLC("TOCUM1", 2012, 5);
		fail("Not yet implemented");
	}

}
