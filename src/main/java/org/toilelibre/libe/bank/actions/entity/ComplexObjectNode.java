package org.toilelibre.libe.bank.actions.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ComplexObjectNode<T extends Node> extends HashMap<String, Node> implements ObjectNode<T> {

    /**
     * 
     */
    private static final long serialVersionUID = -3493692606939012738L;


    public ComplexObjectNode (Map<String, Node> map) {
        super (map);
    }


    public ComplexObjectNode () {
        super ();
    }    
    
    public Node get (String key) {
        return (Node) super.get (key);
    }

    public Iterator<String> iterator () {
        return this.keySet ().iterator ();
    }

    public String asText () {
        return "";
    }

    @Override
    public double asDouble () {
        return 0;
    }

    public ObjectNode<T> set (String key, T value) {
        super.put (key, value);
        return this;
    }

    public ObjectNode<T> put (String key, Serializable value) {
        super.put (key, new PrimitiveNode (value));
        return this;
    }

    public ObjectNode<T> addAll (Map<String, Object> props) {
        for (Map.Entry<String, Object> entry : props.entrySet ()) {
            this.put (entry.getKey (), NodeFactory.instance.pojoNode (entry.getValue ()));
        }
        return this;
    }


}
