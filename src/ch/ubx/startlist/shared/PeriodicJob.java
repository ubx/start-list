package ch.ubx.startlist.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Id;

public class PeriodicJob implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	private long nextTimeInMillis = 0; // in milisec
	private boolean enabled = true;
	private String sendExcels;
	private String importOLCJobs;
	private boolean[] days = new boolean[] { true, true, true, true, true, true, true }; // SO..SA
	private String time; // "hh:mm"
	private String timeZone; // "UTC+0";

	public PeriodicJob() {
	}

	public PeriodicJob(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getNextTimeInMillis() {
		return nextTimeInMillis;
	}

	public void setNextTimeInMillis(long nextTimeInMillis) {
		this.nextTimeInMillis = nextTimeInMillis;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		if (this.enabled != enabled) {
			nextTimeInMillis = 0;
			this.enabled = enabled;
		}
	}

	public String getSendExcels() {
		return sendExcels;
	}

	public List<String> getSendExcelJobList() {
		return sendExcels == null ? new ArrayList<String>() : Arrays.asList(sendExcels.split(";"));
	}

	public void setSendExcels(String sendExcels) {
		this.sendExcels = sendExcels;
	}

	public String getImportOLCJobs() {
		return importOLCJobs;
	}

	public List<String> getImportOLCJobList() {
		return importOLCJobs == null ? new ArrayList<String>() : Arrays.asList(importOLCJobs.split(";"));
	}

	public void setImportOLCJobs(String importOLCJobs) {
		this.importOLCJobs = importOLCJobs;
	}

	public boolean[] getDays() {
		return days;
	}

	public void setDays(boolean[] days) {
		if (days.length != 7) {
			throw new RuntimeException("Array length must be 7");
		}
		for (int i = 0; i < days.length; i++) {
			this.days[i] = days[i];
		}
		this.days = days;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

}
