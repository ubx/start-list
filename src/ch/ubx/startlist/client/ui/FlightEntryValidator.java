package ch.ubx.startlist.client.ui;

import java.util.Date;



import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class FlightEntryValidator {

	private FlightEntryListGUI gui = null;
	private DialogBox dialogBox;

	public FlightEntryValidator(FlightEntryListGUI gui) {
		this.gui = gui;

		dialogBox = new DialogBox();
		dialogBox.setAutoHideEnabled(true);
		dialogBox.setModal(false);
		Button closeButton = new Button("Close");
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		dialogBox.setWidget(closeButton);
		dialogBox.hide();
	}

	public boolean isValid() {
		Date startTime, landingTime;

		// Date
		{
			Date date = gui.dateBox.getValue();
			if (date.compareTo(new Date()) > 0) {
				showMessage(gui.dateBox, "Datum in der Zukunft!");
				return false;
			}
		}

		// Start time
		{
			TextBox tb = gui.startDateBox.getTextBox();
			if (tb != null) {
				if (!checkTime(tb.getValue())) {
					showMessage(gui.startDateBox, "Startzeit format mm:ss sein!");
					return false;
				}
			}

		}

		// Landing time glider
		{
			TextBox tb = gui.endGliderDateBox.getTextBox();
			if (tb != null) {
				if (!checkTime(tb.getValue())) {
					showMessage(gui.endGliderDateBox, "Landezeit Segelflugzeug format mm:ss sein!");
					return false;
				}

			}
		}
		
		// Landing time towplane
		{
			TextBox tb = gui.endTowplaneDateBox.getTextBox();
			if (tb != null) {
				if (!checkTime(tb.getValue())) {
					showMessage(gui.endTowplaneDateBox, "Landezeit Schleppflugzeug format mm:ss sein!");
					return false;
				}

			}
		}

		// Duration glider
		{
			startTime = gui.startDateBox.getValue();
			landingTime = gui.endGliderDateBox.getValue();
			if (startTime != null && landingTime != null) {
				if (!landingTime.after(startTime)) {
					showMessage(gui.startDateBox, "Startzeit SF vor oder gleich Landezeit!");
					return false;
				}
			}
		}
		
		// Duration towplane
		{
			startTime = gui.startDateBox.getValue();
			landingTime = gui.endTowplaneDateBox.getValue();
			if (startTime != null && landingTime != null) {
				if (!landingTime.after(startTime)) {
					showMessage(gui.startDateBox, "Startzeit Schlepper vor oder gleich Landezeit!");
					return false;
				}
			}
		}

		// Name of pilot
		{
			String pilot = gui.pilotNameBox.getValue();
			if (pilot.length() > 32) {
				showMessage(gui.pilotNameBox, "Piloten Name zu lang, maximum 32 Zeichen!");
				return false;
			}
		}
		
		// Name of passenger or instructor
		{
			String pilot = gui.passengerOrInstructorNameBox.getValue();
			if (pilot.length() > 32) {
				showMessage(gui.passengerOrInstructorNameBox, "Pax/FL Name zu lang, maximum 32 Zeichen!");
				return false;
			}
		}

		// Landing Place
		String lp = gui.allPlacesSuggestBox.getValue();
		if (lp.length() > 20) {
			showMessage(gui.registrationGliderBox, "Landeort zu lang, maximum 20 Zeichen!");
			return false;
		}
 

		// Registration glider
		{
			String regGlider = gui.registrationGliderBox.getValue();
			if (regGlider.length() > 7) {
				showMessage(gui.registrationGliderBox, "Immatrikulation SF zu lang, maximum 7 Zeichen!");
				return false;
			}
			if (regGlider.length() > 0)
			{
				if (!regGlider.matches("[A-Z]{1,2}-[A-Z0-9]{4}")) {
					showMessage(gui.registrationGliderBox, "Immatrikulation SF muss in der Form HB-1234 sein!");
					return false;	
				}	
			}

				
		}
		
		// Registration towplane
		{
			String regTowplane = gui.registrationTowplaneBox.getValue();
			if (regTowplane.length() > 7) {
				showMessage(gui.registrationTowplaneBox, "Immatrikulation Schlepper zu lang, maximum 7 Zeichen!");
				return false;
			}
			if (regTowplane.length() > 0)
			{
				if (!regTowplane.matches("[A-Z]{1,2}-[A-Z0-9]{3,4}")) {
					showMessage(gui.registrationTowplaneBox, "Immatrikulation Schlepper muss in der Form HB-WAR oder D-EFGH sein!");
					return false;	
				}			
			}
			
		}

		// Remarks
		{
			String rmk = gui.remarksTextBox.getValue();
			if (rmk.length() > 80) {
				showMessage(gui.remarksTextBox, "Text zu lang, maximum 80 Zeichen!");
				return false;
			}
		}

		// All OK!
		return true;
	}

	private boolean checkTime(String value) {
		if (value.length() == 0) {
			return true;
		}
		String[] mmss = value.split(":");
		if (mmss.length == 2) {
			int val;
			for (String ms : mmss) {
				try {
					val = Integer.parseInt(ms);
				} catch (Exception e) {
					return false;
				}
				if (val < 0 || val > 59) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	private void showMessage(Widget root, String msg) {
		dialogBox.setPopupPosition(root.getAbsoluteLeft(), root.getAbsoluteTop() + root.getOffsetHeight());
		dialogBox.setText(msg);
		dialogBox.show();
	}

}
