package ch.ubx.startlist.shared;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class FeYear extends FeNode<Long, FeStore> implements Serializable {

    private static final long serialVersionUID = 1L;

    public FeYear() {
    }

    public FeYear(Long value, Key<FeStore> parent) {
        setValue(value);
        setParent(parent);
    }

}
