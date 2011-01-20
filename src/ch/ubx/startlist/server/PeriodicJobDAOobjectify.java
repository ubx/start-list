package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.PeriodicJob;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class PeriodicJobDAOobjectify implements PeriodicJobDAO {

	static {
		ObjectifyService.register(PeriodicJob.class);
	}

	@Override
	public void removePeriodicJob(PeriodicJob periodicJob) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createOrUpdatePeriodicJob(PeriodicJob periodicJob) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(periodicJob);
	}

	@Override
	public void updatePeriodicJobs(List<PeriodicJob> periodicJobs) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<PeriodicJob> listAllPeriodicJob() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PeriodicJob> listExpiredPeriodicJob(long timeInMillis) {
		Objectify ofy = ObjectifyService.begin();
		Query<PeriodicJob> query = ofy.query(PeriodicJob.class).filter("nextTimeInMillis <=", timeInMillis);
		return query.list();
	}

}
