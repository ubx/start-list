package ch.ubx.startlist.server;

import java.util.Calendar;
import java.util.List;

import ch.ubx.startlist.shared.FeDate;
import ch.ubx.startlist.shared.FeFlightEntry;
import ch.ubx.startlist.shared.FePlace;
import ch.ubx.startlist.shared.FeYear;

public interface FlightEntryDAO2 {

	public List<FeFlightEntry> listflightEntry();

	public List<FeFlightEntry> listflightEntry(FeDate date, int startIndex, int maxCount);

	public List<FeFlightEntry> listflightEntry(FeDate date);

	public List<FeFlightEntry> listflightEntry(int yearInt);

	public List<FeFlightEntry> listflightEntry(int startYearInt, int endYearInt, String placeStr);

	public List<FeFlightEntry> listflightEntry(long startDateLong, long endDateLong, String placeStr);

	public List<FeFlightEntry> listflightEntry(int year, int month, int day, String place);

	public FeFlightEntry removeFlightEntry(FeFlightEntry flightEntry);

	public FeFlightEntry createOrUpdateFlightEntry(FeFlightEntry flightEntry);

	public List<FeDate> listDate(FePlace place);

	public List<FeDate> listDate(String placeStr, int yearInt);

	public List<FePlace> listAirfield(FeYear year);

	public List<FePlace> listAirfield(int yearInt);

	public List<FeYear> listYear();

	public void addFlightEntries(List<FeFlightEntry> flightEntries);

	public List<FeFlightEntry> listflightEntry(Calendar startDate, Calendar endDate, String place);

	public List<FeFlightEntry> listflightEntry(Calendar date, String place);

	public void addFlightEntries4Test(List<FeFlightEntry> flightEntries);

}
