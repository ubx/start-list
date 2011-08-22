package ch.ubx.startlist.shared;

import java.io.Serializable;


import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class FeYear extends FeNodeName implements Serializable {

    private static final long serialVersionUID = 1L;

    public FeYear() {
    }

    public FeYear(Long year, Key<FeNodeName> store) {
        setName(Long.toString(year));
        setParent(store);
    }

    public Long getYear() {
        return Long.valueOf(getName());
    }

    public void setYear(Long year) {
        setName(Long.toString(year));
    }

}
