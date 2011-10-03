package ch.ubx.startlist.shared;

import java.io.Serializable;

import javax.persistence.Id;

public class SentFlightEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private Long flightEntry;
	private String sendExcel;
	private long lastModified;

	public SentFlightEntry() {
	}

	public SentFlightEntry(Long flightEntry, String sendExcel, long lastModified) {
		this.flightEntry = flightEntry;
		this.sendExcel = sendExcel;
		this.lastModified = lastModified;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setFlightEntry(Long flightEntry) {
		this.flightEntry = flightEntry;
	}

	public Long getFlightEntry() {
		return flightEntry;
	}

	public void setSendExcel(String sendExcel) {
		this.sendExcel = sendExcel;
	}

	public String getSendExcel() {
		return sendExcel;
	}

}
