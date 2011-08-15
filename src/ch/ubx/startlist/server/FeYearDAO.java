package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FeStore;
import ch.ubx.startlist.shared.FeYear;

import com.googlecode.objectify.Key;

public interface FeYearDAO {

    public Key<FeYear> getOrCreateKey(long year, String storeName);

    public Key<FeYear> getOrCreateKey(long year, Key<FeStore> storeKey);

    public FeYear getOrCreate(long year, String storeName);

    public FeYear getOrCreate(long year, Key<FeStore> storeKey);

    public FeYear get(Key<FeYear> key);

    public void delete(Key<FeYear> key);

    public void delete(FeYear year);

    public List<FeYear> list();

    public List<FeYear> list(String storeName);

    public List<FeYear> list(Key<FeStore> storeKey);

}
