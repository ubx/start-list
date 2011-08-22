package ch.ubx.startlist.shared;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class FeYear extends FeNodeName<Long, FeStore> implements Serializable {

    private static final long serialVersionUID = 1L;

    public FeYear() {
    }

    public FeYear(Long year, Key<FeStore> store) {
        setValue(year);
        setParent(store);
    }

    public Long getYear() {
        return getValue();
    }

    public void setYear(Long year) {
        setValue(year);
    }

}
