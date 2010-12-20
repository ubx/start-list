package ch.ubx.startlist.client.admin;

import java.util.List;

import ch.ubx.startlist.client.FlightEntry;
import ch.ubx.startlist.client.admin.ui.AdminGUI;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OlcImportServiceDelegate {
	
	private OlcImportServiceAsync olcImportServiceAsync = GWT.create(OlcImportService.class);

	private AdminGUI gui;

	public OlcImportServiceDelegate(AdminGUI gui) {
		this.gui = gui;
	}

	public void importFromPlace(String place, int year) {
		olcImportServiceAsync.importFromPlace(place, year, new AsyncCallback<List<FlightEntry>>() {

			@Override
			public void onSuccess(List<FlightEntry> result) {
				gui.service_eventImportFromPlaceSuccessful(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

}
