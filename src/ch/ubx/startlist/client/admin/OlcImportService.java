package ch.ubx.startlist.client.admin;

import java.util.List;

import ch.ubx.startlist.shared.FeFlightEntry;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("olcimport")
public interface OlcImportService extends RemoteService {
	
	public List<FeFlightEntry> importFromPlace(String place, int year);


}
