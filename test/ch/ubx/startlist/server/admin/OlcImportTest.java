package ch.ubx.startlist.server.admin;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.ubx.startlist.server.AirfieldDAOobjectify;
import ch.ubx.startlist.server.FeGenDAOobjectify;
import ch.ubx.startlist.shared.Airfield;
import ch.ubx.startlist.shared.FeFlightEntry;
import ch.ubx.startlist.shared.FePlace;
import ch.ubx.startlist.shared.FeYear;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;

public class OlcImportTest {

	private final static LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
	private final static LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);
	private static FeGenDAOobjectify<FeYear, Long> yearDAO;
	private static FeGenDAOobjectify<FePlace, String> placeDAO;
	private static AirfieldDAOobjectify airfieldDAO;
	private static Key<FeYear> currentYear;

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		yearDAO = new FeGenDAOobjectify<FeYear, Long>(FeYear.class);
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void testImportFromOLC() {
		String aa = "TOCUM1";
		String c = "AU";
		currentYear = yearDAO.getOrCreateKey(2012L);
		placeDAO = new FeGenDAOobjectify<FePlace, String>(FePlace.class);
		placeDAO.getOrCreateKey(aa, currentYear);
		airfieldDAO = new AirfieldDAOobjectify();
		Airfield af = new Airfield();
		af.setName(aa);
		af.setId(aa);
		af.setCountry(c);
		airfieldDAO.addAirfield(af);
		List<FeFlightEntry> list = OlcImport.importFromOLC(aa, 2011, 5);
		assertEquals("Should import 5 Flight", 5, list.size());
		for (FeFlightEntry fe : list) {
			assertEquals("Landing place should be " + aa, aa, fe.getLandingPlace());
		}
		
		// Assumption: this entry is the 2nd in the list from Tocumwal, year 2011. The list is 'static'. 
		FeFlightEntry fe = list.get(1);
		assertEquals("CompetitionID wrong", "kyo", fe.getCompetitionID());
		assertEquals("Club wrong", "SportAviation Tocumwal", fe.getClub());
		assertEquals("Pilot wrong", "Rhys Dyer", fe.getPilot());
		assertEquals("RegistrationGlider wrong", "KYO", fe.getRegistrationGlider());
	}
	
}
