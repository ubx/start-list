package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.SendExcel;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class SendExcelDAOobjectify implements SendExcelDAO {

	static {
		ObjectifyService.register(SendExcel.class);
	}

	@Override
	public void removeSendExcel(SendExcel sendExcel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createOrUpdateSendExcel(SendExcel sendExcel) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(sendExcel);
	}

	@Override
	public List<SendExcel> listAllSendExcel() {
		Objectify ofy = ObjectifyService.begin();
		Query<SendExcel> query = ofy.query(SendExcel.class);
		return query.list();
	}

}
