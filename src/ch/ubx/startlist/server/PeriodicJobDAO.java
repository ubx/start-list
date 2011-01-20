package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.PeriodicJob;

public interface PeriodicJobDAO {

	public void removePeriodicJob(PeriodicJob periodicJob);

	public void createOrUpdatePeriodicJob(PeriodicJob periodicJob);

	public void updatePeriodicJobs(List<PeriodicJob> periodicJobs);

	public List<PeriodicJob> listAllPeriodicJob();

	public List<PeriodicJob> listExpiredPeriodicJob(long timeInMillis);
}
