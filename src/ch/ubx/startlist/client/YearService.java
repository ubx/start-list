package ch.ubx.startlist.client;

import ch.ubx.startlist.shared.FeYear;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.googlecode.objectify.Key;

@RemoteServiceRelativePath("years")
public interface YearService extends RemoteService {
    
    public Key<FeYear> listYears();


}
