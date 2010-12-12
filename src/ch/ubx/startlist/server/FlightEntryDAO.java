package ch.ubx.startlist.server;

import java.util.List;
import java.util.Set;

import ch.ubx.startlist.client.FlightEntry;

public interface FlightEntryDAO {

	public FlightEntry addFlightEntry(FlightEntry flightEntry);

	public FlightEntry removeFlightEntry(FlightEntry flightEntry);

	public FlightEntry updateFlightEntry(FlightEntry flightEntry);

	public List<FlightEntry> listflightEntry(String place, long dateTimeInMillis, int startIndex, int maxCount);

	public Set<Integer> listYears();

	public Set<String> listAirfields(int year);

	public Set<Long> listDates(String place, int year);

}
