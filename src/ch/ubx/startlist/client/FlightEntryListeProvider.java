package ch.ubx.startlist.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A data provider that bridges the provides row level updates from the data available through a <@link SchoolCalendarService>.
 */
public class FlightEntryListeProvider implements DynaTableDataProvider, TimeFormat {

	private final static int COL_PILOT = 0;
	private final static int COL_AIRCRAFT = 1;
	private final static int COL_DEP = 2;
	private final static int COL_TO_TIME = 3;
	private final static int COL_DST = 4;
	private final static int COL_LDG_TIME = 5;
	private final static int COL_DURATION = 6;
	private final static int COL_TRAINING = 7;
	private final static int COL_REMARKS = 8;
	private final static int COLS = 9;

	private FlightEntryServiceAsync flightEntryService;

	private int lastMaxRows = -1;

	private List<FlightEntry> flightEntryList;

	private int lastStartRow = -1;

	private String currentPlace;

	private long currentDateInMillies;

	public FlightEntryListeProvider() {
		// Initialize the service.
		//
		flightEntryService = GWT.create(FlightEntryService.class);
		clearCurrentDate();
		clearCurrentPlace();
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
		flightEntryService.listFlightEntries(currentPlace, currentDateInMillies, startRow, maxRows, new AsyncCallback<List<FlightEntry>>() {
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
			rows[i] = new String[COLS];
			for (int j = 0; j < rows[i].length; j++) {
				rows[i][j] = null;
			}
			FlightEntry flightEntry = flightEntryList.get(i);
			rows[i][COL_PILOT] = flightEntry.getPilot();
			Date date = new Date();
			if (flightEntry.isStartTimeValid()) {
				date.setTime(flightEntry.getStartTimeInMillis());
				rows[i][COL_TO_TIME] = TIME_FORMAT.format(date);
			}

			if (flightEntry.isEndTimeValid()) {
				date.setTime(flightEntry.getEndTimeInMillis());
				rows[i][COL_LDG_TIME] = TIME_FORMAT.format(date);
			}

			if (flightEntry.isStartTimeValid() && flightEntry.isEndTimeValid()) {
				long mins = (flightEntry.getEndTimeInMillis() - flightEntry.getStartTimeInMillis()) / 60000;
				String minsStr = String.valueOf(mins % 60);
				String minsStrx = minsStr.length() == 1 ? "0" + minsStr : minsStr;
				rows[i][COL_DURATION] = String.valueOf(mins / 60) + ":" + minsStrx;
			}
			rows[i][COL_TRAINING] = flightEntry.isTraining() ? "J" : "N";
			rows[i][COL_REMARKS] = flightEntry.getRemarks();
			rows[i][COL_AIRCRAFT] = flightEntry.getPlane();
			rows[i][COL_DEP] = flightEntry.getPlace();
			rows[i][COL_DST] = flightEntry.getLandingPlace();
		}
		acceptor.accept(startRow, rows);
	}

	public void setCurrentPlace(String place) {
		currentPlace = place;
	}

	public void clearCurrentPlace() {
		currentPlace = null;
	}

	public long getCurrentDate() {
		return currentDateInMillies;
	}

	public void setCurrentDate(long date) {
		currentDateInMillies = date;
	}

	public void clearCurrentDate() {
		currentDateInMillies = 0;
	}

	public void reset() {
		lastMaxRows = -1;
		lastStartRow = -1;
	}

	public FlightEntry getFlightEntry(int row) {
		return flightEntryList.size() > row ? flightEntryList.get(row) : null; // TODO - fin a better solution !
	}

}
