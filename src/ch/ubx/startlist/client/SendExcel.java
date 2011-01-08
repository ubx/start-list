package ch.ubx.startlist.client;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Id;

public class SendExcel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	private String subject;
	private String place;
	private String recipients;
	private String filterGliders;
	private String filterTowplanes;

	public SendExcel(String name, String subject, String place, String recipients, String filterGliders, String filterTowplanes) {
		this.name = name;
		this.subject = subject;
		this.place = place;
		this.recipients = recipients;
		this.filterGliders = filterGliders;
		this.filterTowplanes = filterTowplanes;
	}

	public SendExcel(String name, String subject, String place, String recipients) {
		this(name, subject, place, recipients, null, null);
	}

	public SendExcel() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getRecipients() {
		return recipients;
	}

	public List<String> getRecipientsList() {
		return Arrays.asList(recipients.split(";"));
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	public String getFilterGliders() {
		return filterGliders;
	}

	public void setFilterGliders(String filterGliders) {
		this.filterGliders = filterGliders;
	}

	public String getFilterTowplanes() {
		return filterTowplanes;
	}

	public void setFilterTowplanes(String filterTowplanes) {
		this.filterTowplanes = filterTowplanes;
	}

}
