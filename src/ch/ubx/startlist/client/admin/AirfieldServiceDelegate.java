package ch.ubx.startlist.client.admin;

import java.util.Set;

import ch.ubx.startlist.client.Airfield;
import ch.ubx.startlist.client.AirfieldService;
import ch.ubx.startlist.client.AirfieldServiceAsync;
import ch.ubx.startlist.client.admin.ui.AdminGUI;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AirfieldServiceDelegate {

	private AirfieldServiceAsync airfieldServiceAsync = GWT.create(AirfieldService.class);

	private AdminGUI gui;

	public AirfieldServiceDelegate(AdminGUI gui) {
		this.gui = gui;
	}

	public void addAirfields(final Set<Airfield> airfields) {
		airfieldServiceAsync.addAirfields(airfields, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub

			}

		});
	}

}
