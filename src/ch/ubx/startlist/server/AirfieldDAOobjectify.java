package ch.ubx.startlist.server;

import java.util.HashSet;
import java.util.Set;

import ch.ubx.startlist.shared.Airfield;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class AirfieldDAOobjectify implements AirfieldDAO {

	static {
		ObjectifyService.register(Airfield.class);
	}

	@Override
	public void addAirfield(Airfield airfield) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(airfield);
	}

	@Override
	public void addAirfields(Set<Airfield> airfields) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(airfields);

	}

	@Override
	public Set<Airfield> getAirfields() {
		Objectify ofy = ObjectifyService.begin();
		Query<Airfield> query = ofy.query(Airfield.class);
		return new HashSet<Airfield>(query.list());
	}

	// --------------------------------------------------------------------------------------
	// server internal access methods

	public void deleteAllAirfields() {
		Objectify ofy = ObjectifyService.begin();
		Query<Airfield> query = ofy.query(Airfield.class);
		ofy.delete(query);
	}

	public Airfield getAirfield(String name) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.get(Airfield.class, name);
	}

}
