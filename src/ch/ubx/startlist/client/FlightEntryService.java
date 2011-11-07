package ch.ubx.startlist.client;

import java.util.List;

import ch.ubx.startlist.shared.FeDate;
import ch.ubx.startlist.shared.FeFlightEntry;
import ch.ubx.startlist.shared.FePlace;
import ch.ubx.startlist.shared.FeYear;

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

    public List<FeFlightEntry> listFlightEntrie(FeDate date, int startIndex, int maxCount);

    public FeFlightEntry removeFlightEntry(FeFlightEntry flightEntry);

    public FeFlightEntry createOrUpdateFlightEntry(FeFlightEntry flightEntry);

}
