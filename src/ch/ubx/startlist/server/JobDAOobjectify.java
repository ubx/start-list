package ch.ubx.startlist.server;

import java.util.List;
import java.util.Map;

import ch.ubx.startlist.shared.Job;
import ch.ubx.startlist.shared.JobImportOLC;
import ch.ubx.startlist.shared.JobSendExcel;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;

public class JobDAOobjectify extends DAOBase implements JobDAO {

	static {
		ObjectifyService.register(JobSendExcel.class);
		ObjectifyService.register(JobImportOLC.class);
	}

	@Override
	public void removeJob(Job sendExcel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createOrUpdateJob(Job job) {
		ofy().put(job);
	}

	@Override
	public List<Job> listAllJob() {
		Query<Job> query = ofy().query(Job.class);
		return query.list();
	}

	@Override
	public Map<String, Job> listJob(List<String> names) {
		return ofy().get(Job.class, names);
	}

}
