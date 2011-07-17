package ch.ubx.startlist.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ch.ubx.startlist.server.admin.CronJobServletTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ SentFlightEntryDAOobjectifyTest.class, ExcelSenderTest.class, CronJobServletTest.class,
		FlightEntryDAOobjectifyTest.class })
public class AllTests {
}
