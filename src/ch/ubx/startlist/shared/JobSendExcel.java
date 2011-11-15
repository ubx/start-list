package ch.ubx.startlist.shared;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ch.ubx.startlist.server.ExcelSender;

import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class JobSendExcel extends Job {

	private static final long serialVersionUID = 1L;

	private String subject;
	private String place;
	private String recipients;
	private String filterGliders;
	private String filterTowplanes;
	private int daysBehind = 0;

	public JobSendExcel(String name, String subject, String place, String recipients, String filterGliders, String filterTowplanes) {
		this.name = name;
		this.subject = subject;
		this.place = place;
		this.recipients = recipients;
		this.filterGliders = filterGliders;
		this.filterTowplanes = filterTowplanes;
	}

	public JobSendExcel(String name, String subject, String place, String recipients) {
		this(name, subject, place, recipients, null, null);
	}

	public JobSendExcel() {
	}

	@Override
	public void execute(String name, Calendar now) {
		try {
			ExcelSender.execute(this, now);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	public void setDaysBehind(int daysBehind) {
		this.daysBehind = daysBehind;
	}

	public int getDaysBehind() {
		return daysBehind;
	}

}
