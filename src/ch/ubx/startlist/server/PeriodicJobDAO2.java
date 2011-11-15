package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.PeriodicJob2;

public interface PeriodicJobDAO2 {

	public void removePeriodicJob(PeriodicJob2 periodicJob);

	public void createOrUpdatePeriodicJob(PeriodicJob2 periodicJob);

	public void updatePeriodicJobs(List<PeriodicJob2> periodicJobs);

	public PeriodicJob2 getPeriodicJob(String name);

	public List<PeriodicJob2> listAllPeriodicJob();

	public List<PeriodicJob2> listExpiredPeriodicJob(long timeInMillis);
}
