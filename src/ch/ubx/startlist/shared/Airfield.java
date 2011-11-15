package ch.ubx.startlist.shared;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Cached;

@Cached
public class Airfield implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	private String id;
	private String icao_id;
	private String country;

	public Airfield() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcao_id() {
		return icao_id;
	}

	public void setIcao_id(String icaoId) {
		icao_id = icaoId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}