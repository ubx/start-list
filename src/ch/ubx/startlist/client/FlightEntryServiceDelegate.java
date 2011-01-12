package ch.ubx.startlist.client;

import java.util.Set;

import ch.ubx.startlist.client.ui.FlightEntryListGUI;
import ch.ubx.startlist.shared.FlightEntry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FlightEntryServiceDelegate {
	private FlightEntryServiceAsync flightEntryService = GWT.create(FlightEntryService.class);
	FlightEntryListGUI gui;


	public void createOrUpdateFlightEntry(final FlightEntry flightEntry) {
		flightEntryService.createOrUpdateFlightEntry(flightEntry, new AsyncCallback<FlightEntry>() {
			public void onFailure(Throwable caught) {
				gui.service_eventUpdateFlightEntryFailed(caught);
			}

			public void onSuccess(FlightEntry result) {
				gui.service_eventUpdateSuccessful(result);
			}
		});
	}

	public void removeFlightEntry(final FlightEntry flightEntry) {
		flightEntryService.removeFlightEntry(flightEntry, new AsyncCallback<FlightEntry>() {
			public void onFailure(Throwable caught) {
				gui.service_eventRemoveFlightEntryFailed(caught);
			}

			public void onSuccess(FlightEntry result) {
				gui.service_eventRemoveFlightEntrySuccessful(result);
			}
		});
	}

	public void listDates(final String place, final int year) {
		flightEntryService.listDates(place, year, new AsyncCallback<Set<Long>>() {
			public void onFailure(Throwable caught) {
				gui.service_eventListDatesFailed(caught);
			}

			public void onSuccess(Set<Long> result) {
				gui.service_eventListDatesSuccessful(result);

			}
		});
	}

	public void listYears() {
		flightEntryService.listYears(new AsyncCallback<Set<Integer>>() {
			public void onFailure(Throwable caught) {
				gui.service_eventListYearsFailed(caught);
			}

			public void onSuccess(Set<Integer> result) {
				gui.service_eventListYearsSuccessful(result);

			}
		});
	}

	public void listPlaces(int year) {
		flightEntryService.listPlaces(year, new AsyncCallback<Set<String>>() {
			public void onFailure(Throwable caught) {
				gui.service_eventListPlacesFailed(caught);
			}

			public void onSuccess(Set<String> result) {
				gui.service_eventListPlacesSuccessful(result);

			}
		});
	}

}
