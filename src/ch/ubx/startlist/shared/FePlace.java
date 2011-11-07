package ch.ubx.startlist.shared;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class FePlace extends FeNode<String> implements Comparable<FePlace>, Serializable {

	private static final long serialVersionUID = 1L;

	public FePlace() {
	}

	@Override
	public int compareTo(FePlace o) {
		return value.compareTo(o.value);
	}

}
