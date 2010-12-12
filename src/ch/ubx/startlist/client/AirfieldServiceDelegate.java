package ch.ubx.startlist.client;

import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AirfieldServiceDelegate {

	private AirfieldServiceAsync airfieldServiceAsync = GWT.create(AirfieldService.class);
	FlightEntryListGUI gui;

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
