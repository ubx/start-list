package ch.ubx.startlist.client;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("flightentries")
public interface FlightEntryService extends RemoteService {

	public List<FlightEntry> listFlightEntries(String place, long dateInMillies, int startIndex, int maxCount);

	public Set<Long> listDates(String place, int year);

	public Set<Integer> listYears();

	public Set<String> listPlaces(int year);

	public FlightEntry removeFlightEntry(FlightEntry flightEntry);

	public FlightEntry createOrUpdateFlightEntry(FlightEntry flightEntry);

}
