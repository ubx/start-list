package ch.ubx.startlist.client.admin;

import java.util.List;

import ch.ubx.startlist.shared.FlightEntry;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OlcImportServiceAsync {

	void importFromPlace(String place, int year, AsyncCallback<List<FlightEntry>> callback);

}
