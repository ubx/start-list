package ch.ubx.startlist.server.maint;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.ubx.startlist.server.ImportOLCDAO;
import ch.ubx.startlist.server.ImportOLCDAOobjectify;
import ch.ubx.startlist.server.JobDAO;
import ch.ubx.startlist.server.JobDAOobjectify;
import ch.ubx.startlist.server.PeriodicJobDAO;
import ch.ubx.startlist.server.PeriodicJobDAO2;
import ch.ubx.startlist.server.PeriodicJobDAOobjectify;
import ch.ubx.startlist.server.PeriodicJobDAOobjectify2;
import ch.ubx.startlist.server.SendExcelDAO;
import ch.ubx.startlist.server.SendExcelDAOobjectify;
import ch.ubx.startlist.shared.ImportOLC;
import ch.ubx.startlist.shared.JobImportOLC;
import ch.ubx.startlist.shared.JobSendExcel;
import ch.ubx.startlist.shared.PeriodicJob;
import ch.ubx.startlist.shared.PeriodicJob2;
import ch.ubx.startlist.shared.SendExcel;

public class MigrateJobs {
	private static final Logger log = Logger.getLogger(MigrateJobs.class.getName());

	private static final PeriodicJobDAO dao = new PeriodicJobDAOobjectify();
	private static final PeriodicJobDAO2 dao2 = new PeriodicJobDAOobjectify2();
	private static final SendExcelDAO sendExceldao = new SendExcelDAOobjectify();
	private static final ImportOLCDAO importOLCdao = new ImportOLCDAOobjectify();
	private static final JobDAO jobdao = new JobDAOobjectify();

	public static void migrate() {
		log.log(Level.INFO, "Start migration...");
		List<PeriodicJob> periodicJobs = dao.listAllPeriodicJob();
		for (PeriodicJob periodicJob : periodicJobs) {
			PeriodicJob2 periodicJob2 = new PeriodicJob2();
			periodicJob2.setDays(periodicJob.getDays());
			periodicJob2.setEnabled(periodicJob.isEnabled());
			periodicJob2.setName(periodicJob.getName());
			periodicJob2.setNextTimeInMillis(periodicJob.getNextTimeInMillis());
			periodicJob2.setTime(periodicJob.getTime());
			periodicJob2.setTimeZone(periodicJob.getTimeZone());
			periodicJob2.setJobs(periodicJob.getImportOLCJobs() + ";" + periodicJob.getSendExcels());
			dao2.createOrUpdatePeriodicJob(periodicJob2);
			dao.removePeriodicJob(periodicJob);
		}
		log.log(Level.INFO, "PeriodicJob migrated: " + dao2.listAllPeriodicJob().size());

		Set<String> names = new HashSet<String>();
		List<SendExcel> sendExcels = sendExceldao.listAllSendExcel();
		for (SendExcel sendExcel : sendExcels) {
			JobSendExcel job = new JobSendExcel();
			job.setDaysBehind(sendExcel.getDaysBehind());
			job.setFilterGliders(sendExcel.getFilterGliders());
			job.setFilterTowplanes(sendExcel.getFilterTowplanes());
			job.setName(sendExcel.getName());
			job.setPlace(sendExcel.getPlace());
			job.setRecipients(sendExcel.getRecipients());
			job.setSubject(sendExcel.getSubject());
			jobdao.createOrUpdateJob(job);
			sendExceldao.removeSendExcel(sendExcel);
			names.add(job.getName());
		}

		int size = jobdao.listAllJob().size();
		log.log(Level.INFO, "SendExcel migrated: " + size);

		List<ImportOLC> olcImport = importOLCdao.listAllImportOLC();
		for (ImportOLC importOLC : olcImport) {
			JobImportOLC job = new JobImportOLC();
			String n = importOLC.getName();
			int i = 2;
			while (names.contains(n)) {
				n = n + i++;
			}
			job.setName(n);
			job.setPlaces(importOLC.getPlaces());
			jobdao.createOrUpdateJob(job);
			importOLCdao.removeImportOLC(importOLC);
			names.add(job.getName());
		}
		log.log(Level.INFO, "ImportOLC migrated: " + (jobdao.listAllJob().size() - size));
		log.log(Level.INFO, "...end migration");

	}

}
