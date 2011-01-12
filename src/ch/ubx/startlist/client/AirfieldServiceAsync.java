package ch.ubx.startlist.client;

import java.util.Set;

import ch.ubx.startlist.shared.Airfield;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AirfieldServiceAsync {

	void addAirfield(Airfield airfield, AsyncCallback<Void> callback);

	void getAirfields(AsyncCallback<Set<Airfield>> callback);

	void addAirfields(Set<Airfield> airfields, AsyncCallback<Void> callback);

}
