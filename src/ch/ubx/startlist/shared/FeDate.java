package ch.ubx.startlist.shared;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class FeDate extends FeNode<Long> implements Comparable<FeDate>, Serializable {

	private static final long serialVersionUID = 1L;

	public FeDate() {
	}

	@Override
	public int compareTo(FeDate o) {
		return value.compareTo(o.value);
	}

}
