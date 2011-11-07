package ch.ubx.startlist.server;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ch.ubx.startlist.shared.FlightEntry;
import ch.ubx.startlist.shared.LoginInfo;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.utils.SystemProperty;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;

public class FlightEntryDAOobjectify extends DAOBase implements FlightEntryDAO {

	static {
		try { // TODO -- eliminate duplicate register error
			ObjectifyService.register(FlightEntry.class);
		} catch (Exception e) {
		}
		try { // TODO -- eliminate duplicate register error
			ObjectifyService.register(LoginInfo.class);
		} catch (Exception e) {
		}
	}

	private LoginInfo loginInfo = null;
	private UserService userService;
	private User adminUser;

	public FlightEntryDAOobjectify() {
		userService = UserServiceFactory.getUserService();
		adminUser = new User("admin.cron@" + SystemProperty.applicationId.get() + ".appspotmail.com", "gmail.com");
	}

	@Override
	public List<FlightEntry> listflightEntry() {
		return ofy().query(FlightEntry.class).list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ubx.startlist.server.X#listflightEntry(java.lang.String, long, int, int)
	 */
	@Override
	public List<FlightEntry> listflightEntry(String place, long dateTimeInMillis, int startIndex, int maxCount) {
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(dateTimeInMillis);
		List<FlightEntry> list = listflightEntry(date, place);
		// TODO - use Query methods limit/ offset
		list = list.subList(startIndex, Math.min(startIndex + maxCount, list.size()));
		for (FlightEntry flightEntry : list) {
			doPostLoad(flightEntry);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ubx.startlist.server.X#removeFlightEntry(ch.ubx.startlist.shared.FlightEntry)
	 */
	@Override
	public FlightEntry removeFlightEntry(FlightEntry flightEntry) {
		ofy().delete(flightEntry);
		return flightEntry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ubx.startlist.server.X#createOrUpdateFlightEntry(ch.ubx.startlist.shared.FlightEntry)
	 */

	@Override
	public FlightEntry createOrUpdateFlightEntry(FlightEntry flightEntry) {
		doPrePersist(flightEntry);
		ofy().put(flightEntry);
		return flightEntry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ubx.startlist.server.X#listDates(java.lang.String, int)
	 */
	@Override
	public Set<Long> listDates(String place, int year) {
		List<FlightEntry> list = listflightEntry(year, place);
		Set<Long> dateList = new TreeSet<Long>();
		Calendar ymd = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		for (FlightEntry flightEntry : list) {
			cal.setTimeInMillis(flightEntry.getStartTimeInMillis());
			ymd.setTimeInMillis(0);
			ymd.set(Calendar.YEAR, cal.get(Calendar.YEAR));
			ymd.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR));
			dateList.add(ymd.getTimeInMillis());
		}
		if (dateList.size() == 0) {
			dateList.add(ymd.getTime().getTime());
		}
		return dateList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ubx.startlist.server.X#listAirfields(int)
	 */
	@Override
	public Set<String> listAirfields(int year) {
		List<FlightEntry> list = listflightEntry(year);
		Set<String> places = new TreeSet<String>();
		for (FlightEntry flightEntry : list) {
			places.add(flightEntry.getPlace());
		}
		return places;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ubx.startlist.server.X#listYears()
	 */
	@Override
	public Set<Integer> listYears() {
		List<FlightEntry> list = ofy().query(FlightEntry.class).list();
		Set<Integer> years = new TreeSet<Integer>();
		Calendar cal = Calendar.getInstance();
		for (FlightEntry flightEntry : list) {
			cal.setTimeInMillis(flightEntry.getStartTimeInMillis());
			years.add(new Integer(cal.get(Calendar.YEAR)));
		}
		if (years.size() == 0) {
			years.add(cal.get(Calendar.YEAR));
		}
		return years;
	}

	// --------------------------------------------------------------------------------------
	// server internal access methods

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ubx.startlist.server.X#listflightEntry(int)
	 */
	@Override
	public List<FlightEntry> listflightEntry(int year) {
		Calendar yearStart = Calendar.getInstance();
		yearStart.setTimeInMillis(0);
		yearStart.set(Calendar.YEAR, year);

		Calendar yearEnd = Calendar.getInstance();
		yearEnd.setTimeInMillis(0);
		yearEnd.set(Calendar.YEAR, year + 1);

		Query<FlightEntry> query = ofy().query(FlightEntry.class).filter("startTimeInMillis >=", yearStart.getTimeInMillis())
				.filter("startTimeInMillis <", yearEnd.getTimeInMillis()).order("startTimeInMillis");

		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ubx.startlist.server.X#listflightEntry(java.util.Calendar, java.lang.String)
	 */
	@Override
	public List<FlightEntry> listflightEntry(Calendar date, String place) {
		Calendar dateStart = Calendar.getInstance(); // TODO - set timezone UTC?
		dateStart.setTimeInMillis(date.getTimeInMillis());
		dateStart.set(Calendar.HOUR_OF_DAY, 0);
		dateStart.set(Calendar.MINUTE, 0);
		dateStart.set(Calendar.SECOND, 0);
		dateStart.set(Calendar.MILLISECOND, 0);
		Calendar dateEnd = Calendar.getInstance(); // TODO - set timezone UTC?
		dateEnd.setTimeInMillis(dateStart.getTimeInMillis());
		dateEnd.add(Calendar.DAY_OF_MONTH, 1);

		Query<FlightEntry> query = ofy().query(FlightEntry.class).filter("place ==", place).filter("startTimeInMillis >=", dateStart.getTimeInMillis())
				.filter("startTimeInMillis <", dateEnd.getTimeInMillis()).order("startTimeInMillis");
		return query.list();
	}

	@Override
	public List<FlightEntry> listflightEntry(Calendar startDate, Calendar endDate, String place) {
		Calendar dateStart = Calendar.getInstance(); // TODO - set timezone UTC?
		dateStart.setTimeInMillis(startDate.getTimeInMillis());
		dateStart.set(Calendar.HOUR_OF_DAY, 0);
		dateStart.set(Calendar.MINUTE, 0);
		dateStart.set(Calendar.SECOND, 0);
		dateStart.set(Calendar.MILLISECOND, 0);

		Calendar dateEnd = Calendar.getInstance(); // TODO - set timezone UTC?
		dateEnd.setTimeInMillis(endDate.getTimeInMillis());
		Query<FlightEntry> query = ofy().query(FlightEntry.class).filter("place", place).filter("startTimeInMillis >=", dateStart.getTimeInMillis())
				.filter("startTimeInMillis <=", dateEnd.getTimeInMillis());
		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ubx.startlist.server.X#listflightEntry(int, int, int, java.lang.String)
	 */
	@Override
	public List<FlightEntry> listflightEntry(int year, int month, int day, String place) {
		Calendar dateStart = Calendar.getInstance(); // TODO - set timezone UTC?
		dateStart.setTimeInMillis(0);
		dateStart.set(Calendar.YEAR, year);
		dateStart.set(Calendar.MONTH, month);
		dateStart.set(Calendar.DAY_OF_MONTH, day);

		Calendar dateEnd = Calendar.getInstance(); // TODO - set timezone UTC?
		dateEnd.setTimeInMillis(0);
		dateEnd.set(Calendar.YEAR, year);
		dateEnd.set(Calendar.MONTH, month);
		dateEnd.set(Calendar.DAY_OF_MONTH, day);
		dateEnd.add(Calendar.DAY_OF_MONTH, 1);
		Query<FlightEntry> query = ofy().query(FlightEntry.class).filter("startTimeInMillis >=", dateStart.getTimeInMillis())
				.filter("startTimeInMillis <", dateEnd.getTimeInMillis()).filter("place ==", place).order("startTimeInMillis");
		return query.list();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ubx.startlist.server.X#listflightEntry(int, java.lang.String)
	 */
	@Override
	public List<FlightEntry> listflightEntry(int year, String place) {
		return listflightEntry(year, year, place);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ubx.startlist.server.X#listflightEntry(int, int, java.lang.String)
	 */
	@Override
	public List<FlightEntry> listflightEntry(int yearStart, int yearEnd, String place) {
		Calendar dateStart = Calendar.getInstance(); // TODO - set timezone UTC?
		dateStart.setTimeInMillis(0);
		dateStart.set(Calendar.YEAR, yearStart);
		dateStart.set(Calendar.DAY_OF_YEAR, 1);

		Calendar dateEnd = Calendar.getInstance(); // TODO - set timezone UTC?
		dateEnd.setTimeInMillis(0);
		dateEnd.set(Calendar.YEAR, yearEnd);
		dateEnd.set(Calendar.DAY_OF_YEAR, 1);
		dateEnd.add(Calendar.YEAR, 1);
		Query<FlightEntry> query = ofy().query(FlightEntry.class).filter("startTimeInMillis >=", dateStart.getTimeInMillis())
				.filter("startTimeInMillis <", dateEnd.getTimeInMillis()).filter("place ==", place).order("startTimeInMillis");
		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ubx.startlist.server.X#addFlightEntries(java.util.List)
	 */
	@Override
	public void addFlightEntries(List<FlightEntry> flightEntries) {
		if (flightEntries.size() > 0) {
			for (FlightEntry flightEntry : flightEntries) {
				doPrePersist(flightEntry);
			}
			ofy().put(flightEntries);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ubx.startlist.server.FlightEntryDAO#addFlightEntries4Test(java.util.List)
	 * 
	 * NOTE: this method should be used only for testing!
	 */
	@Override
	public void addFlightEntries4Test(List<FlightEntry> flightEntries) {
		// ofy().put(flightEntries);
		for (FlightEntry flightEntry : flightEntries) {
			ofy().put(flightEntry);
		}
	}

	// --------------------------------------------------------------------------------------
	// private methods

	private void doPostLoad(FlightEntry flightEntry) {
		if (userService.isUserLoggedIn()) {
			boolean deletable = false;
			boolean modifiable = false;
			if (userService.isUserAdmin()) {
				deletable = true;
				modifiable = true;
			} else {
				if (loginInfo == null) {
					loginInfo = ofy().find(LoginInfo.class, userService.getCurrentUser().getEmail());
				}
				if (loginInfo != null) {
					modifiable = loginInfo.isCanModFlightEntry() || userService.getCurrentUser().getEmail().equals(flightEntry.getCreator());
					deletable = loginInfo.isCanDelFlightEntry() || userService.getCurrentUser().getEmail().equals(flightEntry.getCreator());
				}
			}
			flightEntry.setModifiable(modifiable);
			flightEntry.setDeletable(deletable);
		}
	}

	private void doPrePersist(FlightEntry flightEntry) {
		User currentUser = userService.getCurrentUser();
		// If we are called from a cron job, we need to create one!
		// NOTE: for security reasons this should be done only if we running in a cron job!
		// TODO - find a way to implement
		if (currentUser == null) {
			currentUser = adminUser;
		}
		flightEntry.setModifier(currentUser.getEmail());
		flightEntry.setModified(System.currentTimeMillis());
		if (flightEntry.getId() == null) {
			flightEntry.setCreator(flightEntry.getModifier());
			flightEntry.setCreated(flightEntry.getModified());
		}
	}

}
