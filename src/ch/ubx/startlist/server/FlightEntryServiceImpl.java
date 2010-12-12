package ch.ubx.startlist.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ch.ubx.startlist.client.FlightEntry;
import ch.ubx.startlist.client.FlightEntryService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FlightEntryServiceImpl extends RemoteServiceServlet implements FlightEntryService {
	private static final long serialVersionUID = 1L;
	private FlightEntryDAO flightEntryDAO = new FlightEntryDAOobjectify();

	public FlightEntry addFlightEntry(FlightEntry flightEntry) {
		return flightEntryDAO.addFlightEntry(flightEntry);
	}

	public List<FlightEntry> listFlightEntries(String place, long dateInMillies, int startIndex, int maxCount) {
		List<FlightEntry> listFlightEntrys = flightEntryDAO.listflightEntry(place, dateInMillies, startIndex, maxCount);
		return new ArrayList<FlightEntry>(listFlightEntrys);
	}

	public FlightEntry removeFlightEntry(FlightEntry flightEntry) {
		return flightEntryDAO.removeFlightEntry(flightEntry);

	}

	public FlightEntry updateFlightEntry(FlightEntry flightEntry) {
		return flightEntryDAO.updateFlightEntry(flightEntry);
	}

	public Set<Long> listDates(String place, int year) {
		return flightEntryDAO.listDates(place, year);
	}

	public Set<String> listPlaces(int year) {
		return flightEntryDAO.listAirfields(year);
	}

	public Set<Integer> listYears() {
		return flightEntryDAO.listYears();
	}

}
