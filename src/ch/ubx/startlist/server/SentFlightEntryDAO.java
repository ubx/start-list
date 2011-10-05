package ch.ubx.startlist.server;

import java.util.Calendar;
import java.util.List;

import ch.ubx.startlist.shared.SentFlightEntry;

public interface SentFlightEntryDAO {

	public abstract List<SentFlightEntry> listFlightEntry(String sendExcel);

	public abstract SentFlightEntry getSentFlightEntry(String sendExcel, Long flightEntry);

	public abstract void createOrUpdateSentFlightEntry(SentFlightEntry sentFlightEntry);

	public abstract void addSentFlightEntries(List<SentFlightEntry> sentFlightEntries);

	public abstract void deleteSentFlightEntry(String sendExcel);

	public abstract void purgeSentFlightEntry(String sendExcel, Calendar date);

}
