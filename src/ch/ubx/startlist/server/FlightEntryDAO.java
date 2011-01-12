package ch.ubx.startlist.server;

import java.util.List;
import java.util.Set;

import ch.ubx.startlist.shared.FlightEntry;

public interface FlightEntryDAO {

	public FlightEntry removeFlightEntry(FlightEntry flightEntry);

	public FlightEntry createOrUpdateFlightEntry(FlightEntry flightEntry);

	public List<FlightEntry> listflightEntry(String place, long dateTimeInMillis, int startIndex, int maxCount);

	public Set<Integer> listYears();

	public Set<String> listAirfields(int year);

	public Set<Long> listDates(String place, int year);

}
