package ch.ubx.startlist.server;

import java.util.List;

import com.googlecode.objectify.Key;

public interface IFeGenDAO<P, V, T> {

    public Key<T> getOrCreateKey(V value);

    public Key<T> getOrCreateKey(V value, Key<P> parentKey);

    public T getOrCreate(V value);

    public T getOrCreate(V value, Key<P> parent);

    public T get(Key<T> key);

    public void delete(Key<T> key);

    public void delete(T feNode);

    public List<T> list();

    public List<T> list(Key<P> parent);
}
