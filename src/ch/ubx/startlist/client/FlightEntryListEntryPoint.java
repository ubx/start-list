package ch.ubx.startlist.client;

import java.util.Date;

import ch.ubx.startlist.client.ui.DateBox2;
import ch.ubx.startlist.client.ui.SuggestBox2;
import ch.ubx.startlist.client.ui.FlightEntryListGUI;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define onModuleLoad().
 */
public class FlightEntryListEntryPoint implements EntryPoint {
	private FlightEntryListGUI gui;
	private FlightEntryServiceDelegate flightEntryServiceDelegate;
	private LoginServiceDelegate loginServiceDelegate;
	private AirfieldServiceDelegate airfieldServiceDelegate;
	private PilotServiceDelegate pilotServiceDelegate;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		doFlightEntry();
	}

	private void doFlightEntry() {
		gui = new FlightEntryListGUI();

		flightEntryServiceDelegate = new FlightEntryServiceDelegate();
		gui.flightEntryService = flightEntryServiceDelegate;
		flightEntryServiceDelegate.gui = gui;

		loginServiceDelegate = new LoginServiceDelegate();
		gui.loginServiceDelegate = loginServiceDelegate;
		loginServiceDelegate.gui = gui;

		airfieldServiceDelegate = new AirfieldServiceDelegate(gui);
		gui.airfieldServiceDelegate = airfieldServiceDelegate;
		
		pilotServiceDelegate = new PilotServiceDelegate(gui);
		gui.pilotServiceDelegate = pilotServiceDelegate;

		gui.init();
		wireGUIEvents();
	}

	private void wireGUIEvents() {

		gui.newButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				gui.gui_eventNewButtonClicked();
			}
		});

		gui.modifyButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				gui.gui_eventModifyButtonClicked();
			}
		});

		gui.saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				gui.gui_eventSaveButtonClicked();
			}
		});

		gui.deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				gui.gui_eventDeleteButtonClicked();
			}
		});

		gui.discardButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				gui.gui_eventDiscardClicked();
			}
		});

		gui.btnClose.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				gui.gui_eventCloseClicked();
			}
		});

		gui.yearListBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				gui.gui_eventYearListBoxChanged();
			}
		});

		gui.placeListBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				gui.gui_eventPlaceListBoxChanged();

			}
		});

		gui.dateListBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				gui.gui_eventDateListBoxChanged();
			}
		});

		gui.nextDayPushButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				gui.gui_eventNextDayPushButtonClicked();
			}
		});

		gui.prevDayPushButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				gui.gui_eventPrevDayPushButtonClicked();
			}
		});

		// Add focus handlers on widgets on flightEntryFlexTable
		for (int row = 0; row < gui.flightEntryFlexTable.getRowCount(); row++)
			for (int col = 0; col < gui.flightEntryFlexTable.getCellCount(row); col++) {
				Widget widget = gui.flightEntryFlexTable.getWidget(row, col);
				if (widget instanceof SuggestBox2) {
					widget = ((SuggestBox2) widget).getFocusWidget();
				}
				if (widget instanceof DateBox2) {
					((DateBox2) widget).addValueChangeHandler(new ValueChangeHandler<Date>() {
						@Override
						public void onValueChange(ValueChangeEvent<Date> event) {
							gui.gui_eventModifyPilotForm();
						}
					});
					widget = ((DateBox2) widget).getFocusWidget();
				}

				if (widget instanceof CheckBox) {
					((CheckBox) widget).addValueChangeHandler(new ValueChangeHandler<Boolean>() {
						@Override
						public void onValueChange(ValueChangeEvent<Boolean> event) {
							gui.gui_eventModifyPilotForm();
						}
					});
				} else if (widget instanceof ListBox) {
					((ListBox) widget).addChangeHandler(new ChangeHandler() {
						@Override
						public void onChange(ChangeEvent event) {
							gui.gui_eventModifyPilotForm();
						}
					});
				} else {
					if (widget instanceof FocusWidget) {
						((FocusWidget) widget).addKeyUpHandler(new KeyUpHandler() {
							@Override
							public void onKeyUp(KeyUpEvent event) {
								gui.gui_eventModifyPilotForm();

							}
						});
					}
				}
			}

	}
}
