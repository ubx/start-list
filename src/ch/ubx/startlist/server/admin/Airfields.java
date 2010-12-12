package ch.ubx.startlist.server.admin;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Airfields {
	@Element
	private String id;
	@Element
	private String name;
	@Element(required = false)
	private String icao_id;
	@Element
	private String country;
	@Element
	private String latitude;
	@Element
	private String longitude;
	@Element(required = false)
	private String region;
	@Element
	private String usedinolc2005;
	@Element
	private String corrected;
	@Element
	private String timestamp;

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

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getUsedinolc2005() {
		return usedinolc2005;
	}

	public void setUsedinolc2005(String usedinolc2005) {
		this.usedinolc2005 = usedinolc2005;
	}

	public String getCorrected() {
		return corrected;
	}

	public void setCorrected(String corrected) {
		this.corrected = corrected;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
