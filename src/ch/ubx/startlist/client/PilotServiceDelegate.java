package ch.ubx.startlist.client;

import java.util.Set;

import ch.ubx.startlist.client.ui.FlightEntryListGUI;
import ch.ubx.startlist.shared.Pilot;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PilotServiceDelegate {

	private FlightEntryListGUI gui;

	public PilotServiceDelegate(FlightEntryListGUI gui) {
		super();
		this.gui = gui;
	}

	private PilotServiceAsync pilotServiceAsync = GWT.create(PilotService.class);

	public void listPilots() {
		pilotServiceAsync.getPilots(new AsyncCallback<Set<Pilot>>() {
			public void onFailure(Throwable caught) {
				System.out.println("Error loading pilot names");
			}

			public void onSuccess(Set<Pilot> result) {
				gui.service_eventAllListPilotsSuccessful(result);
			}
		});
	}

}
