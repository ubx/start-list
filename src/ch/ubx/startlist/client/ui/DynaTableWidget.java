package ch.ubx.startlist.client.ui;

/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import ch.ubx.startlist.client.DynaTableDataProvider;
import ch.ubx.startlist.client.RowSelectionHandler;
import ch.ubx.startlist.client.RowDoubleclickHandler;
import ch.ubx.startlist.client.DynaTableDataProvider.RowDataAcceptor;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A composite Widget that implements the main interface for the dynamic table, including the data table, status indicators, and paging buttons.
 */
public class DynaTableWidget extends Composite {

	private static final String NO_CONNECTION_MESSAGE = "<p>The DynaTable example uses a <a href=\"http://code.google.com/"
			+ "webtoolkit/documentation/com.google.gwt.doc.DeveloperGuide." + "RemoteProcedureCalls.html\" target=\"_blank\">Remote Procedure Call</a> "
			+ "(RPC) to request data from the server.  In order for the RPC to " + "successfully return data, the server component must be available.</p>"
			+ "<p>If you are running this demo from compiled code, the server " + "component may not be available to respond to the RPC requests from "
			+ "DynaTable.  Try running DynaTable in development mode to see the demo " + "in action.</p> "
			+ "<p>Click on the Remote Procedure Call link above for more information " + "on GWT's RPC infrastructure.";

	private final RowDataAcceptor acceptor = new RowDataAcceptorImpl();

	private final Table table = new Table();

	private final NavBar navbar = new NavBar();

	private ErrorDialog errorDialog = null;

	private final DockPanel outer = new DockPanel();

	private final DynaTableDataProvider provider;

	private int startRow = 0;

	public DynaTableWidget(DynaTableDataProvider provider, String[] columns, String[] columnStyles, int rowCount) {

		if (columns.length == 0) {
			throw new IllegalArgumentException("expecting a positive number of columns");
		}

		if (columnStyles != null && columns.length != columnStyles.length) {
			throw new IllegalArgumentException("expecting as many styles as columns");
		}

		this.provider = provider;
		initWidget(outer);
		table.setStyleName("table");
		outer.add(navbar, DockPanel.NORTH);
		outer.add(table, DockPanel.CENTER);
		initTable(columns, columnStyles, rowCount);
		setStyleName("DynaTable-DynaTableWidget");
	}

	/**
	 * A dialog box for displaying an error.
	 */
	private static class ErrorDialog extends DialogBox implements ClickHandler {
		private HTML body = new HTML("");

		public ErrorDialog() {
			setStylePrimaryName("DynaTable-ErrorDialog");
			Button closeButton = new Button("Close", this);
			VerticalPanel panel = new VerticalPanel();
			panel.setSpacing(4);
			panel.add(body);
			panel.add(closeButton);
			panel.setCellHorizontalAlignment(closeButton, VerticalPanel.ALIGN_RIGHT);
			setWidget(panel);
		}

		public void onClick(ClickEvent event) {
			hide();
		}

		public void setBody(String html) {
			body.setHTML(html);
		}
	}

	private class NavBar extends Composite implements ClickHandler {

		public final DockPanel bar = new DockPanel();
		public final Button gotoFirst = new Button("&lt;&lt;", this);
		public final Button gotoNext = new Button("&gt;", this);
		public final Button gotoPrev = new Button("&lt;", this);
		public final HTML status = new HTML();

		public NavBar() {
			initWidget(bar);
			bar.setStyleName("navbar");
			status.setStyleName("status");

			HorizontalPanel buttons = new HorizontalPanel();
			buttons.add(gotoFirst);
			buttons.add(gotoPrev);
			buttons.add(gotoNext);
			bar.add(buttons, DockPanel.EAST);
			bar.setCellHorizontalAlignment(buttons, DockPanel.ALIGN_RIGHT);
			bar.add(status, DockPanel.CENTER);
			bar.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
			bar.setCellHorizontalAlignment(status, HasAlignment.ALIGN_RIGHT);
			bar.setCellVerticalAlignment(status, HasAlignment.ALIGN_MIDDLE);
			bar.setCellWidth(status, "100%");

			// Initialize prev & first button to disabled.
			//
			gotoPrev.setEnabled(false);
			gotoFirst.setEnabled(false);
		}

		public void onClick(ClickEvent event) {
			Object source = event.getSource();
			if (source == gotoNext) {
				startRow += getDataRowCount();
				refresh();
			} else if (source == gotoPrev) {
				startRow -= getDataRowCount();
				if (startRow < 0) {
					startRow = 0;
				}
				refresh();
			} else if (source == gotoFirst) {
				startRow = 0;
				refresh();
			}
		}
	}

	private class RowDataAcceptorImpl implements RowDataAcceptor {
		public void accept(int startRow, String[][] data) {

			int destRowCount = getDataRowCount();
			int destColCount = table.getCellCount(0);
			assert (data.length <= destRowCount) : "Too many rows";

			int srcRowIndex = 0;
			int srcRowCount = data.length;
			int destRowIndex = 1; // skip navbar row
			for (; srcRowIndex < srcRowCount; ++srcRowIndex, ++destRowIndex) {
				String[] srcRowData = data[srcRowIndex];
				assert (srcRowData.length == destColCount) : " Column count mismatch";
				for (int srcColIndex = 0; srcColIndex < destColCount; ++srcColIndex) {
					String cellHTML = srcRowData[srcColIndex];
					table.setText(destRowIndex, srcColIndex, cellHTML);
				}
			}

			// Clear remaining table rows.
			//
			boolean isLastPage = false;
			for (; destRowIndex < destRowCount + 1; ++destRowIndex) {
				isLastPage = true;
				for (int destColIndex = 0; destColIndex < destColCount; ++destColIndex) {
					table.clearCell(destRowIndex, destColIndex);
				}
			}

			// Synchronize the nav buttons.
			navbar.gotoNext.setEnabled(!isLastPage);
			navbar.gotoFirst.setEnabled(startRow > 0);
			navbar.gotoPrev.setEnabled(startRow > 0);

			// Update the status message.
			//
			setStatusText((startRow + 1) + " - " + (startRow + srcRowCount));
		}

		public void failed(Throwable caught) {
			setStatusText("Error");
			if (errorDialog == null) {
				errorDialog = new ErrorDialog();
			}
			if (caught instanceof InvocationException) {
				errorDialog.setText("An RPC server could not be reached");
				errorDialog.setBody(NO_CONNECTION_MESSAGE);
			} else {
				errorDialog.setText("Unexcepted Error processing remote call");
				errorDialog.setBody(caught.getMessage());
			}
			errorDialog.center();
		}
	}

	public void setStatusText(String text) {
		navbar.status.setText(text);
		table.setIgnoreMouseEvents(false);
	}

	public void clearStatusText() {
		navbar.status.setHTML("&nbsp;");
		table.setIgnoreMouseEvents(false);
	}

	public void reload() {
		startRow = 0;
		provider.reset();
		refresh();
	}

	public void refresh() {
		// Disable buttons temporarily to stop the user from running off the
		// end.
		navbar.gotoFirst.setEnabled(false);
		navbar.gotoPrev.setEnabled(false);
		navbar.gotoNext.setEnabled(false);
		table.resetSelection();

		for (int row = 1; row < table.getRowCount(); row++) {
			for (int column = 0; column < table.getColumnCount(); column++) {
				table.clearCell(row, column);
			}
		}

		setStatusText("Please wait...");
		provider.updateRowData(startRow, table.getRowCount() - 1, acceptor);
		table.setIgnoreMouseEvents(true);
	}

	public void setRowCount(int rows) {
		table.resizeRows(rows);
	}

	public void addRowSelectionHandler(final RowSelectionHandler handler) {
		table.addRowSelectionHandler(new Table.RowSelectionHandler() {
			@Override
			public void rowSelected(int row, boolean selected) {
				handler.rowSelected(row - 1, selected);
			}
		});
	}
	
	public void addRowDoubleclickHandler(final RowDoubleclickHandler handler) {
		table.addRowDoubleclickHandler(new Table.RowDoubleclickHandler() {
			@Override
			public void rowDoubleclicked(int row) {
				handler.rowDoubleclicked(row - 1);
			}
		});
	}

	private int getDataRowCount() {
		return table.getRowCount() - 1;
	}

	private void initTable(String[] columns, String[] columnStyles, int rowCount) {
		// Set up the header row. It's one greater than the number of visible
		// rows.
		//
		table.resize(rowCount + 1, columns.length);
		for (int i = 0, n = columns.length; i < n; i++) {
			table.setText(0, i, columns[i]);
			if (columnStyles != null) {
				table.getCellFormatter().setStyleName(0, i, columnStyles[i] + " header");
			}
		}
	}

	public void resetSelection() {
		table.resetSelection();
	}

}
