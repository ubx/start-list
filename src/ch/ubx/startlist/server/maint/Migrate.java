package ch.ubx.startlist.server.maint;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.ubx.startlist.server.FlightEntryDAOobjectify2;
import ch.ubx.startlist.shared.FlightEntry;

public class Migrate {

    private static final Logger log = Logger.getLogger(Migrate.class.getName());
    private static final FlightEntryDAOobjectify2 dao2 = new FlightEntryDAOobjectify2();

    public static void migrate() {
        log.log(Level.INFO, "Start migration FlightEntries...");
        List<FlightEntry> flightEntries = dao2.listflightEntry();
        for (FlightEntry flightEntry : flightEntries) {
            if (flightEntry.getParent() == null) {
                dao2.createOrUpdateFlightEntry(flightEntry);
            }
        }
        log.log(Level.INFO, "FlightEntry migration done(" + flightEntries.size() + " FlightEntries)");

    }
}
