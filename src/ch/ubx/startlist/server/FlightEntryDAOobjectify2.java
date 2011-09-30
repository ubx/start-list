package ch.ubx.startlist.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ch.ubx.startlist.shared.FeDate;
import ch.ubx.startlist.shared.FePlace;
import ch.ubx.startlist.shared.FeStore;
import ch.ubx.startlist.shared.FeYear;
import ch.ubx.startlist.shared.FlightEntry;
import ch.ubx.startlist.shared.LoginInfo;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.utils.SystemProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;

public class FlightEntryDAOobjectify2 extends DAOBase implements FlightEntryDAO2 {

    private static final String PARENT = "parent";

    static {
        try { // TODO -- eliminate duplicate register error
            ObjectifyService.register(FlightEntry.class);
            ObjectifyService.register(LoginInfo.class);
        } catch (Exception e) {
        }
    }

    private LoginInfo loginInfo = null;
    private UserService userService;
    private User adminUser;
    private final FeGenDAOobjectify<FeDate, FePlace, Long> dateDAO = new FeGenDAOobjectify<FeDate, FePlace, Long>(FeDate.class);
    private final FeGenDAOobjectify<FeStore, FeStore, String> storeDAO = new FeGenDAOobjectify<FeStore, FeStore, String>(
            FeStore.class);
    private final FeGenDAOobjectify<FeYear, FeStore, Long> yearDAO = new FeGenDAOobjectify<FeYear, FeStore, Long>(FeYear.class);
    private final FeGenDAOobjectify<FePlace, FeYear, String> placeDAO = new FeGenDAOobjectify<FePlace, FeYear, String>(
            FePlace.class);
    private static Key<FeStore> storeActiveKey;

    public FlightEntryDAOobjectify2() {
        userService = UserServiceFactory.getUserService();
        adminUser = new User("admin.cron@" + SystemProperty.applicationId.get() + ".appspotmail.com", "gmail.com");
        storeActiveKey = storeDAO.getOrCreateKey("Active");
    }

    @Override
    public List<FlightEntry> listflightEntry() {
        return ofy().query(FlightEntry.class).list();
    }

    @Override
    public List<FlightEntry> listflightEntry(FeDate date, int startIndex, int maxCount) {
        Query<FlightEntry> query = ofy().query(FlightEntry.class).filter(PARENT, date);
        List<FlightEntry> list = query.list();
        // TODO - use Query methods limit/ offset
        list = list.subList(startIndex, Math.min(startIndex + maxCount, list.size()));
        for (FlightEntry flightEntry : list) {
            doPostLoad(flightEntry);
        }
        return list;
    }

    @Override
    public List<FlightEntry> listflightEntry(FeDate date) {
        return listflightEntry(date, 0, 100000);
    }

    @Override
    public List<FlightEntry> listflightEntry(int yearInt) {
        List<FlightEntry> fes = new ArrayList<FlightEntry>();
        for (FePlace fePlace : listAirfield(yearInt)) {
            for (FeDate feDate : listDate(fePlace)) {
                fes.addAll(listflightEntry(feDate));
            }
        }
        return fes;
    }

    @Override
    public List<FlightEntry> listflightEntry(int startYearInt, int endYearInt, String placeStr) {
        List<FlightEntry> fes = new ArrayList<FlightEntry>();
        if (startYearInt <= endYearInt) {
            List<FePlace> ys = new ArrayList<FePlace>();
            for (int y = startYearInt; y <= endYearInt; y++) {
                ys.addAll(listAirfield(y));
            }
            List<FeDate> ds = new ArrayList<FeDate>();
            for (FePlace fePlace : ys) {
                if (fePlace.getValue().equals(placeStr)) {
                    ds.addAll(listDate(fePlace));
                }
            }
            for (FeDate feDate : ds) {
                fes.addAll(listflightEntry(feDate));
            }
        }
        return fes;
    }

    @Override
    public List<FlightEntry> listflightEntry(long startDateLong, long endDateLong, String placeStr) {
        List<FlightEntry> fes = new ArrayList<FlightEntry>();
        if (startDateLong <= endDateLong) {
            List<FeDate> ds = new ArrayList<FeDate>();
            for (FePlace fePlace : placeDAO.list()) {
                if (fePlace.getValue().equals(placeStr)) {
                    ds.addAll(dateDAO.list(startDateLong, endDateLong, fePlace));
                }
            }
            for (FeDate feDate : ds) {
                fes.addAll(listflightEntry(feDate));
            }
        }
        return fes;
    }

    @Override
    public List<FlightEntry> listflightEntry(int yearInt, int month, int day, String placeStr) {
        List<FlightEntry> fes = new ArrayList<FlightEntry>();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(0);
        date.set(Calendar.YEAR, yearInt);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, day);
        List<FeDate> ds = new ArrayList<FeDate>();
        for (FePlace fePlace : listAirfield(yearInt)) {
            if (fePlace.getValue().equals(placeStr)) {
                ds.addAll(listDate(fePlace));
            }
        }
        for (FeDate feDate : ds) {
            if (feDate.getValue() == date.getTimeInMillis()) {
                fes.addAll(listflightEntry(feDate));
            }
        }
        return fes;
    }

    @Override
    public FlightEntry removeFlightEntry(FlightEntry flightEntry) {
        Key<FeDate> dk = flightEntry.getParent();
        ofy().delete(flightEntry);
        flightEntry.setParent(null);
        Query<FlightEntry> query = ofy().query(FlightEntry.class).filter(PARENT, dk);
        if (query.list().size() == 0) {
            Key<FePlace> pk = dateDAO.get(dk).getParent();
            dateDAO.delete(dk);
            if (dateDAO.list(pk).size() == 0) {
                Key<FeYear> yk = placeDAO.get(pk).getParent();
                placeDAO.delete(pk);
                if (placeDAO.list(yk).size() == 0) {
                    yearDAO.delete(yk);
                }
            }
        }
        return flightEntry;
    }

    @Override
    public FlightEntry createOrUpdateFlightEntry(FlightEntry flightEntry) {
        if (flightEntry.getParent() != null) {
            removeFlightEntry(flightEntry);
        }
        doPrePersist(flightEntry);
        Key<FeYear> yearKey = yearDAO.getOrCreateKey(evalYear(flightEntry), storeActiveKey);
        Key<FePlace> placeKey = placeDAO.getOrCreateKey(evalPlace(flightEntry), yearKey);
        Key<FeDate> dateKey = dateDAO.getOrCreateKey(evalDate(flightEntry), placeKey);
        flightEntry.setParent(dateKey);
        ofy().put(flightEntry);
        return flightEntry;
    }

    @Override
    public List<FeDate> listDate(FePlace place) {
        return dateDAO.list(place);
    }

    @Override
    public List<FeDate> listDate(String placeStr, int yearInt) {
        List<FeDate> ds = new ArrayList<FeDate>();
        for (FePlace fePlace : listAirfield(yearDAO.get(new Long(yearInt), storeActiveKey))) {
            ds.addAll(listDate(fePlace));
        }
        return ds;
    }

    @Override
    public List<FePlace> listAirfield(FeYear year) {
        return placeDAO.list(year);
    }

    @Override
    public List<FePlace> listAirfield(int yearInt) {
        return listAirfield(yearDAO.get(new Long(yearInt), storeActiveKey));
    }

    @Override
    public List<FeYear> listYear() {
        return yearDAO.list(storeActiveKey);
    }

    /*
     * NOTE: this method should be used only for testing!
     */
    public void addFlightEntries4Test(List<FlightEntry> flightEntries) {
        for (FlightEntry flightEntry : flightEntries) {
            createOrUpdateFlightEntry(flightEntry);
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
                    modifiable = loginInfo.isCanModFlightEntry()
                            || userService.getCurrentUser().getEmail().equals(flightEntry.getCreator());
                    deletable = loginInfo.isCanDelFlightEntry()
                            || userService.getCurrentUser().getEmail().equals(flightEntry.getCreator());
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

    private Long evalYear(FlightEntry flightEntry) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(flightEntry.getStartTimeInMillis());
        return new Long(date.get(Calendar.YEAR));
    }

    private Long evalDate(FlightEntry flightEntry) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(flightEntry.getStartTimeInMillis());
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return new Long(date.getTimeInMillis());
    }

    private String evalPlace(FlightEntry flightEntry) {
        return flightEntry.getPlace();
    }

}
