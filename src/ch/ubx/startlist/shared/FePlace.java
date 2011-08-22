package ch.ubx.startlist.shared;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class FePlace extends FeNodeName<String, FeYear> implements Serializable {

    private static final long serialVersionUID = 1L;

    public FePlace() {
    }

    public FePlace(String place, Key<FeYear> year) {
        setValue(place);
        setParent(year);
    }

}
