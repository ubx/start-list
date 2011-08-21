package ch.ubx.startlist.server;

import java.util.HashSet;
import java.util.Set;

import ch.ubx.startlist.shared.Pilot;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;

public class PilotDAOobjectify extends DAOBase implements PilotDAO {

	static {
		ObjectifyService.register(Pilot.class);
	}

	@Override
	public void addPilot(Pilot pilot) {
		ofy().put(pilot);
	}

	@Override
	public Set<Pilot> getPilots() {
		Query<Pilot> query = ofy().query(Pilot.class);
		return new HashSet<Pilot>(query.list());
	}

	// --------------------------------------------------------------------------------------
	// server internal access methods

	// TODO is this necessary?
	public void deleteAllPilots() {
		Query<Pilot> query = ofy().query(Pilot.class);
		ofy().delete(query);
	}

	// TODO is this necessary?
	public Pilot getPilot(String name) {
		return ofy().get(Pilot.class, name);
	}

}
