package org.toilelibre.libe.bank.actions.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name = "resource")
@XmlSeeAlso({PrimitiveNode.class, ArrayNode.class})
public class ComplexObjectNode<T extends Node> extends HashMap<String, Node> implements ObjectNode<T> {


    static class KeyValue<T2> {
        private final String key;
        private final T2 value;

        @XmlAttribute (name = "key")
        public String getKey () {
            return key;
        }

        @XmlAnyElement
        public T2 getValue () {
            return value;
        }

        public KeyValue (String key1, T2 value1) {
            this.key = key1;
            this.value = value1;
        }
    }
    
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


    @XmlElement (name = "entry")
    public List<KeyValue<Node>> getEntries () {
        List<KeyValue<Node>> list = new LinkedList<KeyValue<Node>> ();
        for (Map.Entry<String, Node> entry : super.entrySet ()) {
            list.add (new KeyValue<Node> (entry.getKey (), entry.getValue ()));
        }
        return list;
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
