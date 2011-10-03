package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.PeriodicJob;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;

public class PeriodicJobDAOobjectify extends DAOBase implements PeriodicJobDAO {

	static {
		ObjectifyService.register(PeriodicJob.class);
	}

	@Override
	public void removePeriodicJob(PeriodicJob periodicJob) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createOrUpdatePeriodicJob(PeriodicJob periodicJob) {
		ofy().put(periodicJob);
	}

	@Override
	public void updatePeriodicJobs(List<PeriodicJob> periodicJobs) {
		if (periodicJobs.size() > 0) {
			ofy().put(periodicJobs);
		}
	}

	@Override
	public List<PeriodicJob> listAllPeriodicJob() {
		Query<PeriodicJob> query = ofy().query(PeriodicJob.class);
		return query.list();
	}

	@Override
	public List<PeriodicJob> listExpiredPeriodicJob(long timeInMillis) {
		Query<PeriodicJob> query = ofy().query(PeriodicJob.class).filter("nextTimeInMillis <=", timeInMillis).filter("enabled ==", true);
		return query.list();
	}

	@Override
	public PeriodicJob getPeriodicJob(String name) {
		return ofy().get(PeriodicJob.class, name);
	}

}
