package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FeNodeName;

import com.googlecode.objectify.Key;

public interface FeGenDAO<T> {

    public Key<T> getOrCreateKey(String name);

    public Key<T> getOrCreateKey(String name, Key<? extends FeNodeName> othersKey);

    public T getOrCreate(String name);

    public T getOrCreate(String name, Key<? extends FeNodeName> othersKey);

    public T get(Key<T> key);

    public void delete(Key<T> key);

    public void delete(T feNode);

    public List<? extends FeNodeName> list();

    public List<? extends FeNodeName> list(Key<? extends FeNodeName> othersKey);
}
