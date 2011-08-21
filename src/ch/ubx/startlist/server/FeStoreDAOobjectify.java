package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FeStore;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

public class FeStoreDAOobjectify extends DAOBase implements FeStoreDAO {

    static {
        ObjectifyService.register(FeStore.class);
    }

    @Override
    public Key<FeStore> getOrCreateKey(String name) {
        Key<FeStore> key = ofy().query(FeStore.class).filter("name", name).getKey();
        if (key == null) {
            key = ofy().put(new FeStore(name));
        }
        return key;
    }

    @Override
    public FeStore getOrCreate(String name) {
        FeStore elem = ofy().find(FeStore.class, name);
        if (elem == null) {
            elem = new FeStore(name);
            ofy().put(elem);
        }
        return elem;
    }

    @Override
    public FeStore get(Key<FeStore> key) {
        return ofy().get(key);
    }

    @Override
    public void delete(Key<FeStore> key) {
        ofy().delete(key);
    }

    @Override
    public void delete(FeStore feStore) {
        ofy().delete(feStore);
    }

    @Override
    public List<FeStore> list() {
        return ofy().query(FeStore.class).list();
    }

}
