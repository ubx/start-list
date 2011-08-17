package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FeDate;

import com.googlecode.objectify.Key;

public interface FeDateDAO {

    public Key<FeDate> getOrCreateKey(long date, Key<FeDate> placeKey);

    public FeDate getOrCreate(long date, Key<FeDate> placeKey);

    public FeDate get(Key<FeDate> key);

    public void delete(Key<FeDate> key);

    public void delete(FeDate date);

    public List<FeDate> list();

    public List<FeDate> list(Key<FeDate> key);

}
