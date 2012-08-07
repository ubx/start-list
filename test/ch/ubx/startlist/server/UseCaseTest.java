package ch.ubx.startlist.server;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ch.ubx.startlist.shared.FeFlightEntry;
import ch.ubx.startlist.shared.FeYear;

public class UseCaseTest extends TestWithDb {

	@Test
	public void test_00() {
		FlightEntryDAOobjectify2 dao = new FlightEntryDAOobjectify2();

		Long x = 2009L;
		List<FeYear> ys = dao.listYear();
		Collections.sort(ys);

		assertEquals(4, ys.size());
		for (FeYear y : ys) {
			assertEquals(x++, y.getValue());
		}

		List<FeFlightEntry> fesBefore = dao.listflightEntry();
		assertEquals(6768, fesBefore.size());
		assertEquals(1867, dao.listflightEntry(2012).size());
		assertEquals(3316, dao.listflightEntry(2011).size());
		assertEquals(1579, dao.listflightEntry(2010).size());
		assertEquals(6, dao.listflightEntry(2009).size());
		assertEquals(0, dao.listflightEntry(2008).size());

		assertEquals(219, dao.listflightEntry(2011, 2012, "Bellechasse").size());
		assertEquals(142, dao.listflightEntry(2011, 2011, "Bellechasse").size());
		assertEquals(246, dao.listflightEntry(2010, 2011, "Bellechasse").size());
		assertEquals(246, dao.listflightEntry(2009, 2011, "Bellechasse").size());
		assertEquals(246, dao.listflightEntry(2000, 2011, "Bellechasse").size());

		assertEquals(8, dao.listAirfield(2012).size());
		assertEquals(19, dao.listAirfield(2011).size());
		assertEquals(13, dao.listAirfield(2010).size());
		assertEquals(1, dao.listAirfield(2009).size());
		assertEquals(0, dao.listAirfield(2008).size());

		assertEquals(10, dao.listflightEntry(2012, 7, 1, "Grenchen Gld").size());
		assertEquals(31, dao.listflightEntry(2011, 6, 2, "Grenchen Gld").size());
		assertEquals(14, dao.listflightEntry(2011, 6, 9, "Grenchen Gld").size());

		// TODO -- do something

		List<FeFlightEntry> fesAfter = dao.listflightEntry();
		assertEquals(fesBefore.size(), fesAfter.size());

		for (FeFlightEntry fe0 : fesAfter) {
			for (FeFlightEntry fe1 : fesBefore) {
				if (fe1.compareTo(fe0) == 0) {
					fesBefore.remove(fe1);
					break;
				}
			}
		}
		assertEquals(0, fesBefore.size());
	}

}
