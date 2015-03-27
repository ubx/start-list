package ch.ubx.startlist.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Grid;

public class Table extends Grid {

	private static final String TXT_BACKGROUND_COLOR = "backgroundColor";
	// TODO - replace with css styles?
	private static final String COL_OVER = "yellow";
	private static final String COL_SELECTED = "greenyellow";
	private static final String COL_NONE = "#FFFFFF";
	private static final String COL_EVEN = "#EEF2F7";
	private static final String[] row_bg_col = { COL_NONE, COL_EVEN };

	private Element body = null;

	private Element selElem = null;
	private Element selElem2 = null;
	private RowSelectionHandler rowSelectionHandler = null;
	private RowDoubleclickHandler rowDoubleclickHandler = null;

	private boolean ignoreMouseEvents = false;

	/** Creates a new instance of Table */
	public Table() {
		super();

		// indicate which events you intend to notice
		sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONDBLCLICK);
	}

	@Override
	public void onBrowserEvent(Event event) {
		if (ignoreMouseEvents) {
			return;
		}
		Element td = getEventTargetCell(event);
		if (td == null)
			return;
		Element tr = DOM.getParent(td);

		body = DOM.getParent(tr);
		int row = DOM.getChildIndex(body, tr);
		if (row == 0) {
			return;
		}
		String it = DOM.getInnerText(tr);
		// if (it.startsWith("  ")) { TODO - DOES NOT WORK: WHY??????
		if (it.length() == 9) { // TODO - VERY TEMORARY WORKAROUND
			return;
		}

		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEDOWN: {
			if (selElem == null) {
				DOM.setStyleAttribute(tr, TXT_BACKGROUND_COLOR, COL_SELECTED);
				selElem = tr;
				handleRowSelection(row, true);
			} else {
				if (selElem == tr) {
					DOM.setStyleAttribute(tr, TXT_BACKGROUND_COLOR, row_bg_col[(row) % 2]);
					selElem = null;
					handleRowSelection(row, false);
				} else {
					DOM.setStyleAttribute(selElem, TXT_BACKGROUND_COLOR, row_bg_col[(DOM.getChildIndex(body, selElem)) % 2]);
					DOM.setStyleAttribute(tr, TXT_BACKGROUND_COLOR, COL_SELECTED);
					selElem = tr;
					handleRowSelection(row, true);
				}
			}
			break;
		}
		case Event.ONMOUSEUP: {
			// DOM.setStyleAttribute(tr, "backgroundColor", COL_NONE);
			break;
		}
		case Event.ONMOUSEOVER: {
			if (selElem != tr) {
				DOM.setStyleAttribute(tr, TXT_BACKGROUND_COLOR, COL_OVER);
			}
			selElem2 = tr;
			break;
		}
		case Event.ONMOUSEOUT: {
			if (selElem != tr) {
				DOM.setStyleAttribute(tr, TXT_BACKGROUND_COLOR, row_bg_col[(row) % 2]);
			}
			break;
		}
		case Event.ONDBLCLICK: {
			handleRowDoubleclick(row);
			break;
		}

		}
		super.onBrowserEvent(event);

	}

	private void handleRowSelection(int row, boolean selected) {
		if (rowSelectionHandler != null) {
			rowSelectionHandler.rowSelected(row, selected);
		}
	}
	
	private void handleRowDoubleclick(int row) {
		if (rowDoubleclickHandler != null) {
			rowDoubleclickHandler.rowDoubleclicked(row);
		}
	}

	public void resetSelection() {
		if (selElem != null) {
			DOM.setStyleAttribute(selElem, TXT_BACKGROUND_COLOR, row_bg_col[(DOM.getChildIndex(body, selElem)) % 2]);
			handleRowSelection(-1, false);
			selElem = null;
		}
		if (selElem2 != null) {
			DOM.setStyleAttribute(selElem2, TXT_BACKGROUND_COLOR, row_bg_col[(DOM.getChildIndex(body, selElem2)) % 2]);
			selElem2 = null;
		}

	}

	public interface RowSelectionHandler {
		public void rowSelected(int row, boolean selected);

	}
	
	public interface RowDoubleclickHandler {
		public void rowDoubleclicked(int row);

	}
	
	public void addRowSelectionHandler(RowSelectionHandler handler) {
		this.rowSelectionHandler = handler;
	}
	
	public void addRowDoubleclickHandler(RowDoubleclickHandler handler) {
		this.rowDoubleclickHandler = handler;
	}

	public void setIgnoreMouseEvents(boolean ignoreMouseEvents) {
		this.ignoreMouseEvents = ignoreMouseEvents;
	}

	public boolean isIgnoreMouseEvents() {
		return ignoreMouseEvents;
	}

	@Override
	public void resizeRows(int rows) {
		super.resizeRows(rows);
		for (int row = 0; row < getRowCount(); row++) {
			if ((row % 2) == 0) {
				getRowFormatter().setStyleName(row, "tableCell-even");
			} else {
				getRowFormatter().setStyleName(row, "tableCell-odd");
			}

		}
	}
}
