package ch.ubx.startlist.server;

import java.util.Set;

import ch.ubx.startlist.client.AirfieldService;
import ch.ubx.startlist.shared.Airfield;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AirfieldServiceImpl extends RemoteServiceServlet implements AirfieldService {

	private static final long serialVersionUID = 1L;
	private AirfieldDAO airfieldDAO = new AirfieldDAOobjectify();

	@Override
	public void addAirfield(Airfield airfield) {
		airfieldDAO.addAirfield(airfield);

	}

	@Override
	public void addAirfields(Set<Airfield> airfields) {
		airfieldDAO.addAirfields(airfields);
	}

	@Override
	public Set<Airfield> getAirfields() {
		return airfieldDAO.getAirfields();
	}

}
