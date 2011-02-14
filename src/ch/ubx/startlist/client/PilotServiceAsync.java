package ch.ubx.startlist.client;

import java.util.Set;

import ch.ubx.startlist.shared.Pilot;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PilotServiceAsync {

	void addPilot(Pilot pilot, AsyncCallback<Void> callback);

	void getPilots(AsyncCallback<Set<Pilot>> callback);

}
