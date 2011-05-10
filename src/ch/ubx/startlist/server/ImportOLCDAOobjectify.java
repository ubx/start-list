package ch.ubx.startlist.server;

import java.util.List;
import java.util.Map;

import ch.ubx.startlist.shared.ImportOLC;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class ImportOLCDAOobjectify implements ImportOLCDAO {

	static {
		ObjectifyService.register(ImportOLC.class);
	}

	@Override
	public void removeImportOLC(ImportOLC importOLC) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createOrUpdateImportOLC(ImportOLC importOLC) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(importOLC);

	}

	@Override
	public List<ImportOLC> listAllImportOLC() {
		Objectify ofy = ObjectifyService.begin();
		Query<ImportOLC> query = ofy.query(ImportOLC.class);
		return query.list();
	}

	@Override
	public Map<String, ImportOLC> listImportOLC(List<String> names) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.get(ImportOLC.class, names);
	}

}
