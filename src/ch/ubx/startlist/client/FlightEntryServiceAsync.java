package ch.ubx.startlist.client;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FlightEntryServiceAsync {

	void listFlightEntries(String place, long dateInMillies, int startIndex, int maxCount, AsyncCallback<List<FlightEntry>> callback);

	void removeFlightEntry(FlightEntry flightEntry, AsyncCallback<FlightEntry> callback);

	void createOrUpdateFlightEntry(FlightEntry flightEntry, AsyncCallback<FlightEntry> callback);

	void listDates(String place, int year, AsyncCallback<Set<Long>> callback);

	void listYears(AsyncCallback<Set<Integer>> callback);

	void listPlaces(int year, AsyncCallback<Set<String>> callback);

}
