package ch.ubx.startlist.client;

import java.util.Set;

import ch.ubx.startlist.shared.Pilot;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("pilots")
public interface PilotService extends RemoteService {

	public void addPilot(Pilot pilot);
	
	public Set<Pilot> getPilots();

}
