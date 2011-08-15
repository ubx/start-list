package ch.ubx.startlist.shared;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

public class FeDate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long date;

    private Key<FePlace> place;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Key<FePlace> getPlace() {
        return place;
    }

    public void setPlace(Key<FePlace> place) {
        this.place = place;
    }

}
