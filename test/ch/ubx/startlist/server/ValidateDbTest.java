package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import ch.ubx.startlist.server.maint.ValidateDb;
import ch.ubx.startlist.shared.FeFlightEntry;

public class ValidateDbTest extends TestWithDb {

	@Test @Ignore
	public void testValidate() {
		// test if db ok
		List<FeFlightEntry> list = flightEntryDAOobjectify.listflightEntry();
		assertEquals("Number of entries not as expected", 123, list.size());

		ValidateDb.Result res = ValidateDb.validate();
		assertEquals("Empty Years not as expected", 0, res.numEmptyYears);
		assertEquals("Empty Places not as expected", 0, res.numEmptyPlaces);
		assertEquals("Empty Dates not as expected", 2, res.numEmptyDates);
		assertEquals("FlightEntries total and in tree sould be equal", res.inTreeFlightEntries, res.totalFlightEntries);

	}
}
