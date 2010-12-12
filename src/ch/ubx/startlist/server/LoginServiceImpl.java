package ch.ubx.startlist.server;

import ch.ubx.startlist.client.LoginInfo;
import ch.ubx.startlist.client.LoginService;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	static {
		ObjectifyService.register(LoginInfo.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public LoginInfo login(String requestUri) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		LoginInfo loginInfo;
		if (user != null) {
			Objectify ofy = ObjectifyService.begin();
			loginInfo = ofy.find(LoginInfo.class, user.getEmail());
			if (loginInfo == null) {
				loginInfo = new LoginInfo();
				loginInfo.setEmail(user.getEmail());
				// compose name from email
				String email = user.getEmail();
				String[] em = email.split("@");
				String[] fl = em[0].split("\\.");
				if (fl.length == 2) {
					loginInfo.setFirstName(capitalize(fl[0]));
					loginInfo.setLastName(capitalize(fl[1]));
				} else {
					loginInfo.setFirstName("");
					loginInfo.setLastName(em[0]);
				}
				// store
				ofy.put(loginInfo);
			}
			loginInfo.setLoggedIn(true);
			loginInfo.setNickname(user.getNickname());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));

			// do not store this!
			if (userService.isUserAdmin()) {
				loginInfo.setAdmin(true);
				loginInfo.setCanDelFlightEntry(true);
				loginInfo.setCanModFlightEntry(true);
				loginInfo.setCanModUser(true);
			}

		} else {
			loginInfo = new LoginInfo();
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return loginInfo;
	}

	public static String capitalize(String s) {
		if (s.length() == 0)
			return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

}
