/**
 * 
 */
package ch.ubx.startlist.client.ui;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

public class SuggestBox2 extends SuggestBox {

	public SuggestBox2(SuggestOracle oracle) {
		super(oracle);
	}

	public void setEnabled(boolean enabled) {
		getFocusWidget().setEnabled(enabled);
	}

	public FocusWidget getFocusWidget() {
		return (FocusWidget) getWidget();
	}
}