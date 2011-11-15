package ch.ubx.startlist.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ch.ubx.startlist.server.admin.CronJobServletTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ObjectifyTest.class, SentFlightEntryDAOobjectifyTest.class, ExcelSenderTest.class, CronJobServletTest.class,
		FlightEntryDAOobjectifyTest.class, FeStoreDAOobjectifyGenTest.class, FeYearDAOobjectifyGenTest.class, FePlaceDAOobjectifyGenTest.class,
		FlightEntryDAOobjectify2Test.class, UseCaseTest.class, MigrateAndTestTest.class, MigrateJobTest.class })
public class AllTests {
}
