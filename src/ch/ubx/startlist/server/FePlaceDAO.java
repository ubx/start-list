package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FePlace;
import ch.ubx.startlist.shared.FeYear;

import com.googlecode.objectify.Key;

public interface FePlaceDAO {

    public Key<FePlace> getOrCreateKey(String placeName, Key<FeYear> yearKey);

    public FePlace getOrCreate(String placeName, Key<FeYear> yearKey);

    public FePlace get(Key<FePlace> key);

    public void delete(Key<FePlace> key);

    public void delete(FePlace place);

    public List<FePlace> list();

    public List<FePlace> list(Key<FeYear> yearKey);

}
