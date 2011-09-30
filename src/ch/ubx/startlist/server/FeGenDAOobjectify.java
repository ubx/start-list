package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FeNode;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

public class FeGenDAOobjectify<T extends FeNode<V, P>, P, V> extends DAOBase implements IFeGenDAO<P, V, T> {

    private static final String VALUE = "value";
    private static final String PARENT = "parent";

    private final Class<T> clazz;

    public FeGenDAOobjectify(Class<T> cls) {
        this.clazz = cls;
        ObjectifyService.register(cls);
    }

    @Override
    public Key<T> getOrCreateKey(V value) {
        Key<T> key = ofy().query(clazz).filter(VALUE, value).getKey();
        if (key == null) {
            T elem = newNode(value);
            key = ofy().put(elem);
        }
        return key;
    }

    @Override
    public Key<T> getOrCreateKey(V value, Key<P> parentKey) {
        Key<T> key = ofy().query(clazz).filter(VALUE, value).filter(PARENT, parentKey).getKey();
        if (key == null) {
            T elem = newNode(value, parentKey);
            key = ofy().put(elem);
        }
        return key;
    }

    @Override
    public T getOrCreate(V value) {
        T elem = ofy().query(clazz).filter(VALUE, value).get();
        if (elem == null) {
            elem = newNode(value);
            ofy().put(elem);
        }
        return elem;
    }

    @Override
    public T getOrCreate(V value, Key<P> parentKey) {
        T elem = ofy().query(clazz).filter(PARENT, parentKey).filter(VALUE, value).get();
        if (elem == null) {
            elem = newNode(value, parentKey);
            ofy().put(elem);
        }
        return elem;
    }

    @Override
    public T get(Key<T> key) {
        return (T) ofy().get(key);
    }

    @Override
    public T get(V value, Key<P> parentKey) {
        return ofy().query(clazz).filter(VALUE, value).filter(PARENT, parentKey).get();
    }


    @Override
    public void delete(Key<T> key) {
        ofy().delete(key);
    }

    @Override
    public void delete(T elem) {
        ofy().delete(elem);
    }

    @Override
    public List<T> list() {
        return ofy().query(clazz).list();
    }

    @Override
    public List<T> list(Key<P> parentKey) {
        return (List<T>) ofy().query(clazz).filter(PARENT, parentKey).list();
    }

    @Override
    public List<T> list(P parent) {
        return (List<T>) ofy().query(clazz).filter(PARENT, parent).list();
    }

    @Override
    public  List<T> list(V valueL, V valueH, P parent) {
        return ofy().query(clazz).filter(PARENT, parent).filter(VALUE + ">=", valueL).filter(VALUE + "<=", valueH).list();
    }

    @Override
    public List<T> find(V value) {
        return (List<T>) ofy().query(clazz).filter(VALUE, value).list();
    }


    private T newNode(V value) {
        return newNode(value, null);
    }

    private T newNode(V value, Key<P> parentKey) {
        T elem = null;
        try {
            elem = clazz.newInstance();
            elem.setValue(value);
            elem.setParent(parentKey);
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return elem;
    }

}
