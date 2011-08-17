package ch.ubx.startlist.client;

import ch.ubx.startlist.shared.FeYear;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.objectify.Key;

public interface YearServiceAsync {

    void listYears(AsyncCallback<Key<FeYear>> callback);

}
