package ch.ubx.startlist.server.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.ubx.startlist.server.AirfieldDAOobjectify;
import ch.ubx.startlist.server.FlightEntryDAO;
import ch.ubx.startlist.server.FlightEntryDAOobjectify;
import ch.ubx.startlist.shared.Airfield;
import ch.ubx.startlist.shared.FlightEntry;

public class OlcImportMain {

	private static AirfieldDAOobjectify airfieldDAO = new AirfieldDAOobjectify();
	private static FlightEntryDAO flightEntryDAO = new FlightEntryDAOobjectify();
	private static final Logger log = Logger.getLogger(Olc2006AirfieldServlet.class.getName());

	/**
	 * @param place
	 * @param year
	 * @return
	 */
	public static List<FlightEntry> importFromOLC(String place, int year, int maxImportAtOnce) {
		List<FlightEntry> flightEntries = new ArrayList<FlightEntry>();
		try {
			Airfield airfield = airfieldDAO.getAirfield(place);
			if (airfield != null) {
				List<FlightEntry> storedFlightEntries = flightEntryDAO.listflightEntry(year - 1, year + 1, place);
				OlcImportExtractPilotInfo olcImportExtractPilotInfo = new OlcImportExtractPilotInfo(storedFlightEntries);
				olcImportExtractPilotInfo.setMaxImport(maxImportAtOnce); // avoid DeadlineExceededException, set amount per RPC call!
				flightEntries = olcImportExtractPilotInfo.olcImportFromPlace(airfield.getId(), place, year, airfield.getCountry());
				flightEntries = mergeAddFlightEntries(flightEntries);
				return flightEntries;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flightEntries; // an empty list!
	}

	/**
	 * Merge new FlightEntries with uncompleted FlightEntries in data store. If merge is not possible the new FlightEntry is added to the data store. <code>
	 * Conditions: 1) Date and place are the same.
	 *             2) New FlightEntries are complete and therefore used only once for a merge or add operation.
	 *             3) A modification of a FlightEntry from data store is done only by one new FlightEntry. Merge rules:
	 *           
	 *       OLC flight:           +==============+
	 *  Manual flight 1:         ==|==============|====                 copy start and end
	 *                2:        +==|==============|==                   copy end
	 *                3:           |  +=========  |                     copy end
	 *                4:        ===|============+ |                     copy start
	 *                5:        ===|==============|======+              copy start
	 *                6:           |  +===========|==+                  ignore
	 *                7:        +==|========+     |                     ignore
	 *                8:        +==|==============|===+                 ignore
	 *                9:           |              |  +========+         add
	 *               10:  +=====+  |              |                     add
	 * 
	 *            Key: + defined start/end
	 *                 = open start/end
	 *                 | start/end OLC flight
	 *            
	 *            </code> * 7: +==|==============|===+ do nothing
	 * 
	 * 
	 * @param flightEntries
	 *            Contains new complete FlightEntries of the same place and year.
	 */
	private static List<FlightEntry> mergeAddFlightEntries(List<FlightEntry> flightEntries) {

		List<FlightEntry> modifiedFlightEntries = new ArrayList<FlightEntry>();
		List<FlightEntry> usedNewFlightEntries = new ArrayList<FlightEntry>();

		// eliminate duplicates!
		Set<FlightEntry> newFlightEntries = new TreeSet<FlightEntry>(flightEntries);

		for (FlightEntry newFlightEntry : newFlightEntries) {
			Calendar date = Calendar.getInstance(); // TODO - set timezone UTC?
			date.setTimeInMillis(newFlightEntry.getStartTimeInMillis());
			List<FlightEntry> storedFlightEntries = flightEntryDAO.listflightEntry(date, newFlightEntry.getPlace());
			if (storedFlightEntries.size() == 0) {
				modifiedFlightEntries.add(newFlightEntry);
			} else {
				for (FlightEntry storedFlightEntry : storedFlightEntries) {
					if (!modifiedFlightEntries.contains(storedFlightEntry) & samePilot(newFlightEntry, storedFlightEntry)
							& samePlane(newFlightEntry, storedFlightEntry)) {
						// 1:
						if (!storedFlightEntry.isStartTimeValid() & !storedFlightEntry.isEndTimeGliderValid()) {
							storedFlightEntry.setStartTimeInMillis(newFlightEntry.getStartTimeInMillis());
							storedFlightEntry.setEndTimeGliderInMillis(newFlightEntry.getEndTimeGliderInMillis());
							modifiedFlightEntries.add(storedFlightEntry);
							usedNewFlightEntries.add(newFlightEntry);
							log.log(Level.INFO, "Merge 1:  Pilot: " + storedFlightEntry.getPilot() + " ,Plane: " + storedFlightEntry.getRegistrationGlider());
						}
						// 2+3:
						else if (!storedFlightEntry.isEndTimeGliderValid()
								& storedFlightEntry.getStartTimeInMillis() < newFlightEntry.getEndTimeGliderInMillis()) {
							storedFlightEntry.setEndTimeGliderInMillis(newFlightEntry.getEndTimeGliderInMillis());
							modifiedFlightEntries.add(storedFlightEntry);
							usedNewFlightEntries.add(newFlightEntry);
							log.log(Level.INFO, "Merge 2+3:  Pilot: " + storedFlightEntry.getPilot() + " ,Plane: " + storedFlightEntry.getRegistrationGlider());
						}
						// 4+5:
						else if (!storedFlightEntry.isStartTimeValid() & storedFlightEntry.getEndTimeGliderInMillis() > newFlightEntry.getStartTimeInMillis()) {
							storedFlightEntry.setStartTimeInMillis(newFlightEntry.getStartTimeInMillis());
							modifiedFlightEntries.add(storedFlightEntry);
							usedNewFlightEntries.add(newFlightEntry);
							log.log(Level.INFO, "Merge 4+5:  Pilot: " + storedFlightEntry.getPilot() + " ,Plane: " + storedFlightEntry.getRegistrationGlider());
						}
						// 6..8:
						else if (!(storedFlightEntry.getEndTimeGliderInMillis() < newFlightEntry.getStartTimeInMillis() & storedFlightEntry
								.getStartTimeInMillis() > newFlightEntry.getEndTimeGliderInMillis())) {
							usedNewFlightEntries.add(newFlightEntry);
							log.log(Level.INFO, "Merge 6..8:  Pilot: " + storedFlightEntry.getPilot() + " ,Plane: " + storedFlightEntry.getRegistrationGlider());

						} // 9+10:
						else if (!modifiedFlightEntries.contains(newFlightEntry)) {
							modifiedFlightEntries.add(newFlightEntry);
							log.log(Level.INFO, "Merge 9+10:  Pilot: " + newFlightEntry.getPilot() + " ,Plane: " + newFlightEntry.getRegistrationGlider());
						}
					}
				}
				if (!usedNewFlightEntries.contains(newFlightEntry)) {
					modifiedFlightEntries.add(newFlightEntry);
				}
			}
		}
		flightEntryDAO.addFlightEntries(modifiedFlightEntries);
		final SimpleDateFormat timeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm z");
		for (FlightEntry flightEntry : modifiedFlightEntries) {
			log.log(Level.INFO, "Place: " + flightEntry.getPlace() + " ,Start: " + timeFormat.format(new Date(flightEntry.getStartTimeInMillis()))
					+ " ,Pilot: " + flightEntry.getPilot() + " ,Plane: " + flightEntry.getRegistrationGlider());
		}
		return modifiedFlightEntries;
	}

	private static boolean samePilot(FlightEntry fe0, FlightEntry fe1) {
		String name0 = fe0.getPilot();
		String name1 = fe1.getPilot();
		if (name0 != null && name1 != null) {
			if (name0.equalsIgnoreCase(name1)) {
				return true;
			}
			String[] x0 = name0.split(" ");
			String[] x1 = name1.split(" ");
			if (x0.length == 2 && x1.length == 2) {
				return x0[0].equalsIgnoreCase(x1[1]) & x0[1].equalsIgnoreCase(x1[0]);
			}
		}
		return false;
	}

	private static boolean samePlane(FlightEntry fe0, FlightEntry fe1) {
		String name0 = fe0.getRegistrationGlider();
		String name1 = fe1.getRegistrationGlider();
		if (name0 != null && name1 != null) {
			if (name0.equalsIgnoreCase(name1)) {
				return true;
			}
		}
		return false;
	}

}
