package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FeNodeName;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

public class FeGenDAOobjectify<T extends FeNodeName> extends DAOBase implements FeGenDAO<T> {

    private final Class<T> clazz;

    public FeGenDAOobjectify(Class<T> cls) {
        this.clazz = cls;
        ObjectifyService.register(cls);
    }

    @Override
    public Key<T> getOrCreateKey(String name) {
        Key<T> key = (Key<T>) ofy().query(clazz).filter("name", name).getKey();
        if (key == null) {
            key = (Key<T>) ofy().put(newNode(name));
        }
        return key;
    }

    @Override
    public Key<T> getOrCreateKey(String name, Key<? extends FeNodeName> parentKey) {
        Key<T> key = (Key<T>) ofy().query(clazz).filter("name", name).filter("parent", parentKey).getKey();
        if (key == null) {
            key = (Key<T>) ofy().put(newNode(name, parentKey));
        }
        return key;
    }

    @Override
    public T getOrCreate(String name) {
        T elem = (T) ofy().find(clazz, name);
        if (elem == null) {
            elem = newNode(name);
            ofy().put(elem);
        }
        return elem;
    }

    @Override
    public T getOrCreate(String name, Key<? extends FeNodeName> parentKey) {
        T elem = (T) ofy().find(clazz, name);
        if (elem == null) {
            elem = newNode(name, parentKey);
            ofy().put(elem);
        }
        return elem;
    }

    @Override
    public T get(Key<T> key) {
        return ((T) ofy().get(key));
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
    public List<? extends FeNodeName> list() {
        return ofy().query(clazz).list();

    }

    @Override
    public List<? extends FeNodeName> list(Key<? extends FeNodeName> parentKey) {
        return ofy().query(clazz).filter("parent", parentKey).list();
    }

    private T newNode(String name) {
        return newNode(name, null);
    }

    private T newNode(String name, Key<? extends FeNodeName> parentKey) {
        T elem = null;
        try {
            elem = (T) clazz.newInstance();
            elem.setName(name);
            elem.setParent((Key<FeNodeName>) parentKey);
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
