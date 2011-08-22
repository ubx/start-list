package ch.ubx.startlist.server;

import java.util.List;

import com.googlecode.objectify.Key;

public interface IFeGenDAO<T,P> {

    public Key<T> getOrCreateKey(String name);

    public Key<T> getOrCreateKey(String name, Key<P> parent);

    public T getOrCreate(String name);

    public T getOrCreate(String name, Key<P> parent);

    public T get(Key<T> key);

    public void delete(Key<T> key);

    public void delete(T feNode);

    public List<T> list();

    public List<T> list(Key<P> parent);
}
