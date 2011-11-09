package ch.ubx.startlist.server.maint;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.ubx.startlist.server.FlightEntryDAOobjectify;
import ch.ubx.startlist.server.FlightEntryDAOobjectify2;
import ch.ubx.startlist.shared.FeFlightEntry;
import ch.ubx.startlist.shared.FlightEntry;

import com.google.apphosting.api.DeadlineExceededException;

public class Migrate {

	private static final Logger log = Logger.getLogger(Migrate.class.getName());
	private static final FlightEntryDAOobjectify dao = new FlightEntryDAOobjectify();
	private static final FlightEntryDAOobjectify2 dao2 = new FlightEntryDAOobjectify2();

	public static void migrate() {
		int cnt = 0;
		List<FlightEntry> flightEntries = dao.listflightEntry();
		log.log(Level.INFO, "FlightEntry read: " + flightEntries.size());
		try {
			log.log(Level.INFO, "Start migration FlightEntries...");
			for (FlightEntry flightEntry : flightEntries) {
				if (flightEntry.getParent() == null) {
					dao2.createOrUpdateFlightEntry(copy(flightEntry));
					dao.removeFlightEntry(flightEntry);
					cnt++;
				}
			}
			log.log(Level.INFO, "FlightEntry migration done(" + cnt + " of " + flightEntries.size() + " FlightEntries)");
		} catch (DeadlineExceededException e) {
			log.log(Level.INFO, "FlightEntry migration partly done(" + cnt + " of " + flightEntries.size() + " FlightEntries)");
		}
	}

	private static FeFlightEntry copy(FlightEntry fe) {
		FeFlightEntry feFe = new FeFlightEntry();
		// feFe.setId(fe.getId());
		feFe.setStartTimeValid(fe.isStartTimeValid());
		feFe.setStartTimeInMillis(fe.getStartTimeInMillis());

		feFe.setEndTimeGliderValid(fe.isEndTimeGliderValid());
		feFe.setEndTimeGliderInMillis(fe.getEndTimeGliderInMillis());

		feFe.setEndTimeTowplaneValid(fe.isEndTimeTowplaneValid());
		feFe.setEndTimeTowplaneInMillis(fe.getEndTimeTowplaneInMillis());

		feFe.setPilot(fe.getPilot());
		feFe.setPassengerOrInstructor(fe.getPassengerOrInstructor());
		feFe.setTowplanePilot(fe.getTowplanePilot());
		feFe.setPlace(fe.getPlace());
		feFe.setLandingPlace(fe.getLandingPlace());
		feFe.setRegistrationGlider(fe.getRegistrationGlider());
		feFe.setRegistrationTowplane(fe.getRegistrationTowplane());
		feFe.setTraining(fe.isTraining());
		feFe.setRemarks(fe.getRemarks());
		feFe.setModifiable(fe.isModifiable());
		feFe.setDeletable(fe.isDeletable());
		feFe.setCreated(fe.getCreated());
		feFe.setCreator(fe.getCreator());
		feFe.setModified(fe.getModified());
		feFe.setModifier(fe.getModifier());
		feFe.setClub(fe.getClub());
		feFe.setCompetitionID(fe.getCompetitionID());
		feFe.setSource(fe.getSource());
		return feFe;
	}

}
