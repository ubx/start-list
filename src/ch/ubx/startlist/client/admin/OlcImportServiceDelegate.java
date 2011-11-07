package ch.ubx.startlist.client.admin;

import java.util.List;

import ch.ubx.startlist.client.admin.ui.AdminGUI;
import ch.ubx.startlist.shared.FeFlightEntry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OlcImportServiceDelegate {
	
	private OlcImportServiceAsync olcImportServiceAsync = GWT.create(OlcImportService.class);

	private AdminGUI gui;

	public OlcImportServiceDelegate(AdminGUI gui) {
		this.gui = gui;
	}

	public void importFromPlace(String place, int year) {
		olcImportServiceAsync.importFromPlace(place, year, new AsyncCallback<List<FeFlightEntry>>() {

			@Override
			public void onSuccess(List<FeFlightEntry> result) {
				gui.service_eventImportFromPlaceSuccessful(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

}
