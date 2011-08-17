package ch.ubx.startlist.server;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import ch.ubx.startlist.shared.FlightEntry;

public interface FlightEntryDAO {

    public List<FlightEntry> listflightEntry();

    public List<FlightEntry> listflightEntry(String place, long dateTimeInMillis, int startIndex, int maxCount);

    public FlightEntry removeFlightEntry(FlightEntry flightEntry);

    public FlightEntry createOrUpdateFlightEntry(FlightEntry flightEntry);

    public Set<Long> listDates(String place, int year);

    public Set<String> listAirfields(int year);

    public Set<Integer> listYears();

    public List<FlightEntry> listflightEntry(int year);

    public List<FlightEntry> listflightEntry(Calendar date, String place);

    public List<FlightEntry> listflightEntry(Calendar startDate, Calendar endDate, String place);

    public List<FlightEntry> listflightEntry(int year, int month, int day, String place);

    public List<FlightEntry> listflightEntry(int year, String place);

    public List<FlightEntry> listflightEntry(int yearStart, int yearEnd, String place);

    public void addFlightEntries(List<FlightEntry> flightEntries);

    public void addFlightEntries4Test(List<FlightEntry> flightEntries);

}
