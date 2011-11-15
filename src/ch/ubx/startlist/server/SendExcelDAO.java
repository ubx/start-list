package ch.ubx.startlist.server;

import java.util.List;
import java.util.Map;

import ch.ubx.startlist.shared.SendExcel;

@Deprecated
public interface SendExcelDAO {

	public void removeSendExcel(SendExcel sendExcel);

	public void createOrUpdateSendExcel(SendExcel sendExcel);

	public List<SendExcel> listAllSendExcel();
	
	public Map<String, SendExcel> listSendExcel(List<String> names);

}