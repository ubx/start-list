package ch.ubx.startlist.client.admin;

import ch.ubx.startlist.client.admin.ui.AdminGUI;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;

public class AdminGUIevents {

	protected static final String SubmitCompleteEvent = null;
	private AdminGUI gui;

	public AdminGUIevents(AdminGUI gui) {
		this.gui = gui;
		wireGUIEvents();

	}

	private void wireGUIEvents() {

		// Add a 'submit' button.
		gui.analyzeFileButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				gui.gui_eventAnalyzeButtonClicked();
			}
		});
		
		gui.storeFileButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				gui.gui_eventStoreFileButtonClicked();
			}
		});

		gui.addSubmitHandler(new FormPanel.SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				gui.eventSubmit(event);
			}
		});

		gui.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				gui.eventSubmitComplete(event);
			}
		});

		gui.excelYearListBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				gui.gui_eventExcelYearListBoxChanged();
			}
		});


		gui.olcImportFlightEntryButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				gui.olcImportAirfieldPushButtonClicked();
			}
		});
	}
}
