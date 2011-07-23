package ch.ubx.startlist.server;

import java.util.HashSet;
import java.util.Set;

import ch.ubx.startlist.shared.Airfield;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.helper.DAOBase;

public class AirfieldDAOobjectify extends DAOBase implements AirfieldDAO {

	static {
		ObjectifyService.register(Airfield.class);
	}

	@Override
	public void addAirfield(Airfield airfield) {
		ofy().put(airfield);
	}

	@Override
	public void addAirfields(Set<Airfield> airfields) {
		ofy().put(airfields);

	}

	@Override
	public Set<Airfield> getAirfields() {
		Query<Airfield> query = ofy().query(Airfield.class);
		return new HashSet<Airfield>(query.list());
	}

	// --------------------------------------------------------------------------------------
	// server internal access methods

	public void deleteAllAirfields() {
		Query<Airfield> query = ofy().query(Airfield.class);
		ofy().delete(query);
	}

	public Airfield getAirfield(String name) {
		return ofy().get(Airfield.class, name);
	}

}
