package ch.ubx.startlist.client.admin.ui;

import java.util.List;
import java.util.Set;

import ch.ubx.startlist.client.GwtUtil;
import ch.ubx.startlist.client.admin.AdminGUIevents;
import ch.ubx.startlist.client.admin.OlcImportServiceDelegate;
import ch.ubx.startlist.client.ui.FlightEntryListGUI;
import ch.ubx.startlist.shared.Airfield;
import ch.ubx.startlist.shared.FlightEntry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class AdminGUI extends FormPanel {

	final protected FileUpload olc2006FileUpload;

	public Button analyzeFileButton;
	protected ListBox countriesListBox;
	protected CheckBox chekBoxEraseAirfields;
	public ListBox excelYearListBox;
	protected ListBox olcImportYearListBox;
	public Button olcImportFlightEntryButton;
	public Button storeFileButton;

	private static final String UPLOAD_ACTION_URL = GWT.getModuleBaseURL() + "olc2006airfields";

	private HTML excelLinkHTML;

	private FlightEntryListGUI flightEntryListGUI;
	private Label olc2006FileUploadStatus;
	private Label olcImportFligtEntryStatus;
	private ListBox olcImportPlaceListBox;
	private int olcImportTotal;
	private boolean analyze;

	private OlcImportServiceDelegate olcImportServiceDelegate = new OlcImportServiceDelegate(this);

	public AdminGUI(FlightEntryListGUI flightEntryListGUI) {
		this.flightEntryListGUI = flightEntryListGUI;

		// ----------------------------------------------------------
		// Olc2006 stuff
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);

		FlexTable mainPanel = new FlexTable();
		// mainPanel.setWidth("800px");
		add(mainPanel);

		mainPanel.setWidget(0, 0, new Label("Olc2006 Airfields upload"));

		olc2006FileUpload = new FileUpload();
		olc2006FileUpload.setTitle("Olc file contain airfields in Olc2006 xml format");
		olc2006FileUpload.setName("uploadFile");
		mainPanel.setWidget(1, 0, olc2006FileUpload);

		analyzeFileButton = new Button("Analyze");
		mainPanel.setWidget(1, 1, analyzeFileButton);

		mainPanel.setWidget(1, 2, new Label("Countries"));
		countriesListBox = new ListBox();
		mainPanel.setWidget(1, 3, countriesListBox);

		chekBoxEraseAirfields = new CheckBox();
		chekBoxEraseAirfields.setText("Erase all airfields before upload");
		mainPanel.setWidget(2, 0, chekBoxEraseAirfields);

		storeFileButton = new Button("Store");
		mainPanel.setWidget(2, 1, storeFileButton);

		mainPanel.setWidget(2, 2, new Label("Status: "));
		olc2006FileUploadStatus = new Label();
		mainPanel.setWidget(2, 3, olc2006FileUploadStatus);

		// ----------------------------------------------------------
		// Excel stuff
		mainPanel.getFlexCellFormatter().setColSpan(3, 0, 4);
		mainPanel.setHTML(3, 0, "<hr />");
		mainPanel.setWidget(4, 0, new Label("Create Excel"));
		excelYearListBox = new ListBox();
		excelYearListBox.setVisibleItemCount(1);
		mainPanel.setWidget(5, 0, excelYearListBox);
		excelLinkHTML = new HTML();
		mainPanel.setWidget(5, 1, excelLinkHTML);
		
		// ----------------------------------------------------------
		// OLC import stuff
		mainPanel.getFlexCellFormatter().setColSpan(6, 0, 4);
		mainPanel.setHTML(6, 0, "<hr />");
		mainPanel.setWidget(7, 0, new Label("Import from OLC"));
		olcImportYearListBox = new ListBox();
		olcImportYearListBox.setVisibleItemCount(1);
		mainPanel.setWidget(8, 0, olcImportYearListBox);
		mainPanel.setWidget(8, 1, new Label("Airfield"));

		olcImportPlaceListBox = new ListBox();
		mainPanel.setWidget(8, 2, olcImportPlaceListBox);
		olcImportPlaceListBox.setVisibleItemCount(1);

		olcImportFlightEntryButton = new Button("Import");
		mainPanel.setWidget(9, 0, olcImportFlightEntryButton);
		mainPanel.setWidget(9, 1, new Label("Status: "));
		olcImportFligtEntryStatus = new Label();
		mainPanel.setWidget(9, 2, olcImportFligtEntryStatus);
		
		// ----------------------------------------------------------
		// Send excel mail job
		
		// ----------------------------------------------------------
		// Import OLC job

		new AdminGUIevents(this);
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			olcImportFligtEntryStatus.setText("");

			ListBox ylb = flightEntryListGUI.getYearListBox();
			excelYearListBox.clear();
			olcImportYearListBox.clear();
			for (int i = 0; i < ylb.getItemCount(); i++) {
				excelYearListBox.addItem(ylb.getValue(i));
				olcImportYearListBox.addItem(ylb.getValue(i));
			}
			excelYearListBox.setSelectedIndex(excelYearListBox.getItemCount() - 1);
			olcImportYearListBox.setSelectedIndex(olcImportYearListBox.getItemCount() - 1);
			gui_eventExcelYearListBoxChanged();

			Set<Airfield> allAirfields = flightEntryListGUI.getAllAirfields();
			GwtUtil.setItems(olcImportPlaceListBox, GwtUtil.toAirfieldNames(allAirfields));
		} else {
			flightEntryListGUI.loadYears();
		}
		super.setVisible(visible);
	}

	public void gui_eventExcelYearListBoxChanged() {
		String link = GWT.getModuleBaseURL() + "excelfile" + "/" + excelYearListBox.getValue(excelYearListBox.getSelectedIndex());
		excelLinkHTML.setHTML("<a href=\"" + link + "\">Excel</a>");
	}

	public void olcImportAirfieldPushButtonClicked() {
		olcImportFligtEntryStatus.setText("importing...");
		olcImportFlightEntryButton.setEnabled(false);
		olcImportPlaceListBox.setEnabled(false);
		olcImportYearListBox.setEnabled(false);
		olcImportTotal = 0;
		olcImportAirfield();
	}

	public static String getUploadActionUrl() {
		return UPLOAD_ACTION_URL;
	}

	public void gui_eventAnalyzeButtonClicked() {
		analyze = true;
		olc2006FileUploadStatus.setText("");
		countriesListBox.clear();
		setAction(AdminGUI.getUploadActionUrl() + "?analyze=true");
		submit();
	}

	public void gui_eventStoreFileButtonClicked() {
		analyze = false;
		olc2006FileUploadStatus.setText("");
		String country = countriesListBox.getValue(countriesListBox.getSelectedIndex());
		boolean del = chekBoxEraseAirfields.getValue();
		setAction(AdminGUI.getUploadActionUrl() + "?analyze=false&country=" + country + "&delete=" + (del ? "true" : "false"));
		submit();
	}

	public void eventSubmitComplete(SubmitCompleteEvent event) {
		String res = event.getResults();
		if (res.startsWith("<pre>")) {
			if (analyze) {
				String[] countries = res.substring(5, res.length() - 6).split(";");
				for (String country : countries) {
					countriesListBox.addItem(country);
				}
			} else {
				olc2006FileUploadStatus.setText(res.substring(5, res.length() - 11));
			}
		} else {
			Window.alert(res);
		}

	}

	public void eventSubmit(SubmitEvent event) {
		if (olc2006FileUpload.getFilename().length() == 0) {
			Window.alert("No file selected");
			event.cancel();
		}

	}

	public void service_eventImportFromPlaceSuccessful(List<FlightEntry> result) {
		if (result.size() == 0) {
			olcImportFligtEntryStatus.setText("Total " + String.valueOf(olcImportTotal) + "  added/modified");
			olcImportFlightEntryButton.setEnabled(true);
			olcImportPlaceListBox.setEnabled(true);
			olcImportYearListBox.setEnabled(true);
		} else {
			olcImportTotal = olcImportTotal + result.size();
			olcImportFligtEntryStatus.setText("Imported " + String.valueOf(olcImportTotal) + ",  get next...");
			olcImportAirfield();
		}
	}

	private void olcImportAirfield() {
		String place = olcImportPlaceListBox.getValue(olcImportPlaceListBox.getSelectedIndex());
		int year = Integer.parseInt(olcImportYearListBox.getValue(olcImportYearListBox.getSelectedIndex()));
		olcImportServiceDelegate.importFromPlace(place, year);
	}

}
