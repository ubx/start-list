package ch.ubx.startlist.server;

import java.util.List;
import java.util.Map;

import ch.ubx.startlist.shared.ImportOLC;

@Deprecated
public interface ImportOLCDAO {

	public void removeImportOLC(ImportOLC importOLC);

	public void createOrUpdateImportOLC(ImportOLC importOLC);

	public List<ImportOLC> listAllImportOLC();

	public Map<String, ImportOLC> listImportOLC(List<String> names);

}
