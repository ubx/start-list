package ch.ubx.startlist.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.ubx.startlist.client.FlightEntryService;
import ch.ubx.startlist.shared.FeDate;
import ch.ubx.startlist.shared.FePlace;
import ch.ubx.startlist.shared.FeYear;
import ch.ubx.startlist.shared.FlightEntry;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FlightEntryServiceImpl extends RemoteServiceServlet implements FlightEntryService {
	private static final long serialVersionUID = 1L;
	private FlightEntryDAO2 flightEntryDAO = new FlightEntryDAOobjectify2();
	private StartTimeComperator startTimeComperator = new StartTimeComperator();

	@Override
	public List<FlightEntry> listFlightEntrie(FeDate date, int startIndex, int maxCount) {
		List<FlightEntry> listFlightEntrys = flightEntryDAO.listflightEntry(date, startIndex, maxCount);
		Collections.sort(listFlightEntrys, startTimeComperator);
		return new ArrayList<FlightEntry>(listFlightEntrys);
	}

	@Override
	public FlightEntry removeFlightEntry(FlightEntry flightEntry) {
		return flightEntryDAO.removeFlightEntry(flightEntry);

	}

	@Override
	public FlightEntry createOrUpdateFlightEntry(FlightEntry flightEntry) {
		return flightEntryDAO.createOrUpdateFlightEntry(flightEntry);
	}

	@Override
	public List<FeDate> listDate(FePlace place) {
		List<FeDate> list = flightEntryDAO.listDate(place);
		Collections.sort(list);
		return list;
	}

	@Override
	public List<FePlace> listPlace(FeYear year) {
		List<FePlace> list = flightEntryDAO.listAirfield(year);
		Collections.sort(list);
		return list;
	}

	@Override
	public List<FeYear> listYear() {
		List<FeYear> list = flightEntryDAO.listYear();
		Collections.sort(list);
		return list;
	}

	private class StartTimeComperator implements Comparator<FlightEntry> {

		@Override
		public int compare(FlightEntry o1, FlightEntry o2) {
			return ((Long) o1.getStartTimeInMillis()).compareTo(o2.getStartTimeInMillis());
		}

	}

}
