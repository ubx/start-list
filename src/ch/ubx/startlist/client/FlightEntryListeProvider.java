package ch.ubx.startlist.client;

import java.util.Date;
import java.util.List;

import ch.ubx.startlist.shared.FeDate;
import ch.ubx.startlist.shared.FlightEntry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A data provider that bridges the provides row level updates from the data available through a <@link SchoolCalendarService>.
 */
public class FlightEntryListeProvider implements DynaTableDataProvider, TimeFormat {

    private final static int COL_START_TIME = 0;
    private final static int COL_LDG_TIME_TOWPLANE = 1;
    private final static int COL_DURATION_TOWPLANE = 2;
    private final static int COL_REGISTRATION_TOWPLANE = 3;
    private final static int COL_LDG_TIME_GLIDER = 4;
    private final static int COL_DURATION_GLIDER = 5;
    private final static int COL_REGISTRATION_GLIDER = 6;
    private final static int COL_PILOT = 7;
    private final static int COL_PASSENGER_OR_INSTRUCTOR = 8;
    private final static int COL_TRAINING = 9;
    private final static int COL_REMARKS = 10;
    private final static int COLS = 11;

    private FlightEntryServiceAsync flightEntryService;

    private int lastMaxRows = -1;

    private List<FlightEntry> flightEntryList;

    private int lastStartRow = -1;

    private FeDate currentDate;

    public FlightEntryListeProvider() {
        // Initialize the service.
        //
        flightEntryService = GWT.create(FlightEntryService.class);
        clearCurrentDate();
    }

    public void updateRowData(final int startRow, final int maxRows, final RowDataAcceptor acceptor) {
        // Check the simple cache first.
        //
        if (startRow == lastStartRow) {
            if (maxRows == lastMaxRows) {
                // Use the cached batch.
                //
                pushResults(acceptor, startRow, flightEntryList);
                return;
            }
        }

        // Fetch the data remotely.
        //
        flightEntryService.listFlightEntrie(currentDate, startRow, maxRows, new AsyncCallback<List<FlightEntry>>() {
            public void onFailure(Throwable caught) {
                acceptor.failed(caught);
            }

            public void onSuccess(List<FlightEntry> result) {
                lastStartRow = startRow;
                lastMaxRows = maxRows;
                flightEntryList = result;
                pushResults(acceptor, startRow, result);
            }

        });
    }

    private void pushResults(RowDataAcceptor acceptor, int startRow, List<FlightEntry> flightEntryList) {
        String[][] rows = new String[flightEntryList.size()][];
        for (int i = 0; i < rows.length; i++) {
            // init data structures
            rows[i] = new String[COLS];
            for (int j = 0; j < rows[i].length; j++) {
                rows[i][j] = null;
            }
            FlightEntry flightEntry = flightEntryList.get(i);

            // fill in data column per column
            Date date = new Date();
            if (flightEntry.isStartTimeValid()) {
                date.setTime(flightEntry.getStartTimeInMillis());
                rows[i][COL_START_TIME] = TIME_FORMAT_TABLE.format(date);
            }

            if (flightEntry.isEndTimeTowplaneValid()) {
                date.setTime(flightEntry.getEndTimeTowplaneInMillis());
                rows[i][COL_LDG_TIME_TOWPLANE] = TIME_FORMAT_TABLE.format(date);
            }

            if (flightEntry.isStartTimeValid() && flightEntry.isEndTimeTowplaneValid()) {
                long mins = (flightEntry.getEndTimeTowplaneInMillis() - flightEntry.getStartTimeInMillis()) / 60000;
                String minsStr = String.valueOf(mins % 60);
                String minsStrx = minsStr.length() == 1 ? "0" + minsStr : minsStr;
                rows[i][COL_DURATION_TOWPLANE] = String.valueOf(mins / 60) + ":" + minsStrx;
            }

            rows[i][COL_REGISTRATION_TOWPLANE] = flightEntry.getRegistrationTowplane();

            if (flightEntry.isEndTimeGliderValid()) {
                date.setTime(flightEntry.getEndTimeGliderInMillis());
                rows[i][COL_LDG_TIME_GLIDER] = TIME_FORMAT_TABLE.format(date);
            }

            if (flightEntry.isStartTimeValid() && flightEntry.isEndTimeGliderValid()) {
                long mins = (flightEntry.getEndTimeGliderInMillis() - flightEntry.getStartTimeInMillis()) / 60000;
                String minsStr = String.valueOf(mins % 60);
                String minsStrx = minsStr.length() == 1 ? "0" + minsStr : minsStr;
                rows[i][COL_DURATION_GLIDER] = String.valueOf(mins / 60) + ":" + minsStrx;
            }

            rows[i][COL_REGISTRATION_GLIDER] = flightEntry.getRegistrationGlider();

            rows[i][COL_PILOT] = flightEntry.getPilot();

            rows[i][COL_PASSENGER_OR_INSTRUCTOR] = flightEntry.getPassengerOrInstructor();

            rows[i][COL_TRAINING] = flightEntry.isTraining() ? "J" : "N";

            rows[i][COL_REMARKS] = flightEntry.getRemarks();

        }
        acceptor.accept(startRow, rows);
    }

    public void setCurrentDate(FeDate date) {
        currentDate = date;
    }

    public void clearCurrentDate() {
        currentDate = null;
    }

    public void reset() {
        lastMaxRows = -1;
        lastStartRow = -1;
    }

    public FlightEntry getFlightEntry(int row) {
        return flightEntryList.size() > row ? flightEntryList.get(row) : null; // TODO - fin a better solution !
    }

}
