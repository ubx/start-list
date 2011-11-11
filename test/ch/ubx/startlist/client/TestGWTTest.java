package ch.ubx.startlist.client;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

public class TestGWTTest extends GWTTestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		FlightEntryListEntryPoint fep = new FlightEntryListEntryPoint();
		assertNotNull(fep);

	}

	@Override
	public String getModuleName() {
		return "ch.ubx.startlist.StartList";
	}

}
