package ch.ubx.startlist.client;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.datepicker.client.DateBox;

// XX

public class DateBox2 extends DateBox {
	public DateBox2() {
		super();
	}

	public FocusWidget getFocusWidget() {
		return (FocusWidget) getWidget();
	}

}
