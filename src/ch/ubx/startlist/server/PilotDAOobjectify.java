package ch.ubx.startlist.server;

import java.util.HashSet;
import java.util.Set;

import ch.ubx.startlist.shared.Pilot;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class PilotDAOobjectify implements PilotDAO {

	static {
		ObjectifyService.register(Pilot.class);
	}

	@Override
	public void addPilot(Pilot pilot) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(pilot);
	}

	@Override
	public Set<Pilot> getPilots() {
		Objectify ofy = ObjectifyService.begin();
		Query<Pilot> query = ofy.query(Pilot.class);
		return new HashSet<Pilot>(query.list());
	}

	// --------------------------------------------------------------------------------------
	// server internal access methods

	// TODO is this necessary?
	public void deleteAllPilots() {
		Objectify ofy = ObjectifyService.begin();
		Query<Pilot> query = ofy.query(Pilot.class);
		ofy.delete(query);
	}

	// TODO is this necessary?
	public Pilot getPilot(String name) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.get(Pilot.class, name);
	}

}
