package org.toilelibre.libe.bank.actions.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ComplexObjectNode<T extends Node> extends HashMap<String, Node>implements ObjectNode<T> {

    /**
     *
     */
    private static final long serialVersionUID = -3493692606939012738L;

    public ComplexObjectNode () {
        super ();
    }

    public ComplexObjectNode (final Map<String, Node> map) {
        super (map);
    }

    public ObjectNode<T> addAll (final Map<String, Object> props) {
        for (final Map.Entry<String, Object> entry : props.entrySet ()) {
            this.put (entry.getKey (), NodeFactory.instance.pojoNode (entry.getValue ()));
        }
        return this;
    }

    @Override
    public double asDouble () {
        return 0;
    }

    @Override
    public String asText () {
        return "";
    }

    @Override
    public Node get (final String key) {
        return super.get (key);
    }

    @Override
    public Iterator<String> iterator () {
        return this.keySet ().iterator ();
    }

    @Override
    public ObjectNode<T> put (final String key, final Serializable value) {
        super.put (key, new PrimitiveNode (value));
        return this;
    }

    @Override
    public ObjectNode<T> set (final String key, final T value) {
        super.put (key, value);
        return this;
    }

}
