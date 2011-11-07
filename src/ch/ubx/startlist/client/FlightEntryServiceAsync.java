package ch.ubx.startlist.client;

import java.util.List;

import ch.ubx.startlist.shared.FeDate;
import ch.ubx.startlist.shared.FeFlightEntry;
import ch.ubx.startlist.shared.FePlace;
import ch.ubx.startlist.shared.FeYear;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FlightEntryServiceAsync {

    void listYear(AsyncCallback<List<FeYear>> callback);

    void listPlace(FeYear year, AsyncCallback<List<FePlace>> callback);

    void listDate(FePlace place, AsyncCallback<List<FeDate>> callback);

    void listFlightEntrie(FeDate date, int startIndex, int maxCount, AsyncCallback<List<FeFlightEntry>> callback);

    void removeFlightEntry(FeFlightEntry flightEntry, AsyncCallback<FeFlightEntry> callback);

    void createOrUpdateFlightEntry(FeFlightEntry flightEntry, AsyncCallback<FeFlightEntry> callback);

}
