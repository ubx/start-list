package ch.ubx.startlist.server.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
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

import ch.ubx.startlist.client.FlightEntry;
import ch.ubx.startlist.client.ImportOLC;
import ch.ubx.startlist.client.SendExcel;
import ch.ubx.startlist.server.ExcelSheet;
import ch.ubx.startlist.server.FlightEntryDAOobjectify;
import ch.ubx.startlist.server.ImportOLCDAOobjectify;
import ch.ubx.startlist.server.SendExcelDAOobjectify;

public class CronJobServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private SendExcelDAOobjectify sendExcelDAO = new SendExcelDAOobjectify();
	private ImportOLCDAOobjectify importOLCDAO = new ImportOLCDAOobjectify();
	private FlightEntryDAOobjectify flightEntryDAO = new FlightEntryDAOobjectify();
	private static final Logger log = Logger.getLogger(CronJobServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.log(Level.INFO, req.getRequestURI());
		// FOR TEST ONLY
		if (sendExcelDAO.listAllSendExcel().size() == 0) {
			SendExcel sendExcelTest = new SendExcel("Hendrik Gariep D Test", "Generated Excel file for Hendrik Gariep D", "Hendrik Gariep D",
					"andreas.luethi@gmx.net");
			sendExcelDAO.createOrUpdateSendExcel(sendExcelTest);
		}
		// FOR TEST ONLY
		if (importOLCDAO.listAllImportOLC().size() == 0) {
			ImportOLC importOLCTest = new ImportOLC("Hendrik Gariep D Test", "Hendrik Gariep D");
			importOLCDAO.createOrUpdateImportOLC(importOLCTest);
		}

		sendMail();
		//importFromOLC();
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
		String label = "today"; // TODO - what should be here?

		List<SendExcel> sendExcels = sendExcelDAO.listAllSendExcel();
		for (SendExcel sendExcel : sendExcels) {
			List<FlightEntry> flightEntries = flightEntryDAO.listflightEntry(now, sendExcel.getPlace());
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ExcelSheet.createExcel(flightEntries, outputStream, label);

			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			try {
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress("xxx.yyy@zzz.com")); // TODO - what e-mail should go here?
				for (String recipient : sendExcel.getRecipientsList()) {
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
				}
				msg.setSubject(sendExcel.getSubject());

				// Excel attachment
				Multipart mp = new MimeMultipart();
				MimeBodyPart attachmentBP = new MimeBodyPart();
				attachmentBP.setFileName(URLEncoder.encode(label + ".xls", "UTF-8"));
				attachmentBP.setDisposition(Part.ATTACHMENT);
				DataSource src = new ByteArrayDataSource(outputStream.toByteArray(), "application/x-ms-excel");
				DataHandler handler = new DataHandler(src);
				attachmentBP.setDataHandler(handler);
				mp.addBodyPart(attachmentBP);

				// Message body
				MimeBodyPart plainBody = new MimeBodyPart();
				plainBody.setContent(sendExcel.getSubject(), "text/plain");
				plainBody.setFileName("plainbody.txt");
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
				int max = OlcImportMain.getMaxImportatOnce() * 200;
				while (max > 0 & OlcImportMain.importFromOLC(place, year).size() > 0) {
					max = max - 1;
				}
			}
		}
	}
}
