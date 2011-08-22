package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FeNodeName;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

public class FeGenDAOobjectify<T extends FeNodeName<?, P>, P> extends DAOBase implements IFeGenDAO<T, P> {

    private final Class<T> clazz;

    public FeGenDAOobjectify(Class<T> cls) {
        this.clazz = cls;
        ObjectifyService.register(cls);
    }

    @Override
    public Key<T> getOrCreateKey(String id) {
        Key<T> key = (Key<T>) ofy().query(clazz).filter("id", id).getKey();
        if (key == null) {
            key = (Key<T>) ofy().put(newNode(id));
        }
        return key;
    }

    @Override
    public Key<T> getOrCreateKey(String id, Key<P> parentKey) {
        Key<T> key = (Key<T>) ofy().query(clazz).filter("id", id).filter("parent", parentKey).getKey();
        if (key == null) {
            key = (Key<T>) ofy().put(newNode(id, parentKey));
        }
        return key;
    }

    
    @Override
    public T getOrCreate(String name) {
        T elem = ofy().find(clazz, name);
        if (elem == null) {
            elem = newNode(name);
            ofy().put(elem);
        }
        return elem;
    }

    @Override
    public T getOrCreate(String name, Key<P> parentKey) {
        T elem = ofy().find(clazz, name);
        if (elem == null) {
            elem = newNode(name, parentKey);
            ofy().put(elem);
        }
        return elem;
    }
    
    @Override
    public T get(Key<T> key) {
        return ofy().get(key);
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
        return ofy().query(clazz).filter("parent", parentKey).list();
    }

    private T newNode(String name) {
        return newNode(name, null);
    }

    private T newNode(String name, Key<P> parentKey) {
        T elem = null;
        try {
            elem = clazz.newInstance();
            elem.setId(name);
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
