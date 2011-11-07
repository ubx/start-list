package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FeNode;

import com.googlecode.objectify.Key;

public interface IFeGenDAO<T, V> {

	public Key<T> getOrCreateKey(V value);

	public Key<T> getOrCreateKey(V value, Key<?> parentKey);
	
	public T getOrCreate(V value);

	public T getOrCreate(V value, Key<?> parentKey);

	public T get(Key<?> key);
	
	public T get(V value, Key<?> parentKey);

	public FeNode<?> getG(Key<?> key);

	public void deleteG(Key<?> key);

	public void delete(Key<T> key);

	public void delete(T feNode);

	public List<T> list();

	public List<T> list(Key<?> parent);

	public List<FeNode<?>> listG(Key<?> parent);

	public List<T> list(FeNode<?> parent);

	public List<T> list(V valueL, V valueH, FeNode<?> parent);

	public List<T> find(V value);

}
