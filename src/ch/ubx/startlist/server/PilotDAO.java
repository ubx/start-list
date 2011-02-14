package ch.ubx.startlist.server;

import java.util.Set;

import ch.ubx.startlist.shared.Pilot;

public interface PilotDAO {

	public void addPilot(Pilot pilot);

	public Set<Pilot> getPilots();

}
