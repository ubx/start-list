package ch.ubx.startlist.shared;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ch.ubx.startlist.server.OlcImporter;

import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class JobImportOLC extends Job {

	private static final long serialVersionUID = 1L;
	private String places;

	public JobImportOLC(String name, String places) {
		this.name = name;
		this.places = places;
	}

	public JobImportOLC() {
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

	@Override
	public void execute(String name, Calendar now) {
		OlcImporter.execute(name, this);

	}

}
