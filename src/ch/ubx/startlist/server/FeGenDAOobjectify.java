package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FeNodeName;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

public class FeGenDAOobjectify<T extends FeNodeName> extends DAOBase implements FeGenDAO<T> {

    // static {
    // ObjectifyService.register(FeNodeName.class);
    // }

    private final Class<? extends FeNodeName> cls;

    public FeGenDAOobjectify(Class<T> cls) {
        this.cls = cls;
        ObjectifyService.register(cls);
    }

    @Override
    public Key<T> getOrCreateKey(String name) {
        Key<T> key = (Key<T>) ofy().query(cls).filter("name", name).getKey();
        if (key == null) {
            key = (Key<T>) ofy().put(newNode(name));
        }
        return key;
    }

    @Override
    public Key<T> getOrCreateKey(String name, Key<? extends FeNodeName> othersKey) {
        Key<T> key = (Key<T>) ofy().query(cls).filter("name", name).filter("others", othersKey).getKey();
        if (key == null) {
            key = (Key<T>) ofy().put(newNode(name, othersKey));
        }
        return key;
    }

    @Override
    public T getOrCreate(String name) {
        T elem = (T) ofy().find(cls, name);
        if (elem == null) {
            elem = newNode(name);
            ofy().put(elem);
        }
        return elem;
    }

    @Override
    public T getOrCreate(String name, Key<? extends FeNodeName> othersKey) {
        T elem = (T) ofy().find(cls, name);
        if (elem == null) {
            elem = newNode(name, othersKey);
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
        return ofy().query(cls).list();

    }

    @Override
    public List<? extends FeNodeName> list(Key<? extends FeNodeName> othersKey) {
        return ofy().query(cls).filter("others", othersKey).list();
    }

    private T newNode(String name) {
        return newNode(name, null);
    }

    private T newNode(String name, Key<? extends FeNodeName> othersKey) {
        T elem = null;
        try {
            elem = (T) cls.newInstance();
            elem.setName(name);
            elem.setOthers((Key<FeNodeName>) othersKey);
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
