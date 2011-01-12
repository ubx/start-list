package ch.ubx.startlist.server;

import java.util.Set;

import ch.ubx.startlist.shared.Airfield;

public interface AirfieldDAO {

	public void addAirfield(Airfield airfield);

	public void addAirfields(Set<Airfield> airfields);

	public Set<Airfield> getAirfields();

}
