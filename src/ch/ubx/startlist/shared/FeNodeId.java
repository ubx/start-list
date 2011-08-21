package ch.ubx.startlist.shared;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class FeNodeId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    protected Long id;
    protected Key<FeNodeId> others;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
