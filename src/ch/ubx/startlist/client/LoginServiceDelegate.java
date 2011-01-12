package ch.ubx.startlist.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import ch.ubx.startlist.client.ui.FlightEntryListGUI;
import ch.ubx.startlist.shared.LoginInfo;

public class LoginServiceDelegate {
	private LoginServiceAsync loginService = GWT.create(LoginService.class);
	public FlightEntryListGUI gui;

	public void login(String user) {
		loginService.login(user, new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable caught) {
				gui.service_eventLoginFailed(caught);
			}

			@Override
			public void onSuccess(LoginInfo result) {
				gui.service_eventLoginSuccessful(result);
			}
		});
	}

}
