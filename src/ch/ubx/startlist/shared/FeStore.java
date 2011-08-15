package ch.ubx.startlist.shared;

import java.io.Serializable;

import javax.persistence.Id;

public class FeStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String name;

    public FeStore() {
    }
    
    public FeStore(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
