package ch.ubx.startlist.shared;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class FeNode<V, P> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id = null;
    private Key<P> parent;
    private V value;

    public FeNode() {
    }

    public FeNode(V value) {
        this.value = value;
    }

    public FeNode(V value, Key<P> parent) {
        this.value = value;
        this.parent = parent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Key<P> getParent() {
        return parent;
    }

    public void setParent(Key<P> parentKey) {
        this.parent = parentKey;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V value) {
        this.value = value;
    }

}
