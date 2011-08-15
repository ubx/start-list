package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FeDate;

import com.googlecode.objectify.Key;

public interface FeDateDAO {

    public Key<FeDate> getOrCreateKey(long date, String placeName);

    public FeDate getOrCreate(long date, String placeName);

    public FeDate get(Key<FeDate> key);

    public void delete(Key<FeDate> key);

    public void delete(String placeName);

    public List<FeDate> list();

    public List<FeDate> list(long date);

}
