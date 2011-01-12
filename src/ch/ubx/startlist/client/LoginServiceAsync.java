package ch.ubx.startlist.client;

import ch.ubx.startlist.shared.LoginInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {

	void login(String requestUri, AsyncCallback<LoginInfo> callback);

}
