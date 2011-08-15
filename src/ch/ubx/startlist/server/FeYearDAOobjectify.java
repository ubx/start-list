package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FeStore;
import ch.ubx.startlist.shared.FeYear;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.helper.DAOBase;

public class FeYearDAOobjectify extends DAOBase implements FeYearDAO {

    static {
        ObjectifyService.register(FeYear.class);
    }

    private final static FeStoreDAO feStoreDAO = new FeStoreDAOobjectify();

    @Override
    public Key<FeYear> getOrCreateKey(long year, String storeName) {
        Key<FeStore> store = feStoreDAO.getOrCreateKey(storeName);
        Key<FeYear> key = ofy().query(FeYear.class).filter("year", year).filter("store", store).getKey();
        if (key == null) {
            key = ofy().put(new FeYear(year, store));
        }
        return key;
    }

    @Override
    public Key<FeYear> getOrCreateKey(long year, Key<FeStore> storeKey) {
        Key<FeYear> key = ofy().query(FeYear.class).filter("year", year).filter("store", storeKey).getKey();
        if (key == null) {
            key = ofy().put(new FeYear(year, storeKey));
        }
        return key;
    }

    @Override
    public FeYear getOrCreate(long year, String storeName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FeYear getOrCreate(long year, Key<FeStore> storeKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FeYear get(Key<FeYear> key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(Key<FeYear> key) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(FeYear year) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<FeYear> list() {
        return ofy().query(FeYear.class).list();
    }

    @Override
    public List<FeYear> list(String storeName) {
        return ofy().query(FeYear.class).filter("store", feStoreDAO.getOrCreateKey(storeName)).list();
    }

    @Override
    public List<FeYear> list(Key<FeStore> storeKey) {
        return ofy().query(FeYear.class).filter("store", storeKey).list();
    }

}
