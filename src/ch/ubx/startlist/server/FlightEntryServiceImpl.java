package ch.ubx.startlist.server;

import java.util.ArrayList;
import java.util.Collections;
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

    @Override
    public List<FlightEntry> listFlightEntrie(FeDate date, int startIndex, int maxCount) {
        List<FlightEntry> listFlightEntrys = flightEntryDAO.listflightEntry(date, startIndex, maxCount);
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

}
