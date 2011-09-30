package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FeDate;
import ch.ubx.startlist.shared.FePlace;
import ch.ubx.startlist.shared.FeYear;
import ch.ubx.startlist.shared.FlightEntry;

public interface FlightEntryDAO2 {

    public List<FlightEntry> listflightEntry();

    public List<FlightEntry> listflightEntry(FeDate date, int startIndex, int maxCount);

    public List<FlightEntry> listflightEntry(FeDate date);

    public List<FlightEntry> listflightEntry(int yearInt);

    public List<FlightEntry> listflightEntry(int startYearInt, int endYearInt, String placeStr);

    public List<FlightEntry> listflightEntry(long startDateLong, long endDateLong, String placeStr);

    public List<FlightEntry> listflightEntry(int year, int month, int day, String place);

    public FlightEntry removeFlightEntry(FlightEntry flightEntry);

    public FlightEntry createOrUpdateFlightEntry(FlightEntry flightEntry);

    public List<FeDate> listDate(FePlace place);

    public List<FeDate> listDate(String placeStr, int yearInt);

    public List<FePlace> listAirfield(FeYear year);

    public List<FePlace> listAirfield(int yearInt);

    public List<FeYear> listYear();

}
