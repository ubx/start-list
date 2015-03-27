package ch.ubx.startlist.client;

import java.util.Set;

import ch.ubx.startlist.client.ui.FlightEntryListGUI;
import ch.ubx.startlist.shared.Airfield;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AirfieldServiceDelegate {

	private FlightEntryListGUI gui;

	public AirfieldServiceDelegate(FlightEntryListGUI gui) {
		super();
		this.gui = gui;
	}

	private AirfieldServiceAsync airfieldServiceAsync = GWT.create(AirfieldService.class);

	public void listAirfields() {
		airfieldServiceAsync.getAirfields(new AsyncCallback<Set<Airfield>>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Set<Airfield> result) {
				gui.service_eventAllListPlacesSuccessful(result);
			}
		});
	}

}
