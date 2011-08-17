package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FeStore;
import ch.ubx.startlist.shared.FeYear;

import com.googlecode.objectify.Key;

// TODO: Auto-generated Javadoc
/**
 * The Interface FeYearDAO.
 */
public interface FeYearDAO {

    /**
     * Gets the or create key.
     *
     * @param year the year
     * @param storeKey the store key
     * @return the or create key
     */
    public Key<FeYear> getOrCreateKey(long year, Key<FeStore> storeKey);

    /**
     * Gets the or create.
     *
     * @param year the year
     * @param storeKey the store key
     * @return the or create
     */
    public FeYear getOrCreate(long year, Key<FeStore> storeKey);

    /**
     * Gets the.
     *
     * @param key the key
     * @return the fe year
     */
    public FeYear get(Key<FeYear> key);

    /**
     * Delete.
     *
     * @param key the key
     */
    public void delete(Key<FeYear> key);

    /**
     * Delete.
     *
     * @param year the year
     */
    public void delete(FeYear year);

    /**
     * List.
     *
     * @return the list
     */
    public List<FeYear> list();

    /**
     * List.
     *
     * @param storeKey the store key
     * @return the list
     */
    public List<FeYear> list(Key<FeStore> storeKey);

}
