package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.client.ImportOLC;

public interface ImportOLCDAO {

	public void removeImportOLC(ImportOLC importOLC);

	public void createOrUpdateImportOLC(ImportOLC importOLC);

	public List<ImportOLC> listAllImportOLC();

}
