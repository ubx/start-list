package ch.ubx.startlist.server;

import java.util.List;
import java.util.Map;

import ch.ubx.startlist.shared.Job;

public interface JobDAO {

	public void removeJob(Job job);

	public void createOrUpdateJob(Job job);

	public List<Job> listAllJob();

	public Map<String, Job> listJob(List<String> names);

}