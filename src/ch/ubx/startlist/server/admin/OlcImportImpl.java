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

import ch.ubx.startlist.client.Airfield;
import ch.ubx.startlist.client.FlightEntry;
import ch.ubx.startlist.client.TimeFormat;
import ch.ubx.startlist.client.admin.OlcImportService;
import ch.ubx.startlist.server.AirfieldDAOobjectify;
import ch.ubx.startlist.server.FlightEntryDAOobjectify;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class OlcImportImpl extends RemoteServiceServlet implements OlcImportService,TimeFormat {

	private static final long serialVersionUID = 1L;

	private AirfieldDAOobjectify airfieldDAO = new AirfieldDAOobjectify();
	private FlightEntryDAOobjectify flightEntryDAO = new FlightEntryDAOobjectify();
	private static final Logger log = Logger.getLogger(Olc2006AirfieldServlet.class.getName());


	@Override
	public List<FlightEntry> importFromPlace(String place, int year) {
		List<FlightEntry> flightEntries = new ArrayList<FlightEntry>();
		try {
			Airfield airfield = airfieldDAO.getAirfield(place);
			if (airfield != null) {
				List<FlightEntry> storedFlightEntries = flightEntryDAO.listflightEntry(year-1, year+1, place);
				OlcImportExtractPilotInfo olcImportExtractPilotInfo = new OlcImportExtractPilotInfo(storedFlightEntries);
				olcImportExtractPilotInfo.setMaxImport(5); // avoid DeadlineExceededException, set amount per RPC call!
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
	 * Merge new FlightEntries with uncompleted FlightEntries in data store. If merge is not possible the new
	 * FlightEntry is added to the data store. Conditions: 1) Date and place are the same. 2) New FlightEntries are
	 * complete and therefore used only once for a merge or add operation. 3) A modification of a FlightEntry from data
	 * store is done only by one new FlightEntry. Merge:
	 * 
	 * @param flightEntries
	 *            Contains new complete FlightEntries of the same place and year.
	 */
	private List<FlightEntry> mergeAddFlightEntries(List<FlightEntry> flightEntries) {
		List<FlightEntry> modifiedFlightEntries = new ArrayList<FlightEntry>();

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
					if (!modifiedFlightEntries.contains(storedFlightEntry)) {
						boolean samePilot = samePilot(newFlightEntry, storedFlightEntry);
						boolean samePlane = samePlane(newFlightEntry, storedFlightEntry);
						boolean sameStart = sameStartTime(newFlightEntry, storedFlightEntry);
						boolean sameEnd = sameEndTime(newFlightEntry, storedFlightEntry);
						if (samePilot & samePlane & !storedFlightEntry.isStartTimeValid() & !storedFlightEntry.isEndTimeValid()) {
							storedFlightEntry.setEndTimeInMillis(newFlightEntry.getEndTimeInMillis());
							storedFlightEntry.setEndTimeValid(true);
							modifiedFlightEntries.add(storedFlightEntry);
						} else if (samePilot & samePlane & sameStart & !sameEnd) {
							storedFlightEntry.setEndTimeInMillis(newFlightEntry.getEndTimeInMillis());
							storedFlightEntry.setEndTimeValid(true);
							modifiedFlightEntries.add(storedFlightEntry);
						} else if (samePilot & samePlane & !sameStart & sameEnd) {
							storedFlightEntry.setStartTimeInMillis(newFlightEntry.getStartTimeInMillis());
							storedFlightEntry.setStartTimeValid(true);
							modifiedFlightEntries.add(storedFlightEntry);
						} else if (samePilot & !samePlane & sameStart & sameEnd) {
							storedFlightEntry.setPlane(newFlightEntry.getPlane());
							modifiedFlightEntries.add(storedFlightEntry);
						} else if (!samePilot & samePlane & sameStart & sameEnd) {
							storedFlightEntry.setPilot(newFlightEntry.getPilot());
							modifiedFlightEntries.add(storedFlightEntry);
						} else {
							if (!modifiedFlightEntries.contains(newFlightEntry)) {
								modifiedFlightEntries.add(newFlightEntry);
							}
						}
					}
				}
			}
		}
		flightEntryDAO.addFlightEntries(modifiedFlightEntries);
		final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (FlightEntry flightEntry : newFlightEntries) {
			log.log(Level.INFO,
					"Place: " + flightEntry.getPlace() + " Start: "
							+ timeFormat.format(new Date(flightEntry.getStartTimeInMillis())));

		}
		return modifiedFlightEntries;
	}

	private boolean samePilot(FlightEntry fe0, FlightEntry fe1) {
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

	private boolean samePlane(FlightEntry fe0, FlightEntry fe1) {
		String name0 = fe0.getPlane();
		String name1 = fe1.getPlane();
		if (name0 != null && name1 != null) {
			if (name0.equalsIgnoreCase(name1)) {
				return true;
			}
		}
		return false;
	}

	private boolean sameStartTime(FlightEntry fe0, FlightEntry fe1) {
		if (fe0.isStartTimeValid() && fe1.isStartTimeValid()) {
			return fe0.getStartTimeInMillis() == fe1.getStartTimeInMillis();
		}
		return false;
	}

	private boolean sameEndTime(FlightEntry fe0, FlightEntry fe1) {
		if (fe0.isEndTimeValid() && fe1.isEndTimeValid()) {
			return fe0.getEndTimeInMillis() == fe1.getEndTimeInMillis();
		}
		return false;
	}

}
