package ch.ubx.startlist.server.admin;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.ubx.startlist.server.ExcelSender;
import ch.ubx.startlist.server.OLCImporter;
import ch.ubx.startlist.server.PeriodicJobDAO;
import ch.ubx.startlist.server.PeriodicJobDAOobjectify;
import ch.ubx.startlist.shared.PeriodicJob;
import ch.ubx.startlist.shared.TextConstants;

public class CronJobServlet extends HttpServlet implements TextConstants {

	private static final long serialVersionUID = 1L;

	private static PeriodicJobDAO periodicJobDAO = new PeriodicJobDAOobjectify();
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
	private static SimpleDateFormat nowTimeFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm z");

	private static final Logger log = Logger.getLogger(CronJobServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.log(Level.INFO, req.getRequestURI());

		Calendar now = Calendar.getInstance();

		// For testing, the now parameter sets the time!
		String nowStr = req.getParameter("now");
		if (nowStr != null) {
			try {
				now.setTimeInMillis(nowTimeFormat.parse(nowStr).getTime());
			} catch (ParseException e) {
				log.log(Level.SEVERE, "Could not parse test time=" + nowStr);
			}
		}

		// Get all expired (or not yet initialized, i.e. timeInMillis == 0).
		List<PeriodicJob> periodicJobs = periodicJobDAO.listExpiredPeriodicJob(now.getTimeInMillis());
		List<PeriodicJob> periodicJobsMod = new ArrayList<PeriodicJob>();

		// Adjust all with timeInMillis == 0
		for (PeriodicJob periodicJob : periodicJobs) {
			if (periodicJob.getNextTimeInMillis() == 0) {
				adjustTimeInMillis(periodicJob, now);
				periodicJobsMod.add(periodicJob);
			}
		}

		// Run all expired jobs
		for (PeriodicJob periodicJob : periodicJobs) {
			if (periodicJob.getNextTimeInMillis() <= now.getTimeInMillis()) {
				OLCImporter.doImport(periodicJob.getImportOLCJobList());
				ExcelSender.doSend(periodicJob.getSendExcelJobList(), now);
				adjustTimeInMillis(periodicJob, now);
				periodicJobsMod.add(periodicJob);
			}
		}

		// // Adjust all timeInMillis for next execution
		// for (PeriodicJob periodicJob : periodicJobs) {
		// adjustTimeInMillis(periodicJob, now);
		// }

		// Update all
		if (periodicJobsMod.size() > 0) {
			periodicJobDAO.updatePeriodicJobs(periodicJobs);
		}
	}

	protected void adjustTimeInMillis(PeriodicJob periodicJob, Calendar now) {
		log.log(Level.INFO, "For periodicJob=" + periodicJob.getName());

		// Parse time string
		Date time = null;
		try {
			String zoneStr = periodicJob.getTimeZone();
			timeFormat.setTimeZone(TimeZone.getTimeZone(zoneStr == null ? "UTC" : zoneStr));
			time = timeFormat.parse(periodicJob.getTime());
		} catch (ParseException e) {
			log.log(Level.WARNING, "Could not parse time string=" + periodicJob.getTime() + ", expecting " + timeFormat.toPattern());
			return;
		}

		// set next day
		Calendar nextTime = Calendar.getInstance();
		nextTime.setTimeZone(timeFormat.getTimeZone());
		nextTime.setTime(time);
		nextTime.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

		int curDayOfWeek = nextTime.get(Calendar.DAY_OF_WEEK) - 1; // Start with 0
		boolean days[] = periodicJob.getDays();
		for (int i = curDayOfWeek; i < days.length * 2; i++) {
			int daysToAdd = i - curDayOfWeek;
			if (days[i % days.length]) {
				// TODO -- this is a temporary solution:
				// NOTE: we assume here that the periodic jobs run on a hourly basis, so we do nothing an hour before midnight!!
				if (daysToAdd > 0 || periodicJob.getNextTimeInMillis() == 0 || nextTime.get(Calendar.HOUR_OF_DAY) == 23) {
					nextTime.add(Calendar.DATE, daysToAdd);
					periodicJob.setNextTimeInMillis(nextTime.getTimeInMillis());
					log.log(Level.INFO, "Next day(DAY_OF_WEEK)=" + nextTime.get(Calendar.DAY_OF_WEEK));
					break;
				}
			}
			if (daysToAdd > 7) {
				log.log(Level.WARNING, "Could not set next day");
				return;
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.log(Level.INFO, req.getRequestURI());
		doGet(req, resp);
	}

}
