package ch.ubx.startlist.shared;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class FeNodeName<V, P> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private Key<P> parent;
    private V value;

    public FeNodeName() {
    }

    public FeNodeName(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Key<P> getParent() {
        return parent;
    }

    public void setParent(Key<P> parentKey) {
        this.parent = parentKey;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

}
