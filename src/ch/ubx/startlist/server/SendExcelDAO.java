package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.client.SendExcel;

public interface SendExcelDAO {

	public void removeSendExcel(SendExcel sendExcel);

	public void createOrUpdateSendExcel(SendExcel sendExcel);

	public List<SendExcel> listAllSendExcel();

}
