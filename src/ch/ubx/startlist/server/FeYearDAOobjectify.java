package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FeStore;
import ch.ubx.startlist.shared.FeYear;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.helper.DAOBase;

// TODO: Auto-generated Javadoc
/**
 * The Class FeYearDAOobjectify.
 */
public class FeYearDAOobjectify extends DAOBase implements FeYearDAO {

    static {
        ObjectifyService.register(FeYear.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.ubx.startlist.server.FeYearDAO#getOrCreateKey(long, com.googlecode.objectify.Key)
     */
    @Override
    public Key<FeYear> getOrCreateKey(long year, Key<FeStore> storeKey) {
        Key<FeYear> key = ofy().query(FeYear.class).filter("year", year).filter("store", storeKey).getKey();
        if (key == null) {
            key = ofy().put(new FeYear(year, storeKey));
        }
        return key;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.ubx.startlist.server.FeYearDAO#getOrCreate(long, com.googlecode.objectify.Key)
     */
    @Override
    public FeYear getOrCreate(long year, Key<FeStore> storeKey) {
        FeYear elem = ofy().query(FeYear.class).filter("year", year).filter("store", storeKey).get();
        if (elem == null) {
            elem = new FeYear(year, storeKey);
            ofy().put(elem);
        }
        return elem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.ubx.startlist.server.FeYearDAO#get(com.googlecode.objectify.Key)
     */
    @Override
    public FeYear get(Key<FeYear> key) {
        return ofy().get(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.ubx.startlist.server.FeYearDAO#delete(com.googlecode.objectify.Key)
     */
    @Override
    public void delete(Key<FeYear> key) {
        ofy().delete(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.ubx.startlist.server.FeYearDAO#delete(ch.ubx.startlist.shared.FeYear)
     */
    @Override
    public void delete(FeYear year) {
        ofy().delete(year);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.ubx.startlist.server.FeYearDAO#list()
     */
    @Override
    public List<FeYear> list() {
        return ofy().query(FeYear.class).list();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.ubx.startlist.server.FeYearDAO#list(com.googlecode.objectify.Key)
     */
    @Override
    public List<FeYear> list(Key<FeStore> storeKey) {
        return ofy().query(FeYear.class).filter("store", storeKey).list();
    }

}
