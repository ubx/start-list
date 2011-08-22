package ch.ubx.startlist.shared;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class FeNodeName implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String name;
    private Key<FeNodeName> parent;

    public FeNodeName() {
    }

    public FeNodeName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Key<FeNodeName> getParent() {
        return parent;
    }

    public void setParent(Key<FeNodeName> parent) {
        this.parent = parent;
    }

}
