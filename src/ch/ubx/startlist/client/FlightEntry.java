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
	private String passengerOrInstructor;
	private long startTimeInMillis;
	private long endTimeGliderInMillis;
	private long endTimeTowplaneInMillis;
	private boolean training;
	private String remarks;
	private String place;
	private String LandingPlace;
	private String registrationGlider;
	private String registrationTowplane;
	private boolean startTimeValid = false;
	private boolean endTimeGliderValid = false;
	private boolean endTimeTowplaneValid = false;
	private String club;
	private String competitionID;
	private int source = SRC_MANUEL;
	@Transient
	private boolean modifiable;
	@Transient
	private boolean deletable;

	public FlightEntry(String pilot, long startTimeInMillis, long endTimeGliderInMillis, boolean training, String remarks, String place) {
		this.pilot = pilot;
		this.startTimeInMillis = startTimeInMillis;
		this.endTimeGliderInMillis = endTimeGliderInMillis;
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
	
	public String getPassengerOrInstructor() {
		return passengerOrInstructor;
	}

	public void setPassengerOrInstructor(String passengerOrInstructor) {
		this.passengerOrInstructor = passengerOrInstructor;
	}

	public long getStartTimeInMillis() {
		return startTimeInMillis;
	}

	public void setStartTimeInMillis(long startTimeInMillis) {
		this.startTimeInMillis = startTimeInMillis;
	}

	public long getEndTimeGliderInMillis() {
		return endTimeGliderInMillis;
	}

	public void setEndTimeGliderInMillis(long endTimeGliderInMillis) {
		this.endTimeGliderInMillis = endTimeGliderInMillis;
	}

	public long getEndTimeTowplaneInMillis() {
		return endTimeTowplaneInMillis;
	}

	public void setEndTimeTowplaneInMillis(long endTimeTowplaneInMillis) {
		this.endTimeTowplaneInMillis = endTimeTowplaneInMillis;
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

	public boolean isEndTimeGliderValid() {
		return endTimeGliderValid;
	}

	public void setEndTimeGliderValid(boolean endTimeGliderValid) {
		this.endTimeGliderValid = endTimeGliderValid;
	}

	public boolean isEndTimeTowplaneValid() {
		return endTimeTowplaneValid;
	}

	public void setEndTimeTowplaneValid(boolean endTimeTowplaneValid) {
		this.endTimeTowplaneValid = endTimeTowplaneValid;
	}
	
	public String getRegistrationGlider() {
		return registrationGlider;
	}

	public void setRegistrationGlider(String registrationGlider) {
		this.registrationGlider = registrationGlider;
	}

	public String getRegistrationTowplane() {
		return registrationTowplane;
	}

	public void setRegistrationTowplane(String registrationTowplane) {
		this.registrationTowplane = registrationTowplane;
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
	//Compares everything except created, modified, creator, modifier, club, competitionID, source, modifiable, deletable
	//returns 0 if equal, 1 if not equal
	public int compareTo(FlightEntry other) {
		if (getStartTimeInMillis() != other.getStartTimeInMillis()) {
			return 1;
		}
		if (isStartTimeValid() != other.isStartTimeValid()) {
			return 1;
		}

		if (isEndTimeGliderValid() != other.isEndTimeGliderValid()) {
			return 1;
		}
		if (isEndTimeGliderValid() & other.isEndTimeGliderValid()) {
			if (getEndTimeGliderInMillis() != other.getEndTimeGliderInMillis()) {
				return 1;
			}
		}
		
		if (isEndTimeTowplaneValid() != other.isEndTimeTowplaneValid()) {
			return 1;
		}
		if (isEndTimeTowplaneValid() & other.isEndTimeTowplaneValid()) {
			if (getEndTimeTowplaneInMillis() != other.getEndTimeTowplaneInMillis()) {
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
		
		if (getPassengerOrInstructor() != null & other.getPassengerOrInstructor() != null) {
			if (!getPassengerOrInstructor().equals(other.getPassengerOrInstructor())) {
				return 1;
			}
		} else {
			if (getPassengerOrInstructor() != other.getPassengerOrInstructor()) {
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

		if (getRegistrationGlider() != null & other.getRegistrationGlider() != null) {
			if (!getRegistrationGlider().equalsIgnoreCase(other.getRegistrationGlider())) {
				return 1;
			}
		} else {
			if (getRegistrationGlider() != other.getRegistrationGlider()) {
				return 1;
			}
		}
		
		if (getRegistrationTowplane() != null & other.getRegistrationTowplane() != null) {
			if (!getRegistrationTowplane().equalsIgnoreCase(other.getRegistrationTowplane())) {
				return 1;
			}
		} else {
			if (getRegistrationTowplane() != other.getRegistrationTowplane()) {
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

	// used to compare FlightEntrys which are not complete (OLC import)
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
		if (isEndTimeGliderValid() && other.isEndTimeGliderValid()) {
			if (getEndTimeGliderInMillis() != other.getEndTimeGliderInMillis()) {
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
		if (getRegistrationGlider() != null & other.getRegistrationGlider() != null) {
			samePlane = getRegistrationGlider().equalsIgnoreCase(other.getRegistrationGlider());
		} else {
			return 1;
		}
		return samePilot | samePlane ? 0 : 1;
	}

	// copies all propertys of the instance into a new instance
	public FlightEntry copy() {
		FlightEntry copy = new FlightEntry();
		copy.setId(getId());
		copy.setStartTimeValid(isStartTimeValid());
		copy.setStartTimeInMillis(getStartTimeInMillis());
		copy.setEndTimeGliderValid(isEndTimeGliderValid());
		copy.setEndTimeTowplaneValid(isEndTimeTowplaneValid());
		copy.setEndTimeGliderInMillis(getEndTimeGliderInMillis());
		copy.setEndTimeTowplaneInMillis(getEndTimeTowplaneInMillis());
		copy.setPilot(getPilot());
		copy.setPassengerOrInstructor(getPassengerOrInstructor());
		copy.setPlace(getPlace());
		copy.setLandingPlace(getLandingPlace());
		copy.setRegistrationGlider(getRegistrationGlider());
		copy.setRegistrationTowplane(getRegistrationGlider());
		copy.setTraining(isTraining());
		copy.setRemarks(getRemarks());
		copy.setModifiable(isModifiable());
		copy.setDeletable(isDeletable());
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
