package ch.ubx.startlist.client.ui;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ch.ubx.startlist.client.Airfield;
import ch.ubx.startlist.client.AirfieldServiceDelegate;
import ch.ubx.startlist.client.FlightEntry;
import ch.ubx.startlist.client.FlightEntryListeProvider;
import ch.ubx.startlist.client.FlightEntryServiceDelegate;
import ch.ubx.startlist.client.GwtUtil;
import ch.ubx.startlist.client.LoginInfo;
import ch.ubx.startlist.client.LoginServiceDelegate;
import ch.ubx.startlist.client.RowSelectionHandler;
import ch.ubx.startlist.client.TextConstants;
import ch.ubx.startlist.client.TimeFormat;
import ch.ubx.startlist.client.admin.ui.AdminGUI;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FlightEntryListGUI implements TimeFormat, TextConstants {

	private static final String STATUS_ROOT_PANEL = "flightEntryStatus";
	private static final String STACK_ROOT_PANEL = "flightEntryToolBar";
	private static final String LOGIN_ROOT_PANEL = "loginrootpanel";
	private static final String TXT_CLOSE = "Close";

	/* GUI Widgets */
	protected Button addButton, updateButton;
	public Button newButton;
	public Button modifyButton;
	public Button deleteButton;
	public Button discardButton;
	public Button saveButton;

	protected TextBox dateField;
	protected TextBox pilotField;
	protected TextBox startField;
	protected TextBox endField;
	protected TextBox registrationBox;

	protected DateBox2 startDateBox;
	protected DateBox2 endDateBox;

	protected Label status;

	protected StackPanel stackPanel;

	public ListBox placeListBox;
	public ListBox yearListBox;

	public ListBox dateListBox;

	public Button prevDayPushButton;
	public Button nextDayPushButton;
	public FlexTable flightEntryFlexTable;

	protected DateBox2 dateBox;
	protected TextBox pilotNameBox;
	protected CheckBox trainingCheckBox;
	protected TextBox remarksTextBox;
	protected SuggestBox2 allPlacesSuggestBox;

	protected TextBox userTextBox;
	protected PasswordTextBox passwordTextBox;
	protected Button btnLoginOkButton;
	public Button btnClose;

	public FlightEntryServiceDelegate flightEntryService;
	public LoginServiceDelegate loginServiceDelegate;
	public AirfieldServiceDelegate airfieldServiceDelegate;

	/* Data model */
	private FlightEntry currentFlightEntry;
	private VerticalPanel mainPanel;
	private DynaTableWidget dynaTableWidget;

	private FlightEntryListeProvider provider = new FlightEntryListeProvider();

	private VerticalPanel verticaPanel_2;
	private RowSelectionHandler rowSelectionHandler = null;
	private Map<String, Long> strToDate = new LinkedHashMap<String, Long>();
	private ListBox allPlacesListBox;
	private MultiWordSuggestOracle allPlacesSuggest = new MultiWordSuggestOracle();
	private Set<Airfield> allAirfields;
	private DialogBox flightEntryDialogBox;
	private FlightEntryValidator validator;
	private HorizontalPanel operationNewModDel;
	private Anchor signInLink;
	private Label loggedInAs;
	private LoginInfo currentLoginInfo;
	private HTML excelLinkHTML;

	private AdminGUI adminGUI;
	private FlightEntry lastflightEntry;

	/**
	 * @wbp.parser.entryPoint
	 */
	public void init() {

		buildForm();
		placeWidgets();
	}

	private void buildForm() {
	}

	private void placeWidgets() {

		// Login panel
		HorizontalPanel loginPanel = new HorizontalPanel();
		RootPanel.get(LOGIN_ROOT_PANEL).add(loginPanel, 10, 60);
		signInLink = new Anchor();
		loginPanel.add(signInLink);
		loggedInAs = new Label();
		loginPanel.add(loggedInAs);

		HorizontalPanel statusPanel = new HorizontalPanel();
		RootPanel.get(STATUS_ROOT_PANEL).add(statusPanel, 10, 100);
		status = new Label();
		statusPanel.add(status);

		stackPanel = new StackPanel();
		RootPanel.get(STACK_ROOT_PANEL).add(stackPanel, 10, 130);

		mainPanel = new VerticalPanel();
		stackPanel.add(mainPanel, TXT_STARTLIST, false);

		HorizontalPanel selectionPanel = new HorizontalPanel();
		selectionPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		selectionPanel.addStyleName("selectionPanel");
		mainPanel.add(selectionPanel);

		Label yearLabel = new Label(TXT_YEAR);
		selectionPanel.add(yearLabel);

		yearListBox = new ListBox();
		selectionPanel.add(yearListBox);
		yearListBox.setVisibleItemCount(1);

		Label ortLabel = new Label(TXT_START_PLACE);
		selectionPanel.add(ortLabel);

		placeListBox = new ListBox();
		selectionPanel.add(placeListBox);
		placeListBox.setVisibleItemCount(1);

		HorizontalPanel datePanel = new HorizontalPanel();
		datePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		datePanel.addStyleName("datePanel");
		selectionPanel.add(datePanel);

		Label lblDatum = new Label(TXT_FLIGHT_DATUM);
		datePanel.add(lblDatum);

		prevDayPushButton = new Button(TXT_PREV);
		datePanel.add(prevDayPushButton);

		dateListBox = new ListBox();
		datePanel.add(dateListBox);
		dateListBox.setVisibleItemCount(1);

		nextDayPushButton = new Button(TXT_NEXT);
		datePanel.add(nextDayPushButton);

		Label dummyLabel = new Label();
		dummyLabel.setWidth("20px");
		selectionPanel.add(dummyLabel);

		excelLinkHTML = new HTML();
		selectionPanel.add(excelLinkHTML);

		String[] columns = new String[] { TXT_PILOT, TXT_SHORT_REGISTRATION, TXT_START_PLACE, TXT_START_TIME, TXT_LANDING_PLACE, TXT_LANDING_TIME,
				TXT_DURATION, TXT_SHORT_TRAINING, TXT_REMARKS };
		String[] styles = new String[] { "pilot", "registration", "start", "starttime", "end", "endtime", "dauer", "schulung", "bemerkungen" };

		verticaPanel_2 = new VerticalPanel();
		mainPanel.add(verticaPanel_2);
		dynaTableWidget = new DynaTableWidget(provider, columns, styles, 20);
		dynaTableWidget.setWidth("1000px");
		verticaPanel_2.add(dynaTableWidget);
		dynaTableWidget.setStatusText("");

		operationNewModDel = new HorizontalPanel();
		operationNewModDel.setVisible(false);
		verticaPanel_2.add(operationNewModDel);

		newButton = new Button(TXT_NEW);
		newButton.setEnabled(true);
		operationNewModDel.add(newButton);

		modifyButton = new Button(TXT_MODIFY);
		modifyButton.setEnabled(false);
		operationNewModDel.add(modifyButton);

		deleteButton = new Button(TXT_DELETE);
		deleteButton.setEnabled(false);
		operationNewModDel.add(deleteButton);

		flightEntryDialogBox = new DialogBox();
		VerticalPanel flightEntryVerticaPanel = new VerticalPanel();
		flightEntryDialogBox.add(flightEntryVerticaPanel);
		flightEntryDialogBox.hide();
		flightEntryDialogBox.setModal(true);

		flightEntryFlexTable = new FlexTable();
		flightEntryVerticaPanel.add(flightEntryFlexTable);

		Label lblDatum2 = new Label(TXT_FLIGHT_DATUM);
		flightEntryFlexTable.setWidget(0, 0, lblDatum2);

		dateBox = new DateBox2();
		dateBox.setFormat(DD_MMM_YYYY_FORMAT);
		flightEntryFlexTable.setWidget(0, 1, dateBox);

		Label lblStart = new Label(TXT_START_TIME);
		flightEntryFlexTable.setWidget(0, 2, lblStart);

		startDateBox = new DateBox2();
		startDateBox.setFormat(MM_HH_FORMAT);
		startDateBox.getDatePicker().setVisible(false);
		flightEntryFlexTable.setWidget(0, 3, startDateBox);

		Label lblEnde = new Label(TXT_LANDING_TIME);
		flightEntryFlexTable.setWidget(0, 4, lblEnde);

		endDateBox = new DateBox2();
		endDateBox.setFormat(MM_HH_FORMAT);
		endDateBox.getDatePicker().setVisible(false);
		flightEntryFlexTable.setWidget(0, 5, endDateBox);

		Label lblPilot = new Label(TXT_PILOT);
		flightEntryFlexTable.setWidget(1, 0, lblPilot);

		pilotNameBox = new TextBox();
		flightEntryFlexTable.setWidget(1, 1, pilotNameBox);

		Label lblAllPlaces = new Label(TXT_START_PLACE);
		flightEntryFlexTable.setWidget(1, 2, lblAllPlaces);

		allPlacesListBox = new ListBox();
		flightEntryFlexTable.setWidget(1, 3, allPlacesListBox);

		Label lblAllSuggestPlaces = new Label(TXT_LANDING_PLACE);
		flightEntryFlexTable.setWidget(1, 4, lblAllSuggestPlaces);

		allPlacesSuggestBox = new SuggestBox2(allPlacesSuggest);
		flightEntryFlexTable.setWidget(1, 5, allPlacesSuggestBox);

		Label lblPlane = new Label(TXT_REGISTRATION);
		flightEntryFlexTable.setWidget(2, 0, lblPlane);

		registrationBox = new TextBox();
		flightEntryFlexTable.setWidget(2, 1, registrationBox);

		Label lblSchulung = new Label(TXT_TRAINING);
		flightEntryFlexTable.setWidget(2, 2, lblSchulung);

		trainingCheckBox = new CheckBox();
		flightEntryFlexTable.setWidget(2, 3, trainingCheckBox);

		Label lblBemerkungen = new Label(TXT_REMARKS);
		flightEntryFlexTable.setWidget(3, 0, lblBemerkungen);

		remarksTextBox = new TextBox();
		remarksTextBox.setVisibleLength(80);
		flightEntryFlexTable.setWidget(3, 1, remarksTextBox);
		flightEntryFlexTable.getFlexCellFormatter().setColSpan(3, 1, 5);// TODO
		// -
		// does
		// not
		// work?

		// Set tab order
		setTabOrder(dateBox, pilotNameBox, registrationBox, startDateBox, endDateBox, allPlacesListBox, allPlacesSuggestBox, trainingCheckBox, remarksTextBox);

		HorizontalPanel operationsPanel;
		operationsPanel = new HorizontalPanel();
		operationsPanel.setSpacing(5);
		flightEntryVerticaPanel.add(operationsPanel);

		saveButton = new Button(TXT_SAVE);
		saveButton.setEnabled(false);
		operationsPanel.add(saveButton);

		discardButton = new Button(TXT_DISCARD);
		discardButton.setEnabled(false);
		operationsPanel.add(discardButton);

		btnClose = new Button(TXT_CLOSE);
		btnClose.setEnabled(true);
		operationsPanel.add(btnClose);

		dateField = new TextBox();
		pilotField = new TextBox();
		startField = new TextBox();

		loadYears();

		loadAllPlaces();

		enablePilotFields(false);

		initLogin();
	}

	public void loadYears() {
		flightEntryService.listYears();
	}

	private void initLogin() {
		loginServiceDelegate.login(GWT.getHostPageBaseURL());
	}

	private void loadPlaces(int year) {
		flightEntryService.listPlaces(year);
	}

	private void loadAllPlaces() {
		airfieldServiceDelegate.listAirfields();
	}

	private void reload() {
		reload(null);
	}

	private void reload(FlightEntry flightEntry) {
		if (flightEntry != null) {
			provider.setCurrentPlace(flightEntry.getPlace());
			provider.setCurrentDate(flightEntry.getStartTimeInMillis());
		} else {
			provider.setCurrentPlace(placeListBox.getItemCount() > 0 ? placeListBox.getItemText(placeListBox.getSelectedIndex()) : "");
			provider.setCurrentDate(strToDate.get(dateListBox.getItemText(dateListBox.getSelectedIndex())));
		}
		lastflightEntry = flightEntry;
		dynaTableWidget.reload();

		if (rowSelectionHandler == null) {
			rowSelectionHandler = new RowSelectionHandler() {
				@Override
				public void rowSelected(int row, boolean selected) {
					enablePilotFields(false);
					newButton.setEnabled(!selected);
					if (selected) {
						FlightEntry flightEntry = provider.getFlightEntry(row);
						if (flightEntry == null) {
							clearForm();
						} else {
							enablePilotFields(false);
							loadForm(flightEntry);
							enableCUDButtons();
						}
					} else {
						clearForm();
					}
				}
			};
			dynaTableWidget.addRowSelectionHandler(rowSelectionHandler);
		}
	}

	private void enablePilotFields(boolean enable) {
		dateBox.setEnabled(enable);
		pilotNameBox.setEnabled(enable);
		startDateBox.setEnabled(enable);
		endDateBox.setEnabled(enable);
		trainingCheckBox.setEnabled(enable);
		remarksTextBox.setEnabled(enable);
		allPlacesListBox.setEnabled(enable);
		allPlacesSuggestBox.setEnabled(enable);
		registrationBox.setEnabled(enable);
	}

	private void clearForm() {
		disableCUDButtons();
		currentFlightEntry = null;
		dateBox.setValue(null);
		pilotNameBox.setValue(null);
		startDateBox.setValue(null);
		endDateBox.setValue(null);
		trainingCheckBox.setValue(false);
		remarksTextBox.setValue(null);
		allPlacesListBox.clear();
		allPlacesSuggestBox.setValue(null);
		registrationBox.setValue(null);
	}

	private void disableCUDButtons() {
		modifyButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}

	private void enableCUDButtons() {
		modifyButton.setEnabled(true);
		deleteButton.setEnabled(true);
		disableSCButtons();
	}

	private void enableSCButtons(boolean enable) {
		saveButton.setEnabled(enable);
		discardButton.setEnabled(enable);
		btnClose.setEnabled(!enable);
	}

	private void disableSCButtons() {
		saveButton.setEnabled(false);
		discardButton.setEnabled(false);
	}

	private void loadForm(FlightEntry flightEntry) {
		boolean newEntry = flightEntry.getId() == null;
		currentFlightEntry = flightEntry;

		// Date date = newEntry ? new Date() : new
		// Date(strToDate.get(dateListBox.getItemText(dateListBox.getSelectedIndex())));

		// Get date from current selected
		Date date = new Date(strToDate.get(dateListBox.getItemText(dateListBox.getSelectedIndex())));
		dateBox.setValue(date);
		if (flightEntry.isStartTimeValid()) {
			date.setTime(flightEntry.getStartTimeInMillis());
			startDateBox.setValue(date);
		} else {
			startDateBox.setValue(null);
		}
		if (flightEntry.isEndTimeValid()) {
			date.setTime(flightEntry.getEndTimeInMillis());
			endDateBox.setValue(date);
		} else {
			endDateBox.setValue(null);
		}
		pilotNameBox.setValue(flightEntry.getPilot());
		trainingCheckBox.setValue(flightEntry.isTraining());
		remarksTextBox.setValue(flightEntry.getRemarks());
		if (newEntry) {
			String pl = placeListBox.getItemCount() > 0 ? placeListBox.getValue(placeListBox.getSelectedIndex()) : "";
			flightEntry.setPlace(pl);
			pilotNameBox.setValue(currentLoginInfo.getLastName() + " " + currentLoginInfo.getFirstName());
		}
		GwtUtil.setItems(allPlacesListBox, GwtUtil.toAirfieldNames(allAirfields));
		// TODO - it may be slow if lots of airfields -> optimize!
		for (int i = 0; i < allPlacesListBox.getItemCount(); i++) {
			if (flightEntry.getPlace().equals(allPlacesListBox.getValue(i))) {
				allPlacesListBox.setSelectedIndex(i);
				break;
			}
		}
		allPlacesSuggestBox.setValue(flightEntry.getLandingPlace());
		registrationBox.setValue(flightEntry.getPlane());
	}

	private void saveForm(FlightEntry flightEntry) {
		String pilot = pilotNameBox.getValue();
		if (pilot.length() == 0) {
			pilot = "<Unknown>";
		}
		flightEntry.setPilot(pilot);

		Date date = null;
		// TODO - find a better way to check
		try {
			date = startDateBox.getValue();
		} catch (Exception e) {
			// TODO: handle exception
		}
		flightEntry.setStartTimeValid(date != null);
		if (flightEntry.isStartTimeValid()) {
			toYMD(dateBox.getValue(), date);
			flightEntry.setStartTimeInMillis(date.getTime());
		} else {
			// TODO - very crude workaround for time zone problem
			date = dateBox.getValue();
			long offset = timeZone.getStandardOffset();
			date.setTime(date.getTime() - (offset * 60000));
			flightEntry.setStartTimeInMillis(date.getTime());
		}

		date = null;
		// TODO - find a better way to check
		try {
			date = endDateBox.getValue();
		} catch (Exception e) {
			// TODO: handle exception
		}
		flightEntry.setEndTimeValid(date != null);
		if (flightEntry.isEndTimeValid()) {
			toYMD(dateBox.getValue(), date);
			flightEntry.setEndTimeInMillis(date.getTime());
		} else {
			flightEntry.setEndTimeInMillis(0);
		}

		flightEntry.setTraining(trainingCheckBox.getValue());
		flightEntry.setRemarks(remarksTextBox.getValue());
		flightEntry.setPlace(allPlacesListBox.getValue(allPlacesListBox.getSelectedIndex()));
		flightEntry.setLandingPlace(allPlacesSuggestBox.getValue());
		flightEntry.setPlane(registrationBox.getValue());
	}

	private void toYMD(Date srcDate, Date dstDate) {
		dstDate.setDate(srcDate.getDate());
		dstDate.setMonth(srcDate.getMonth());
		dstDate.setYear(srcDate.getYear());
	}

	public void gui_eventNewButtonClicked() {
		disableCUDButtons();
		enablePilotFields(true);
		FlightEntry flightEntry = new FlightEntry();
		loadForm(flightEntry);
		flightEntryDialogBox.setTitle(TXT_TITLE_CREATE_NEW_FLIGHT);
		flightEntryDialogBox.setText(TXT_CREATE_NEW_FLIGHT);
		flightEntryDialogBox.setPopupPosition(newButton.getAbsoluteLeft() + newButton.getOffsetWidth(), newButton.getAbsoluteTop()
				- flightEntryDialogBox.getOffsetHeight() - 20);
		flightEntryDialogBox.show();
		registrationBox.setFocus(true);

		newButton.setEnabled(false);
		saveButton.setEnabled(true);
		discardButton.setEnabled(true);
		btnClose.setEnabled(false);
	}

	public void gui_eventModifyButtonClicked() {
		if (currentFlightEntry.isModifiable()) {
			enablePilotFields(true);
			flightEntryDialogBox.setTitle(TXT_TITLE_MODIFY_FLIGHT);
			flightEntryDialogBox.setHTML(TXT_MODIFY_FLIGHT);
			flightEntryDialogBox.setPopupPosition(modifyButton.getAbsoluteLeft() + modifyButton.getOffsetWidth(), modifyButton.getAbsoluteTop()
					- flightEntryDialogBox.getOffsetHeight() - 20);
			flightEntryDialogBox.show();
			registrationBox.setFocus(true);

			disableCUDButtons();
			saveButton.setEnabled(false);
			discardButton.setEnabled(false);
			btnClose.setEnabled(true);
		} else {// Not the owner and not Admin
			showMidifiableDialog(currentFlightEntry, "You can not modify this elemen, the owner is");
		}
	}

	public void gui_eventSaveButtonClicked() {
		if (currentFlightEntry == null) {
			return;
		}
		if (validator == null) {
			validator = new FlightEntryValidator(this);
		}
		if (validator.isValid()) {
			disableSCButtons();
			saveForm(currentFlightEntry);
			flightEntryService.updateFlightEntry(currentFlightEntry);
			clearForm();
			enablePilotFields(false);
			newButton.setEnabled(true);
			flightEntryDialogBox.hide();
		}
	}

	public void gui_eventDiscardClicked() {
		disableSCButtons();
		clearForm();
		enablePilotFields(false);
		newButton.setEnabled(true);
		dynaTableWidget.resetSelection();
		flightEntryDialogBox.hide();
	}

	public void gui_eventCloseClicked() {
		gui_eventDiscardClicked();
	}

	private boolean hasDate(FlightEntry flightEntry) {
		return strToDate.containsKey(DATE_FORMAT.format(new Date(flightEntry.getStartTimeInMillis())));
	}

	private boolean hasPlace(FlightEntry flightEntry) {
		Set<String> items = getItemList(placeListBox);
		return items.contains(flightEntry.getPlace());
	}

	private boolean hasYear(FlightEntry flightEntry) {
		Set<String> items = getItemList(yearListBox);
		Date date = new Date(flightEntry.getStartTimeInMillis());
		return items.contains(String.valueOf(date.getYear() + 1900));
	}

	private Set<String> getItemList(ListBox listBox) {
		Set<String> items = new TreeSet<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			items.add(listBox.getValue(i));
		}
		return items;
	}

	private void setSetected(ListBox listBox, String item) {
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (item.equals(listBox.getValue(i))) {
				listBox.setSelectedIndex(i);
				break;
			}
		}
	}

	public void gui_eventDeleteButtonClicked() {
		if (currentFlightEntry == null) {
			return;
		}
		if (currentFlightEntry.isDeletable()) {
			final DialogBox deleteDialogBox = new DialogBox();
			deleteDialogBox.setModal(true);
			deleteDialogBox.setPopupPosition(deleteButton.getAbsoluteLeft() + deleteButton.getOffsetWidth(), deleteButton.getAbsoluteTop()
					- deleteDialogBox.getOffsetHeight());
			deleteDialogBox.setHTML(TXT_REALLY_DELETE_QUESWTION);
			Button yesButton = new Button(TXT_YES);
			yesButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					disableCUDButtons();
					flightEntryService.removeFlightEntry(currentFlightEntry);
					reload();
					deleteDialogBox.hide();
				}
			});
			Button noButton = new Button(TXT_NO);
			noButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					deleteDialogBox.hide();
				}
			});
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(yesButton);
			hp.add(noButton);
			deleteDialogBox.setWidget(hp);
			deleteDialogBox.show();
		} else { // Not the owner and not Admin
			showMidifiableDialog(currentFlightEntry, "You can not delete this elemen, the owner is");
		}
	}

	private void showMidifiableDialog(FlightEntry flightEntry, String msg) {
		final DialogBox notmodDialogBox = new DialogBox();
		notmodDialogBox.setModal(true);
		notmodDialogBox.setPopupPosition(deleteButton.getAbsoluteLeft() + deleteButton.getOffsetWidth(), deleteButton.getAbsoluteTop()
				- notmodDialogBox.getOffsetHeight());
		notmodDialogBox.setHTML(msg + " " + flightEntry.getCreator());
		Button okButton = new Button(TXT_OK);
		okButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				disableCUDButtons();
				notmodDialogBox.hide();
			}
		});
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(okButton);
		notmodDialogBox.setWidget(hp);
		notmodDialogBox.show();
	}

	private void setTabOrder(Widget... widgets) {
		int idx = 0;
		for (Widget widget : widgets) {
			Widget wdg = widget;
			if (wdg instanceof SuggestBox2) {
				wdg = ((SuggestBox2) wdg).getFocusWidget();
			} else {
				if (wdg instanceof DateBox2) {
					wdg = ((DateBox2) wdg).getFocusWidget();
				}
			}
			if (wdg instanceof FocusWidget) {
				((FocusWidget) wdg).setTabIndex(idx++);
			}
		}
	}

	public void service_eventUpdateSuccessful(FlightEntry flightEntry) {
		status.setText("FlightEntry was successfully added");
		if (placeListBox.getItemCount() == 0) {
			placeListBox.addItem(flightEntry.getPlace());
		}
		if (!hasYear(flightEntry)) {
			loadYears();
		} else {
			Date date = new Date(flightEntry.getStartTimeInMillis());
			setSetected(yearListBox, String.valueOf(date.getYear() + 1900));
			if (!hasPlace(flightEntry)) {
				date = new Date(flightEntry.getStartTimeInMillis());
				loadPlaces(date.getYear() + 1900);
			} else {
				setSetected(placeListBox, flightEntry.getPlace());
				if (!hasDate(flightEntry)) {
					loadDates();
				} else {
					String dateStr = DATE_FORMAT.format(new Date(flightEntry.getStartTimeInMillis()));
					setSetected(dateListBox, dateStr);
					reload(flightEntry);
				}
			}
		}
	}

	public void service_eventRemoveFlightEntrySuccessful(FlightEntry flightEntry) {
		status.setText("FlightEntry was removed");
		reload();

	}

	public void service_eventUpdateFlightEntryFailed(Throwable caught) {
		status.setText("Update flightEntry failed");
	}

	public void service_eventAddFlightEntryFailed(Throwable caught) {
		status.setText("Unable to update flightEntry");
	}

	public void service_eventRemoveFlightEntryFailed(Throwable caught) {
		status.setText("Remove flightEntry failed");
	}

	public void service_eventListFlightEntrysFailed(Throwable caught) {
		status.setText("Unable to get flightEntry list");

	}

	public void gui_eventPlaceListBoxChanged() {
		clearForm();
		loadDates();
	}

	public void service_eventListPlacesSuccessful(Set<String> places) {
		String oldPlace = "";
		if (lastflightEntry != null) {
			oldPlace = lastflightEntry.getPlace();
		} else {
			int oldIdx = placeListBox.getSelectedIndex();
			if (oldIdx >= 0) {
				oldPlace = placeListBox.getItemText(placeListBox.getSelectedIndex());
			}
		}
		int newOldPlaceIdx = -1;
		int homePlaceIdx = -1;
		placeListBox.clear();
		for (String place : places) {
			placeListBox.addItem(place);
			if (newOldPlaceIdx == -1 && place.equals(oldPlace)) {
				newOldPlaceIdx = placeListBox.getItemCount() - 1;
			}
			if (currentLoginInfo != null && homePlaceIdx == -1 && currentLoginInfo.getHomeAirfield() != null
					&& place.equals(currentLoginInfo.getHomeAirfield())) {
				homePlaceIdx = placeListBox.getItemCount() - 1;
			}
		}
		if (newOldPlaceIdx != -1) {
			placeListBox.setSelectedIndex(newOldPlaceIdx);
		} else if (homePlaceIdx != -1) {
			placeListBox.setSelectedIndex(homePlaceIdx);
		} else {
			placeListBox.setSelectedIndex(0);
		}
		loadDates();
	}

	private void loadDates() {
		String place = placeListBox.getItemCount() > 0 ? placeListBox.getItemText(placeListBox.getSelectedIndex()) : "";
		int year = Integer.parseInt(yearListBox.getItemText(yearListBox.getSelectedIndex()));
		flightEntryService.listDates(place, year);
	}

	public void service_eventListPlacesFailed(Throwable caught) {
		// TODO Auto-generated method stub
	}

	/*
	 * Year handling
	 */

	public void gui_eventYearListBoxChanged() {
		clearForm();
		int year = Integer.parseInt(yearListBox.getItemText(yearListBox.getSelectedIndex()));
		flightEntryService.listPlaces(year);
	}

	public void service_eventListYearsFailed(Throwable caught) {
		// TODO Auto-generated method stub
	}

	public void service_eventListYearsSuccessful(Set<Integer> years) {
		yearListBox.clear();
		for (Integer year : years) {
			yearListBox.addItem(Integer.toString(year));
		}
		yearListBox.setSelectedIndex(yearListBox.getItemCount() - 1);
		int year = Integer.parseInt(yearListBox.getItemText(yearListBox.getSelectedIndex()));
		flightEntryService.listPlaces(year);
	}

	/*
	 * Day handling
	 */
	private void adjustPrevNextDayButtons() {
		nextDayPushButton.setEnabled(dateListBox.getSelectedIndex() < dateListBox.getItemCount() - 1);
		prevDayPushButton.setEnabled(dateListBox.getSelectedIndex() > 0);
		clearForm();
	}

	public void gui_eventDateListBoxChanged() {
		adjustPrevNextDayButtons();

		// adjust link to excel file
		Date date = new Date(strToDate.get(dateListBox.getItemText(dateListBox.getSelectedIndex())));
		String link = GWT.getModuleBaseURL() + "excelfile" + "/" + yearListBox.getValue(yearListBox.getSelectedIndex()) + "/" + date.getMonth() + "/"
				+ date.getDate() + "/" + placeListBox.getValue(placeListBox.getSelectedIndex());
		excelLinkHTML.setHTML("<a href=\"" + link + "\">Excel</a>");

		// reload table values
		reload();
	}

	public void gui_eventNextDayPushButtonClicked() {
		if (dateListBox.getSelectedIndex() < dateListBox.getItemCount() - 1) {
			dateListBox.setItemSelected(dateListBox.getSelectedIndex() + 1, true);
		}
		gui_eventDateListBoxChanged();
	}

	public void gui_eventPrevDayPushButtonClicked() {
		if (dateListBox.getSelectedIndex() > 0) {
			dateListBox.setItemSelected(dateListBox.getSelectedIndex() - 1, true);
		}
		gui_eventDateListBoxChanged();
	}

	public void service_eventListDatesFailed(Throwable caught) {
		// TODO Auto-generated method stub
	}

	public void service_eventListDatesSuccessful(Set<Long> dates) {
		dateListBox.clear();
		strToDate.clear();
		for (Long dateInMillies : dates) {
			String dateStr = DATE_FORMAT.format(new Date(dateInMillies), timeZone);
			dateListBox.addItem(dateStr);
			strToDate.put(dateStr, dateInMillies);
		}
		int lasSelected = dateListBox.getSelectedIndex();
		if (lastflightEntry != null) {
			Date feDate = new Date(lastflightEntry.getStartTimeInMillis());
			for (int i = 0; i < dateListBox.getItemCount(); i++) {
				Date date = new Date(strToDate.get(dateListBox.getValue(i)));
				if (date.getYear() == feDate.getYear() && date.getMonth() == feDate.getMonth() && date.getDate() == feDate.getDate()) {
					dateListBox.setSelectedIndex(i);
					break;
				}
			}
		} else {
			dateListBox.setSelectedIndex(dateListBox.getItemCount() - 1);
		}
		gui_eventDateListBoxChanged();

	}

	public void service_eventListAllPlacesFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	public void service_eventAllListPlacesSuccessful(Set<Airfield> airfields) {
		allAirfields = airfields;
		GwtUtil.setItems(allPlacesListBox, GwtUtil.toAirfieldNames(airfields));
		for (String place : GwtUtil.toAirfieldNames(airfields)) {
			allPlacesSuggest.add(place);
		}
	}

	public void gui_eventModifyPilotForm() {
		if (currentFlightEntry.getId() != null) {
			// compare only modified entry
			// TODO - should we use tmpFlightEntry for save?
			FlightEntry tmpFlightEntry = currentFlightEntry.copy();
			saveForm(tmpFlightEntry);
			enableSCButtons(tmpFlightEntry.compareTo(currentFlightEntry) != 0);
		}
	}

	public void service_eventLoginSuccessful(LoginInfo loginInfo) {
		if (loginInfo.isLoggedIn()) {
			currentLoginInfo = loginInfo;
			signInLink.setText("Logout");
			signInLink.setHref(loginInfo.getLogoutUrl());
			loggedInAs.setText("(logged in as " + loginInfo.getEmail() + ")");
			operationNewModDel.setVisible(true);

			if (loginInfo.isAdmin()) {
				if (adminGUI == null) {
					adminGUI = new AdminGUI(this);
					RootPanel.get().add(adminGUI);
				}
				stackPanel.add(adminGUI, "Admin", false);
			}
		} else {
			currentLoginInfo = null;
			signInLink.setText("Login");
			signInLink.setHref(loginInfo.getLoginUrl());
			loggedInAs.setText("");
			operationNewModDel.setVisible(false);

			if (adminGUI != null) {
				adminGUI.removeFromParent();
			}
		}
	}

	public void service_eventLoginFailed(Throwable caught) {
		status.setText("LOGIN ERROR " + caught.getMessage());
	}

	public ListBox getYearListBox() {
		return yearListBox;
	}

	public Set<Airfield> getAllAirfields() {
		return allAirfields;
	}

}
