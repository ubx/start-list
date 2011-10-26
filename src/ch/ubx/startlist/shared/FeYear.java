package ch.ubx.startlist.shared;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class FeYear extends FeNode<Long, FeStore> implements Comparable<FeYear>, Serializable {

    private static final long serialVersionUID = 1L;

    public FeYear() {
    }

    public FeYear(Long value, Key<FeStore> parent) {
        this.value = value;
        this.parent = parent;
    }

    @Override
    public int compareTo(FeYear o) {
        return value.compareTo(o.value);
    }

}
