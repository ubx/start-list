package ch.ubx.startlist.shared;

import java.io.Serializable;
import javax.persistence.Id;

public class Pilot implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	private String id;

	public Pilot() {
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}