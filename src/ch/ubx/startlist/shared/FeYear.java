package ch.ubx.startlist.shared;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

public class FeYear implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long year;
    private Key<FeStore> store;

    public FeYear() {
    }

    public FeYear(Long year, Key<FeStore> store) {
        this.year = year;
        this.store = store;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Key<FeStore> getStore() {
        return store;
    }

    public void setStore(Key<FeStore> store) {
        this.store = store;
    }

}
