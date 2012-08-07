package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.ubx.startlist.server.maint.ValidateDb;

public class ValidateDbTest extends TestWithDb {

	@Test
	public void testValidate() {
		ValidateDb.Result res = ValidateDb.validate();
		assertEquals("Empty Years not as expected", 0, res.numEmptyYears);
		assertEquals("Empty Places not as expected", 0, res.numEmptyPlaces);
		assertEquals("Empty Dates not as expected", 2, res.numEmptyDates);
		assertEquals("FlightEntries total and in tree sould be equal", res.inTreeFlightEntries, res.totalFlightEntries);

	}

}
