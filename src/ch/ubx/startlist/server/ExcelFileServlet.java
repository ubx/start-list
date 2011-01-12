package ch.ubx.startlist.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.ubx.startlist.shared.FlightEntry;

public class ExcelFileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private FlightEntryDAOobjectify flightEntryDAO = new FlightEntryDAOobjectify();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pathInfo = req.getPathInfo();

		int year = 0;
		int month = -1;
		int day = -1;
		String place = "";

		List<FlightEntry> flightEnties = null;

		String[] params = pathInfo.split("/");
		year = Integer.parseInt(params[1]);
		if (params.length == 2) {
			year = Integer.parseInt(params[1]);
			flightEnties = flightEntryDAO.listflightEntry(year);
		} else if (params.length == 3) {
			// flightEnties = flightEntryDAO.listflightEntry(Integer.parseInt(params[1]), Integer.parseInt(params[2])); // TODO - what for is this?
			// month
		} else if (params.length == 5) {
			year = Integer.parseInt(params[1]);
			month = Integer.parseInt(params[2]);
			day = Integer.parseInt(params[3]);
			place = params[4];
			flightEnties = flightEntryDAO.listflightEntry(year, month, day, place);
		} else {
			return;
		}

		if (flightEnties != null) {
			String sheetname = String.valueOf(year) + (month == -1 ? "" : String.format("%02d", month + 1)) + (day == -1 ? "" : String.format("%02d", day))
					+ "-" + place.replace(" ", "_");
			String filename = sheetname + ".xls";
			resp.setContentType("application/ms-excel");
			resp.setHeader("Content-Disposition", "attachment; filename=" + filename);
			ExcelSheet.createExcel(flightEnties, resp.getOutputStream(), sheetname);
		}
	}

}
