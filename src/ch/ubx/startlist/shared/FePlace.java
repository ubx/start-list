package ch.ubx.startlist.shared;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

public class FePlace implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Id
    private String name;
    private Key<FeYear> year;

    public FePlace() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Key<FeYear> getYear() {
        return year;
    }

    public void setYear(Key<FeYear> year) {
        this.year = year;
    }

}
