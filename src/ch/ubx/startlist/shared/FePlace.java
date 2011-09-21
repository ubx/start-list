package ch.ubx.startlist.shared;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class FePlace extends FeNode<String, FeYear> implements Serializable {

    private static final long serialVersionUID = 1L;

    public FePlace() {
    }

    public FePlace(String value, Key<FeYear> parent) {
        setValue(value);
        setParent(parent);
    }

}
