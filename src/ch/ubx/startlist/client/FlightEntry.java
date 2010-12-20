package ch.ubx.startlist.client;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Transient;

public class FlightEntry implements Serializable, Comparable<FlightEntry> {

	public static final int SRC_MANUEL = 0;
	public static final int SRC_OLC = 1;
	public static final int SRC_FLARM = 2;

	private static final long serialVersionUID = 1L;
	private static final int version = 0;

	@Id
	private Long id = null;
	private long created;
	private long modified;
	private String creator;
	private String modifier;
	private String pilot;
	// @Indexed
	private long startTimeInMillis;
	private long endTimeInMillis;
	private boolean training;
	private String remarks;
	// @Indexed
	private String place;
	private String LandingPlace;
	private String plane;
	private boolean startTimeValid = false;
	private boolean endTimeValid = false;
	private String club;
	private String competitionID;
	private int source = SRC_MANUEL;
	@Transient
	private boolean modifiable;
	@Transient
	private boolean deletable;

	public FlightEntry(String pilot, long startTimeInMillis, long endTimeInMillis, boolean training, String remarks, String place) {
		this.pilot = pilot;
		this.startTimeInMillis = startTimeInMillis;
		this.endTimeInMillis = endTimeInMillis;
		this.training = training;
		this.remarks = remarks;
		this.place = place;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static int getVersion() {
		return version;
	}

	public long getModified() {
		return modified;
	}

	public void setModified(long modified) {
		this.modified = modified;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public FlightEntry() {
		super();
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String user) {
		this.creator = user;
	}

	public String getPilot() {
		return pilot;
	}

	public void setPilot(String pilot) {
		this.pilot = pilot;
	}

	public long getStartTimeInMillis() {
		return startTimeInMillis;
	}

	public void setStartTimeInMillis(long startTimeInMillis) {
		this.startTimeInMillis = startTimeInMillis;
	}

	public long getEndTimeInMillis() {
		return endTimeInMillis;
	}

	public void setEndTimeInMillis(long endTimeInMillis) {
		this.endTimeInMillis = endTimeInMillis;
	}

	public boolean isTraining() {
		return training;
	}

	public void setTraining(boolean training) {
		this.training = training;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean isStartTimeValid() {
		return startTimeValid;
	}

	public void setStartTimeValid(boolean startTimeValid) {
		this.startTimeValid = startTimeValid;
	}

	public boolean isEndTimeValid() {
		return endTimeValid;
	}

	public void setEndTimeValid(boolean endTimeValid) {
		this.endTimeValid = endTimeValid;
	}

	public String getPlane() {
		return plane;
	}

	public void setPlane(String plane) {
		this.plane = plane;
	}

	public String getLandingPlace() {
		return LandingPlace;
	}

	public void setLandingPlace(String landingPlace) {
		LandingPlace = landingPlace;
	}

	public boolean isModifiable() {
		return modifiable;
	}

	public void setModifiable(boolean modifiable) {
		this.modifiable = modifiable;
	}

	public boolean isDeletable() {
		return deletable;
	}

	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}

	public long getCreated() {
		return created;
	}

	public String getModifier() {
		return modifier;
	}

	// ----
	public void setClub(String club) {
		this.club = club;
	}

	public String getClub() {
		return club;
	}

	public void setCompetitionID(String competitionID) {
		this.competitionID = competitionID;
	}

	public String getCompetitionID() {
		return competitionID;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	@Override
	public int compareTo(FlightEntry other) {
		if (getStartTimeInMillis() != other.getStartTimeInMillis()) {
			return 1;
		}
		if (isStartTimeValid() != other.isStartTimeValid()) {
			return 1;
		}

		if (isEndTimeValid() != other.isEndTimeValid()) {
			return 1;
		}
		if (isEndTimeValid() & other.isEndTimeValid()) {
			if (getEndTimeInMillis() != other.getEndTimeInMillis()) {
				return 1;
			}
		}

		if (getPilot() != null & other.getPilot() != null) {
			if (!getPilot().equals(other.getPilot())) {
				return 1;
			}
		} else {
			if (getPilot() != other.getPilot()) {
				return 1;
			}
		}

		if (getPlace() != null & other.getPlace() != null) {
			if (!getPlace().equals(other.getPlace())) {
				return 1;
			}
		} else {
			if (getPlace() != other.getPlace()) {
				return 1;
			}
		}

		if (getLandingPlace() != null & other.getLandingPlace() != null) {
			if (!getLandingPlace().equals(other.getLandingPlace())) {
				return 1;
			}
		} else {
			if (getLandingPlace() != other.getLandingPlace()) {
				return 1;
			}
		}

		if (getPlane() != null & other.getPlane() != null) {
			if (!getPlane().equalsIgnoreCase(other.getPlane())) {
				return 1;
			}
		} else {
			if (getPlane() != other.getPlane()) {
				return 1;
			}
		}

		if (isTraining() != other.isTraining()) {
			return 1;
		}

		if (getRemarks() != null & other.getRemarks() != null) {
			if (!getRemarks().equals(other.getRemarks())) {
				return 1;
			}
		} else {
			if (getRemarks() != other.getRemarks()) {
				return 1;
			}
		}

		// all equal!
		return 0;
	}

	public int compareToMajor(FlightEntry other) {
		boolean samePilot = false;
		boolean samePlane = false;
		if (isStartTimeValid() && other.isStartTimeValid()) {
			if (getStartTimeInMillis() != other.getStartTimeInMillis()) {
				return 1;
			}
		} else {
			return 1;
		}
		if (isEndTimeValid() && other.isEndTimeValid()) {
			if (getEndTimeInMillis() != other.getEndTimeInMillis()) {
				return 1;
			}
		} else {
			return 1;
		}
		if (getPlace() != null & other.getPlace() != null) {
			if (!getPlace().equalsIgnoreCase(other.getPlace())) {
				return 1;
			}
		} else {
			return 1;
		}
		if (getPilot() != null & other.getPilot() != null) {
			samePilot = getPilot().equalsIgnoreCase(other.getPilot());
		} else {
			return 1;
		}
		if (getPlane() != null & other.getPlane() != null) {
			samePlane = getPlane().equalsIgnoreCase(other.getPlane());
		} else {
			return 1;
		}
		return samePilot | samePlane ? 0 : 1;
	}

	public FlightEntry copy() {
		FlightEntry copy = new FlightEntry();
		copy.setId(getId());
		copy.setStartTimeValid(isStartTimeValid());
		copy.setStartTimeInMillis(getStartTimeInMillis());
		copy.setEndTimeValid(isEndTimeValid());
		copy.setEndTimeInMillis(getEndTimeInMillis());
		copy.setPilot(getPilot());
		copy.setPlace(getPlace());
		copy.setLandingPlace(getLandingPlace());
		copy.setPlane(getPlane());
		copy.setTraining(isTraining());
		copy.setRemarks(getRemarks());
		copy.setModifiable(isModifiable());
		copy.setCreated(getCreated());
		copy.setCreator(getCreator());
		copy.setModified(getModified());
		copy.setModifier(getModifier());
		copy.setClub(getClub());
		copy.setCompetitionID(getCompetitionID());
		copy.setSource(getSource());
		return copy;
	}

}
