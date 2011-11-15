package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.PeriodicJob2;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;

public class PeriodicJobDAOobjectify2 extends DAOBase implements PeriodicJobDAO2 {

	static {
		ObjectifyService.register(PeriodicJob2.class);
	}

	@Override
	public void removePeriodicJob(PeriodicJob2 periodicJob) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createOrUpdatePeriodicJob(PeriodicJob2 periodicJob) {
		ofy().put(periodicJob);
	}

	@Override
	public void updatePeriodicJobs(List<PeriodicJob2> periodicJobs) {
		if (periodicJobs.size() > 0) {
			ofy().put(periodicJobs);
		}
	}

	@Override
	public List<PeriodicJob2> listAllPeriodicJob() {
		Query<PeriodicJob2> query = ofy().query(PeriodicJob2.class);
		return query.list();
	}

	@Override
	public List<PeriodicJob2> listExpiredPeriodicJob(long timeInMillis) {
		Query<PeriodicJob2> query = ofy().query(PeriodicJob2.class).filter("nextTimeInMillis <=", timeInMillis).filter("enabled ==", true);
		return query.list();
	}

	@Override
	public PeriodicJob2 getPeriodicJob(String name) {
		return ofy().get(PeriodicJob2.class, name);
	}

}
