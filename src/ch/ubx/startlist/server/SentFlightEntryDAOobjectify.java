package ch.ubx.startlist.server;

import java.util.Calendar;
import java.util.List;

import ch.ubx.startlist.shared.PeriodicJob;
import ch.ubx.startlist.shared.SentFlightEntry;

import com.google.appengine.api.datastore.QueryResultIterable;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class SentFlightEntryDAOobjectify implements SentFlightEntryDAO {

	static {
		ObjectifyService.register(SentFlightEntry.class);
	}

	@Override
	public List<SentFlightEntry> listFlightEntry(String sendExcel) {
		Objectify ofy = ObjectifyService.begin();
		Query<SentFlightEntry> query = ofy.query(SentFlightEntry.class).filter("sendExcel", sendExcel);
		return query.list();
	}

	@Override
	public void addSentFlightEntries(List<SentFlightEntry> sentFlightEntries) {
		if (sentFlightEntries.size() > 0) {
			Objectify ofy = ObjectifyService.begin();
			ofy.put(sentFlightEntries);
		}
	}

	@Override
	public void createOrUpdateSentFlightEntry(SentFlightEntry sentFlightEntry) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(sentFlightEntry);

	}

	@Override
	public void deleteSentFlightEntry(String sendExcel) {
		Objectify ofy = ObjectifyService.begin();
		Query<SentFlightEntry> query = ofy.query(SentFlightEntry.class).filter("sendExcel", sendExcel);
		ofy.delete(query.fetchKeys());
	}

	@Override
	public void purgeSentFlightEntry(String sendExcel, Calendar date) {
		Objectify ofy = ObjectifyService.begin();
		Query<SentFlightEntry> query = ofy.query(SentFlightEntry.class).filter("sendExcel", sendExcel).filter("lastModified <", date.getTimeInMillis());
		ofy.delete(query.fetchKeys());
	}

	@Override
	public SentFlightEntry getSentFlightEntry(String sendExcel, Long flightEntry) {
		Objectify ofy = ObjectifyService.begin();
		Query<SentFlightEntry> query = ofy.query(SentFlightEntry.class).filter("sendExcel", sendExcel).filter("flightEntry", flightEntry);
		return query.get();
	}

}
