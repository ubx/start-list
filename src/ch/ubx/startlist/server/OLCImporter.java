package ch.ubx.startlist.server;

import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.ubx.startlist.server.admin.OlcImportMain;
import ch.ubx.startlist.shared.ImportOLC;

public class OLCImporter {

	private static ImportOLCDAO importOLCDAO = new ImportOLCDAOobjectify();

	private static final Logger log = Logger.getLogger(OLCImporter.class.getName());

	/**
	 * Import for all ImportOLC from OLC web page;
	 */
	public static void doImport(List<String> names) {
		if (names.size() > 0) {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			Set<Entry<String, ImportOLC>> entrySet = importOLCDAO.listImportOLC(names).entrySet();
			for (Entry<String, ImportOLC> entry : entrySet) {
				log.log(Level.INFO, "Import for job=" + entry.getKey());
				List<String> places = entry.getValue().getPlacesList();
				for (String place : places) {
					log.log(Level.INFO, "Import from place=" + place + ", year=" + year);
					// Split requests into small pieces to avoid DeadlineExceededException for the whole request.
					int maxImport = 50;
					log.log(Level.INFO, "Import...");
					while (OlcImportMain.importFromOLC(place, year, 20).size() > 0 & maxImport > 0) {
						log.log(Level.INFO, "Imported 20 ...");
						--maxImport;
					}
					log.log(Level.INFO, "...imported");
				}
			}
		}
	}

}
