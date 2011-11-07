package ch.ubx.startlist.shared;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class FeNode<V> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id = null;
	protected Key<?>[] parents;
	protected V value;

	public FeNode() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Key<?> getParent() {
		return parents != null ? parents[0] : null;
	}

	public Key<?>[] getParents() {
		return parents;
	}

	public void setParent(Key<?>... parentKeys) {
		this.parents = parentKeys;
	}

	public V getValue() {
		return this.value;
	}

	public void setValue(V value) {
		this.value = value;
	}
}
