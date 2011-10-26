package ch.ubx.startlist.server.maint;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.apphosting.api.DeadlineExceededException;

import ch.ubx.startlist.server.FlightEntryDAOobjectify2;
import ch.ubx.startlist.shared.FlightEntry;

public class Migrate {

    private static final Logger log = Logger.getLogger(Migrate.class.getName());
    private static final FlightEntryDAOobjectify2 dao2 = new FlightEntryDAOobjectify2();

    public static void migrate() {
        int cnt = 0;
        List<FlightEntry> flightEntries = dao2.listflightEntry();
        log.log(Level.INFO, "FlightEntry read: " + flightEntries.size());
        try {
            log.log(Level.INFO, "Start migration FlightEntries...");
            for (FlightEntry flightEntry : flightEntries) {
                if (flightEntry.getParent() == null) {
                    dao2.createOrUpdateFlightEntry(flightEntry);
                    cnt++;
                }
            }
            log.log(Level.INFO, "FlightEntry migration done(" + cnt + " of " + flightEntries.size() + " FlightEntries)");
        } catch (DeadlineExceededException e) {
            log.log(Level.INFO, "FlightEntry migration partly done(" + cnt + " of " + flightEntries.size() + " FlightEntries)");
        }
    }
}
