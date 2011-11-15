package ch.ubx.startlist.shared;

import java.io.Serializable;
import javax.persistence.Id;

import com.googlecode.objectify.annotation.Cached;

@Cached
public class Pilot implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String name;

	public Pilot() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}