package ch.ubx.startlist.server;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ErrorOutputTestHelperTest {

	private static final Logger log = Logger.getLogger(ErrorOutputTestHelperTest.class.getName());

	private final static ErrorOutputTestHelper errOutHelper = new ErrorOutputTestHelper();

	@Before
	public void setUp() throws Exception {
		errOutHelper.setUp();
		errOutHelper.setOutOutput(false);
	}

	@After
	public void tearDown() throws Exception {
		errOutHelper.tearDown();
	}

	//@Test
	public void testSysErr() {
		log.log(Level.INFO, "Test output");
		assertTrue(errOutHelper.getSysErr(), errOutHelper.contains("Test output"));
		assertTrue(errOutHelper.getSysErr(), errOutHelper.contains("Test output"));
		assertTrue("First takeSysErr should get a not empty output", errOutHelper.takeSysErr().contains("Test output"));
		assertTrue("Second takeSysErr should get a  empty output", errOutHelper.takeSysErr().length() == 0);
		for (int i = 0; i < 500; i++) {
			log.log(Level.INFO, "Blablable-" + i);
			assertTrue("ccc", errOutHelper.contains("Blablable-" + i));
		}
	}

}
