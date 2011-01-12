package ch.ubx.startlist.shared;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Transient;

public class LoginInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String email;
	private String firstName;
	private String lastName;
	private boolean canDelFlightEntry;
	private boolean canModFlightEntry;
	private boolean canModUser;
	private String homeAirfield;

	public boolean isCanDelFlightEntry() {
		return canDelFlightEntry;
	}

	public void setCanDelFlightEntry(boolean canDelFlightEntry) {
		this.canDelFlightEntry = canDelFlightEntry;
	}

	public boolean isCanModFlightEntry() {
		return canModFlightEntry;
	}

	public void setCanModFlightEntry(boolean canModFlightEntry) {
		this.canModFlightEntry = canModFlightEntry;
	}

	public boolean isCanModUser() {
		return canModUser;
	}

	public void setCanModUser(boolean canModUser) {
		this.canModUser = canModUser;
	}

	@Transient
	private boolean loggedIn = false;

	@Transient
	private boolean admin = false;

	@Transient
	private String nickname;

	@Transient
	private String loginUrl;

	@Transient
	private String logoutUrl;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isAdmin() {
		return admin;
	}

	public String getHomeAirfield() {
		return homeAirfield;
	}

	public void setHomeAirfield(String homeAirfield) {
		this.homeAirfield = homeAirfield;
	}

}
