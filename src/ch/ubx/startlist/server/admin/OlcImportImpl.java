package ch.ubx.startlist.server.admin;

import java.util.List;

import ch.ubx.startlist.client.FlightEntry;
import ch.ubx.startlist.client.admin.OlcImportService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class OlcImportImpl extends RemoteServiceServlet implements OlcImportService {

	private static final long serialVersionUID = 1L;

	@Override
	public List<FlightEntry> importFromPlace(String place, int year) {
		return OlcImportMain.importFromOLC(place, year, 5);
	}

}
