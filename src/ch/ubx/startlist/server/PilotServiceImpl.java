package ch.ubx.startlist.server;

import java.util.Set;

import ch.ubx.startlist.client.PilotService;
import ch.ubx.startlist.shared.Pilot;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class PilotServiceImpl extends RemoteServiceServlet implements PilotService {

	private static final long serialVersionUID = 1L;
	private PilotDAO pilotDAO = new PilotDAOobjectify();

	@Override
	public void addPilot(Pilot pilot) {
		pilotDAO.addPilot(pilot);

	}

	@Override
	public Set<Pilot> getPilots() {
		return pilotDAO.getPilots();
	}

}
