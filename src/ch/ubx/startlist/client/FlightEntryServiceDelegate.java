package ch.ubx.startlist.client;

import java.util.List;

import ch.ubx.startlist.client.ui.FlightEntryListGUI;
import ch.ubx.startlist.shared.FeDate;
import ch.ubx.startlist.shared.FeFlightEntry;
import ch.ubx.startlist.shared.FePlace;
import ch.ubx.startlist.shared.FeYear;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FlightEntryServiceDelegate {
    private FlightEntryServiceAsync flightEntryService = GWT.create(FlightEntryService.class);
    FlightEntryListGUI gui;

    public void createOrUpdateFlightEntry(final FeFlightEntry flightEntry) {
        flightEntryService.createOrUpdateFlightEntry(flightEntry, new AsyncCallback<FeFlightEntry>() {
            public void onFailure(Throwable caught) {
                gui.service_eventUpdateFlightEntryFailed(caught);
            }

            public void onSuccess(FeFlightEntry result) {
                gui.service_eventUpdateSuccessful(result);
            }
        });
    }

    public void removeFlightEntry(final FeFlightEntry flightEntry) {
        flightEntryService.removeFlightEntry(flightEntry, new AsyncCallback<FeFlightEntry>() {
            public void onFailure(Throwable caught) {
                gui.service_eventRemoveFlightEntryFailed(caught);
            }

            public void onSuccess(FeFlightEntry result) {
                gui.service_eventRemoveFlightEntrySuccessful(result);
            }
        });
    }

    public void listDate(final FePlace place) {
        flightEntryService.listDate(place, new AsyncCallback<List<FeDate>>() {
            public void onFailure(Throwable caught) {
                gui.service_eventListDatesFailed(caught);
            }

            public void onSuccess(List<FeDate> result) {
                gui.service_eventListDatesSuccessful(result);
            }

        });
    }

    public void listYears() {
        flightEntryService.listYear(new AsyncCallback<List<FeYear>>() {
            public void onFailure(Throwable caught) {
                gui.service_eventListYearsFailed(caught);
            }

            public void onSuccess(List<FeYear> result) {
                gui.service_eventListYearsSuccessful(result);

            }
        });
    }

    public void listPlaces(FeYear year) {
        flightEntryService.listPlace(year, new AsyncCallback<List<FePlace>>() {
            public void onFailure(Throwable caught) {
                gui.service_eventListPlacesFailed(caught);
            }

            public void onSuccess(List<FePlace> result) {
                gui.service_eventListPlacesSuccessful(result);

            }
        });
    }

}
