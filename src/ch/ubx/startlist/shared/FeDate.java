package ch.ubx.startlist.shared;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class FeDate extends FeNode<Long, FePlace> implements Comparable<FeDate>, Serializable {

    private static final long serialVersionUID = 1L;

    public FeDate() {
    }

    public FeDate(Long value, Key<FePlace> parent) {
        setValue(value);
        setParent(parent);
    }

    @Override
    public int compareTo(FeDate o) {
        return getValue().compareTo(o.getValue());
    }

}
