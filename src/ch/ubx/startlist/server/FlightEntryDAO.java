package ch.ubx.startlist.server;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import ch.ubx.startlist.shared.FlightEntry;

public interface FlightEntryDAO {

	public abstract List<FlightEntry> listflightEntry(String place, long dateTimeInMillis, int startIndex, int maxCount);

	public abstract FlightEntry removeFlightEntry(FlightEntry flightEntry);

	public abstract FlightEntry createOrUpdateFlightEntry(FlightEntry flightEntry);

	public abstract Set<Long> listDates(String place, int year);

	public abstract Set<String> listAirfields(int year);

	public abstract Set<Integer> listYears();

	public abstract List<FlightEntry> listflightEntry(int year);

	public abstract List<FlightEntry> listflightEntry(Calendar date, String place);
	
	public abstract List<FlightEntry> listflightEntry(Calendar startDate, Calendar endDate, String place);

	public abstract List<FlightEntry> listflightEntry(int year, int month, int day, String place);

	public abstract List<FlightEntry> listflightEntry(int year, String place);

	public abstract List<FlightEntry> listflightEntry(int yearStart, int yearEnd, String place);

	public abstract void addFlightEntries(List<FlightEntry> flightEntries);
	
	public abstract void addFlightEntries4Test(List<FlightEntry> flightEntries);

}
