package ch.ubx.startlist.client;

import java.util.Set;

import ch.ubx.startlist.client.Airfield;
import ch.ubx.startlist.client.AirfieldService;
import ch.ubx.startlist.client.AirfieldServiceAsync;
import ch.ubx.startlist.client.ui.FlightEntryListGUI;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AirfieldServiceDelegate {

	public FlightEntryListGUI gui;
	private AirfieldServiceAsync airfieldServiceAsync = GWT.create(AirfieldService.class);

	public void listAirfields() {
		airfieldServiceAsync.getAirfields(new AsyncCallback<Set<Airfield>>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Set<Airfield> result) {
				gui.service_eventAllListPlacesSuccessful(result);
			}
		});
	}

}
