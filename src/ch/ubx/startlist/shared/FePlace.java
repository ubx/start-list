package ch.ubx.startlist.shared;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class FePlace extends FeNode<String, FeYear> implements Comparable<FePlace>, Serializable {

    private static final long serialVersionUID = 1L;

    public FePlace() {
    }

    public FePlace(String value, Key<FeYear> parent) {
        this.value = value;
        this.parent = parent;
    }

    @Override
    public int compareTo(FePlace o) {
        return value.compareTo(o.value);
    }

}
