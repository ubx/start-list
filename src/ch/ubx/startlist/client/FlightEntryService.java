package ch.ubx.startlist.client;

import java.util.List;

import ch.ubx.startlist.shared.FeDate;
import ch.ubx.startlist.shared.FePlace;
import ch.ubx.startlist.shared.FeYear;
import ch.ubx.startlist.shared.FlightEntry;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("flightentries")
public interface FlightEntryService extends RemoteService {

    public List<FeYear> listYear();

    public List<FePlace> listPlace(FeYear year);

    public List<FeDate> listDate(FePlace place);

    public List<FlightEntry> listFlightEntrie(FeDate date, int startIndex, int maxCount);

    public FlightEntry removeFlightEntry(FlightEntry flightEntry);

    public FlightEntry createOrUpdateFlightEntry(FlightEntry flightEntry);

}
