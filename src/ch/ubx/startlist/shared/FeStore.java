package ch.ubx.startlist.shared;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class FeStore extends FeNode<String, FeStore> implements Serializable {

    private static final long serialVersionUID = 1L;

    public FeStore() {
    }

    public FeStore(String value) {
        setValue(value);
    }

}
