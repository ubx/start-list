package ch.ubx.startlist.client;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

public class TestGWTTest extends GWTTestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test @Ignore
	public void test() {
		FlightEntryListEntryPoint fep = new FlightEntryListEntryPoint();

	}

	@Override
	public String getModuleName() {
		//return "ch.ubx.startlist.Startlist";
		return null; // TODO -- fix!
	}

}
