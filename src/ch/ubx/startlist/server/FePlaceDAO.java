package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FePlace;

import com.googlecode.objectify.Key;

public interface FePlaceDAO {

    public Key<FePlace> getOrCreateKey(String placeName, long year);

    public FePlace getOrCreate(String placeName, long year);

    public FePlace get(Key<FePlace> key);

    public void delete(Key<FePlace> key);

    public void delete(String placeName);

    public List<FePlace> list();

    public List<FePlace> list(long year);

}
