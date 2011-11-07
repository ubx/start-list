package ch.ubx.startlist.shared;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class FeYear extends FeNode<Long> implements Comparable<FeYear>, Serializable {

    private static final long serialVersionUID = 1L;

    public FeYear() {
    }

    @Override
    public int compareTo(FeYear o) {
        return value.compareTo(o.value);
    }

}
