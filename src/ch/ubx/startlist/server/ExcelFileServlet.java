package ch.ubx.startlist.server;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import ch.ubx.startlist.client.FlightEntry;
import ch.ubx.startlist.client.TextConstants;

public class ExcelFileServlet extends HttpServlet implements TextConstants {

	private static final long serialVersionUID = 1L;

	private static final String[] columns = new String[] { TXT_DATE, TXT_START_PLACE, TXT_START_TIME + "[UTC]", TXT_LANDING_TIME_TOWPLANE + "[UTC]", TXT_DURATION_TOWPLANE, TXT_SHORT_REGISTRATION_TOWPLANE, 
		                                                   TXT_LANDING_PLACE, TXT_LANDING_TIME_GLIDER + "[UTC]", TXT_DURATION_GLIDER, TXT_SHORT_REGISTRATION_GLIDER, TXT_PILOT, TXT_PASSENGER_OR_INSTRUCTOR, TXT_TRAINING, TXT_REMARKS };

	private static final int[] columnswidth = new int[] { 10, 18, 18, 18, 10, 18, 18, 18, 10, 18, 20, 20, 6, 60 };

	private static final String TXT_COLUMN_LANDING_TIME_GLIDER = "H";
	private static final String TXT_COLUMN_LANDING_TIME_TOWPLANE = "D";
	private static final String TXT_COLUMN_START_TIME = "C";

	// TODO - the commented code below seem not to work here as expected, therefore it is put into doGet

	// private static final WritableFont times12font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD, false);
	// private static final WritableCellFormat cellHeaderFormat = new WritableCellFormat(times12font);
	// private static final WritableCellFormat cellDateFormat = new WritableCellFormat(new DateFormat("dd.MM"));
	// private static final WritableCellFormat cellTimeFormat = new WritableCellFormat(new DateFormat("hh:mm"));
	// static {
	// try {
	// cellDateFormat.setAlignment(Alignment.LEFT);
	// cellTimeFormat.setAlignment(Alignment.LEFT);
	// } catch (WriteException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	private FlightEntryDAOobjectify flightEntryDAO = new FlightEntryDAOobjectify();

	public ExcelFileServlet() {
		super();
		if (columns.length != columnswidth.length) {
			throw new RuntimeException("columns text array and width array have a different length");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pathInfo = req.getPathInfo();

		int year = 0;
		int month = -1;
		int day = -1;
		String place = "";

		List<FlightEntry> flightEnties = null;
		{
			String[] params = pathInfo.split("/");
			year = Integer.parseInt(params[1]);
			if (params.length == 2) {
				year = Integer.parseInt(params[1]);
				flightEnties = flightEntryDAO.listflightEntry(year);
			} else if (params.length == 3) {

				// flightEnties = flightEntryDAO.listflightEntry(Integer.parseInt(params[1]), Integer.parseInt(params[2])); // TODO - month
			} else if (params.length == 5) {
				year = Integer.parseInt(params[1]);
				month = Integer.parseInt(params[2]);
				day = Integer.parseInt(params[3]);
				place = params[4];
				flightEnties = flightEntryDAO.listflightEntry(year, month, day, place);
			} else {
				return;
			}
		}

		String filename = String.valueOf(year) + (month == -1 ? "" : String.valueOf(month + 1)) + (day == -1 ? "" : String.valueOf(day))
				+ place.replace(" ", "_") + ".xls";
		resp.setContentType("application/ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=" + filename);

		final WritableWorkbook workbook = Workbook.createWorkbook(resp.getOutputStream());

		final WritableSheet sheet = workbook.createSheet(String.valueOf(year), 0);

		final WritableFont times12font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD, false);
		final WritableCellFormat cellHeaderFormat = new WritableCellFormat(times12font);
		final WritableCellFormat cellDateFormat = new WritableCellFormat(new DateFormat("dd.MM"));
		final WritableCellFormat cellTimeFormat = new WritableCellFormat(new DateFormat("hh:mm"));
		try {
			cellDateFormat.setAlignment(Alignment.LEFT);
			cellTimeFormat.setAlignment(Alignment.LEFT);

			// write header
			int col = 0;
			int row = 0;
			for (String labelText : columns) {
				sheet.setColumnView(col, columnswidth[col]);
				sheet.addCell(new Label(col++, row, labelText, cellHeaderFormat));
			}
			row++;
			sheet.getSettings().setVerticalFreeze(row);

			// write rows
			for (FlightEntry flightEntry : flightEnties) {
				
				col = 0;

				// column A: date
				Date time = new Date(flightEntry.getStartTimeInMillis());
				sheet.addCell(new DateTime(col++, row, time, cellDateFormat));
				
				// column B: start place
				sheet.addCell(new Label(col++, row, flightEntry.getPlace()));
				
				// column C: start time
				if (!flightEntry.isStartTimeValid()) {
					time.setTime(0);
				}
				sheet.addCell(new DateTime(col++, row, time, cellTimeFormat));
				
				// column D: end time towplane
				if (flightEntry.isEndTimeTowplaneValid()) {
					time.setTime(flightEntry.getEndTimeTowplaneInMillis());
				} else {
					time.setTime(0);
				}
				sheet.addCell(new DateTime(col++, row, time, cellTimeFormat));
				
				// column E: tow duration: let excel calculate it
				String durationFormulaTowplane = TXT_COLUMN_LANDING_TIME_TOWPLANE + (row + 1) + "-" + TXT_COLUMN_START_TIME + (row + 1);
				sheet.addCell(new Formula(col++, row, durationFormulaTowplane, cellTimeFormat));
				
				// column F: registration towplane
				sheet.addCell(new Label(col++, row, flightEntry.getRegistrationTowplane()));
				
				// column G: landing place
				sheet.addCell(new Label(col++, row, flightEntry.getLandingPlace()));
				
				// column H: end time glider
				if (flightEntry.isEndTimeGliderValid()) {
					time.setTime(flightEntry.getEndTimeGliderInMillis());
				} else {
					time.setTime(0);
				}
				sheet.addCell(new DateTime(col++, row, time, cellTimeFormat));
				
				// column I: glider duration: let excel calculate it
				String durationFormulaGlider = TXT_COLUMN_LANDING_TIME_GLIDER + (row + 1) + "-" + TXT_COLUMN_START_TIME + (row + 1);
				sheet.addCell(new Formula(col++, row, durationFormulaGlider, cellTimeFormat));
				
				// column J: registration glider
				sheet.addCell(new Label(col++, row, flightEntry.getRegistrationGlider()));
				
				// column K: name of pilot
				sheet.addCell(new Label(col++, row, flightEntry.getPilot()));
				
				// column L: name of passenger or instructor
				sheet.addCell(new Label(col++, row, flightEntry.getPassengerOrInstructor()));

				// column M: training or not?
				sheet.addCell(new Label(col++, row, flightEntry.isTraining() ? TXT_Y : TXT_N));

				// column N: remarks
				sheet.addCell(new Label(col++, row, flightEntry.getRemarks()));

				row++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// All sheets and cells added. Now write out the workbook
		workbook.write();
		try {
			workbook.close();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
