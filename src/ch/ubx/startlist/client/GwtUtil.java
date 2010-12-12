package ch.ubx.startlist.client;

import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.user.client.ui.ListBox;

public class GwtUtil {
	public static Set<String> toAirfieldNames(Set<Airfield> airfields) {
		Set<String> sortedAf = new TreeSet<String>();
		for (Airfield af : airfields) {
			sortedAf.add(af.getName());
		}
		return sortedAf;
	}

	public static void setItems(ListBox lb, Set<String> items) {
		lb.clear();
		for (String item : items) {
			lb.addItem(item);
		}
	}

}
