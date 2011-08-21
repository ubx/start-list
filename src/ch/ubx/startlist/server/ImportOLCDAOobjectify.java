package ch.ubx.startlist.server;

import java.util.List;
import java.util.Map;

import ch.ubx.startlist.shared.ImportOLC;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;

public class ImportOLCDAOobjectify extends DAOBase implements ImportOLCDAO {

	static {
		ObjectifyService.register(ImportOLC.class);
	}

	@Override
	public void removeImportOLC(ImportOLC importOLC) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createOrUpdateImportOLC(ImportOLC importOLC) {
		ofy().put(importOLC);

	}

	@Override
	public List<ImportOLC> listAllImportOLC() {
		Query<ImportOLC> query = ofy().query(ImportOLC.class);
		return query.list();
	}

	@Override
	public Map<String, ImportOLC> listImportOLC(List<String> names) {
		return ofy().get(ImportOLC.class, names);
	}

}
