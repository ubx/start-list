package ch.ubx.startlist.server.admin;

import java.util.List;

import ch.ubx.startlist.client.admin.OlcImportService;
import ch.ubx.startlist.shared.FlightEntry;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class OlcImportImpl extends RemoteServiceServlet implements OlcImportService {

	private static final long serialVersionUID = 1L;

	@Override
	public List<FlightEntry> importFromPlace(String place, int year) {
		return OlcImport.importFromOLC(place, year, 5);
	}

}
