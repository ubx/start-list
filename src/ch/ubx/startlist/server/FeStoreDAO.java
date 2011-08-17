package ch.ubx.startlist.server;

import java.util.List;

import com.googlecode.objectify.Key;

import ch.ubx.startlist.shared.FeStore;

// TODO: Auto-generated Javadoc
/**
 * The Interface FeStoreDAO.
 */
public interface FeStoreDAO {

    /**
     * Gets the or create key.
     *
     * @param name the name
     * @return the or create key
     */
    public Key<FeStore> getOrCreateKey(String name);

    /**
     * Gets the or create.
     *
     * @param name the name
     * @return the or create
     */
    public FeStore getOrCreate(String name);

    /**
     * Gets the.
     *
     * @param key the key
     * @return the fe store
     */
    public FeStore get(Key<FeStore> key);

    /**
     * Delete.
     *
     * @param key the key
     */
    public void delete(Key<FeStore> key);

    /**
     * Delete.
     *
     * @param feStore the fe store
     */
    public void delete(FeStore feStore);

    /**
     * List.
     *
     * @return the list
     */
    public List<FeStore> list();
}
