package ch.ubx.startlist.shared;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Id;

public class ImportOLC implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	private String places;

	public ImportOLC(String name, String places) {
		this.name = name;
		this.places = places;
	}

	public ImportOLC() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlaces() {
		return places;
	}

	public List<String> getPlacesList() {
		return Arrays.asList(places.split(";"));
	}

	public void setPlaces(String places) {
		this.places = places;
	}

}
