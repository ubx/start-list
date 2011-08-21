package ch.ubx.startlist.server;

import java.util.List;

import ch.ubx.startlist.shared.FeNodeName;

import com.googlecode.objectify.Key;

public interface FeGenDAO<FeNode> {

    public Key<FeNode> getOrCreateKey(String name);

    public FeNode getOrCreate(String name);

    public FeNode get(Key<FeNode> key);

    public void delete(Key<FeNode> key);

    public void delete(FeNode feNode);

    public List<? extends FeNodeName> list();
}
