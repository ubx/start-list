package ch.ubx.startlist.server;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.ubx.startlist.server.admin.OlcImport;
import ch.ubx.startlist.shared.JobImportOLC;

public class OlcImporter {

	private static final Logger log = Logger.getLogger(OlcImporter.class.getName());

	/**
	 * Import for all ImportOLC from OLC web page;
	 */
	public static void execute(String name, JobImportOLC job) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		log.log(Level.INFO, "Import for job=" + name);
		List<String> places = job.getPlacesList();
		for (String place : places) {
			log.log(Level.INFO, "Import from place=" + place + ", year=" + year);
			// Split requests into small pieces to avoid DeadlineExceededException for the whole request.
			int maxImport = 50;
			log.log(Level.INFO, "Import...");
			while (OlcImport.importFromOLC(place, year, 20).size() > 0 & maxImport > 0) {
				log.log(Level.INFO, "Imported 20 ...");
				--maxImport;
			}
			log.log(Level.INFO, "...imported");

		}

	}

}
