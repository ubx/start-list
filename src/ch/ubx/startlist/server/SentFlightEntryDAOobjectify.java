package ch.ubx.startlist.server;

import java.util.Calendar;
import java.util.List;

import ch.ubx.startlist.shared.SentFlightEntry;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;

public class SentFlightEntryDAOobjectify extends DAOBase implements SentFlightEntryDAO {

	static {
		ObjectifyService.register(SentFlightEntry.class);
	}

	@Override
	public List<SentFlightEntry> listFlightEntry(String sendExcel) {
		Query<SentFlightEntry> query = ofy().query(SentFlightEntry.class).filter("sendExcel", sendExcel);
		return query.list();
	}

	@Override
	public void addSentFlightEntries(List<SentFlightEntry> sentFlightEntries) {
		for (SentFlightEntry sentFlightEntry : sentFlightEntries) {
			ofy().put(sentFlightEntry);
		}
		// if (sentFlightEntries.size() > 0) {
		// ofy().put(sentFlightEntries);
		// }
	}

	@Override
	public void createOrUpdateSentFlightEntry(SentFlightEntry sentFlightEntry) {
		ofy().put(sentFlightEntry);

	}

	@Override
	public void deleteSentFlightEntry(String sendExcel) {
		Query<SentFlightEntry> query = ofy().query(SentFlightEntry.class).filter("sendExcel", sendExcel);
		ofy().delete(query.fetchKeys());
	}

	@Override
	public void purgeSentFlightEntry(String sendExcel, Calendar date) {
		Query<SentFlightEntry> query = ofy().query(SentFlightEntry.class).filter("sendExcel", sendExcel).filter("lastModified <", date.getTimeInMillis());
		ofy().delete(query.fetchKeys());
	}

	@Override
	public SentFlightEntry getSentFlightEntry(String sendExcel, Long flightEntry) {
		Query<SentFlightEntry> query = ofy().query(SentFlightEntry.class).filter("sendExcel", sendExcel).filter("flightEntry", flightEntry);
		return query.get();
	}

}
