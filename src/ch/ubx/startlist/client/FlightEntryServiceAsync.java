package ch.ubx.startlist.client;

import java.util.List;

import ch.ubx.startlist.shared.FeDate;
import ch.ubx.startlist.shared.FePlace;
import ch.ubx.startlist.shared.FeYear;
import ch.ubx.startlist.shared.FlightEntry;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FlightEntryServiceAsync {

    void listYear(AsyncCallback<List<FeYear>> callback);

    void listPlace(FeYear year, AsyncCallback<List<FePlace>> callback);

    void listDate(FePlace place, AsyncCallback<List<FeDate>> callback);

    void listFlightEntrie(FeDate date, int startIndex, int maxCount, AsyncCallback<List<FlightEntry>> callback);

    void removeFlightEntry(FlightEntry flightEntry, AsyncCallback<FlightEntry> callback);

    void createOrUpdateFlightEntry(FlightEntry flightEntry, AsyncCallback<FlightEntry> callback);

}
