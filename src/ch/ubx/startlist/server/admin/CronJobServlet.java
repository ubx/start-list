package ch.ubx.startlist.server.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.utils.SystemProperty;

import ch.ubx.startlist.server.ExcelSheet;
import ch.ubx.startlist.server.FlightEntryDAOobjectify;
import ch.ubx.startlist.server.ImportOLCDAOobjectify;
import ch.ubx.startlist.server.SendExcelDAOobjectify;
import ch.ubx.startlist.shared.FlightEntry;
import ch.ubx.startlist.shared.ImportOLC;
import ch.ubx.startlist.shared.SendExcel;
import ch.ubx.startlist.shared.TextConstants;

public class CronJobServlet extends HttpServlet implements TextConstants {

	private static final long serialVersionUID = 1L;
	private SendExcelDAOobjectify sendExcelDAO = new SendExcelDAOobjectify();
	private ImportOLCDAOobjectify importOLCDAO = new ImportOLCDAOobjectify();
	private FlightEntryDAOobjectify flightEntryDAO = new FlightEntryDAOobjectify();
	private static final Logger log = Logger.getLogger(CronJobServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.log(Level.INFO, req.getRequestURI());
		importFromOLC();
		sendMail();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.log(Level.INFO, req.getRequestURI());
		doGet(req, resp);
	}

	/**
	 * Send e-mail with an excel sheet for all SendExcel.
	 * 
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private void sendMail() throws IOException {
		Calendar now = Calendar.getInstance();
		String applicationId = SystemProperty.applicationId.get();
		List<SendExcel> sendExcels = sendExcelDAO.listAllSendExcel();
		for (SendExcel sendExcel : sendExcels) {
			String sheetname = String.format("%1$tY%1$tm%1$te", now) + "-" + sendExcel.getPlace().replace(" ", "_");
			List<FlightEntry> filteredFlightEntries = new ArrayList<FlightEntry>();
			List<FlightEntry> flightEntries = flightEntryDAO.listflightEntry(now, sendExcel.getPlace());
			if (!flightEntries.isEmpty()) {

				// filter
				Set<String> filterGliders = new TreeSet<String>();
				if (sendExcel.getFilterGliders() != null) {
					filterGliders.addAll(Arrays.asList(sendExcel.getFilterGliders().split(";")));
				}
				Set<String> filterTowplanes = new TreeSet<String>();
				if (sendExcel.getFilterTowplanes() != null) {
					filterTowplanes.addAll(Arrays.asList(sendExcel.getFilterTowplanes().split(";")));
				}
				for (FlightEntry flightEntry : flightEntries) {
					if (filterGliders.isEmpty() & filterTowplanes.isEmpty()) {
						filteredFlightEntries.add(flightEntry);
					} else if (flightEntry.getRegistrationGlider() != null && filterGliders.contains(flightEntry.getRegistrationGlider())
							|| (flightEntry.getRegistrationTowplane() != null && filterTowplanes.contains(flightEntry.getRegistrationTowplane()))) {
						filteredFlightEntries.add(flightEntry);
					}
				}
			}

			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			try {
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress("admin@" + applicationId + ".appspotmail.com"));
				for (String recipient : sendExcel.getRecipientsList()) {
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
				}
				msg.setSubject(sendExcel.getSubject());

				Multipart mp = new MimeMultipart();
				log.log(Level.INFO, "Create excel for FlighEnties: " + filteredFlightEntries.size());
				// Excel attachment
				if (!filteredFlightEntries.isEmpty()) {
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					ExcelSheet.createExcel(filteredFlightEntries, outputStream, sheetname);
					MimeBodyPart attachmentBP = new MimeBodyPart();
					attachmentBP.setFileName(URLEncoder.encode(sheetname + ".xls", "UTF-8"));
					attachmentBP.setDisposition(Part.ATTACHMENT);
					DataSource src = new ByteArrayDataSource(outputStream.toByteArray(), "application/x-ms-excel");
					DataHandler handler = new DataHandler(src);
					attachmentBP.setDataHandler(handler);
					mp.addBodyPart(attachmentBP);
				}

				// Message body
				MimeBodyPart plainBody = new MimeBodyPart();
				plainBody.setContent(filteredFlightEntries.isEmpty() ? TXT_NO_FLIGHT_TODAY : TXT_FLIGHT_FOR_TODY_IN_EXCEL_FILE, "text/plain");
				plainBody.setFileName("body.txt");
				mp.addBodyPart(plainBody);
				msg.setText(sendExcel.getSubject());
				msg.setContent(mp);
				msg.saveChanges();
				Transport.send(msg);
			} catch (AddressException e) {
				// ...
			} catch (MessagingException e) {
				// ...
			}
		}
	}

	/**
	 * Import for all ImportOLC from OLC web page;
	 */
	private void importFromOLC() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		List<ImportOLC> importOLCs = importOLCDAO.listAllImportOLC();
		for (ImportOLC importOLC : importOLCs) {
			List<String> places = importOLC.getPlacesList();
			for (String place : places) {
				// Split requests into small pieces to avoid DeadlineExceededException for the whole request.
				int maxImport = 50;
				while (OlcImportMain.importFromOLC(place, year, 20).size() > 0 & maxImport > 0) {
					--maxImport;
				}
			}
		}
	}
}
