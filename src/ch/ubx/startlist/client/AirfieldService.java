package ch.ubx.startlist.client;

import java.util.Set;

import ch.ubx.startlist.shared.Airfield;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("airfields")
public interface AirfieldService extends RemoteService {

	public void addAirfield(Airfield airfield);
	
	public void addAirfields(Set<Airfield> airfields);	
	
	public Set<Airfield> getAirfields();

}
