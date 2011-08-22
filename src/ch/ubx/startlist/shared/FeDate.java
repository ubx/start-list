package ch.ubx.startlist.shared;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class FeDate extends FeNodeName<Long, FePlace> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Key<FePlace> place;

    public Long getDate() {
        return getValue();
    }

    public void setDate(Long date) {
        setValue(date);
    }

    public Key<FePlace> getPlace() {
        return place;
    }

    public void setPlace(Key<FePlace> place) {
        this.place = place;
    }

}
