package ch.ubx.startlist.server;

import java.util.List;

import com.googlecode.objectify.Key;

import ch.ubx.startlist.shared.FeStore;

public interface FeStoreDAO {

    public Key<FeStore> getOrCreateKey(String name);

    public FeStore getOrCreate(String name);

    public FeStore get(Key<FeStore> key);

    public void delete(Key<FeStore> key);

    public void delete(FeStore feStore);

    public List<FeStore> list();
}
