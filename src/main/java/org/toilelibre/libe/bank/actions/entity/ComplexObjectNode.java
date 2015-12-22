package org.toilelibre.libe.bank.actions.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name = "resource")
@XmlSeeAlso({PrimitiveNode.class, ArrayNode.class})
public class ComplexObjectNode extends HashMap<String, Node> implements ObjectNode{


    static class Pair<T1, T2> {
        private final T1 key;
        private final T2 value;

        @XmlElement (name = "key")
        public T1 getKey () {
            return key;
        }

        @XmlElement (name = "value")
        public T2 getValue () {
            return value;
        }

        public Pair (T1 key1, T2 value1) {
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
    public List<Pair<String, Node>> getEntries () {
        List<Pair<String, Node>> list = new LinkedList<Pair<String, Node>> ();
        for (Map.Entry<String, Node> entry : super.entrySet ()) {
            list.add (new Pair<String, Node> (entry.getKey (), entry.getValue ()));
        }
        return list;
    }
    
    
    public Node get (String key) {
        return (Node) super.get (key);
    }

    public Iterator<String> fieldNames () {
        return this.keySet ().iterator ();
    }

    public String asText () {
        return "";
    }

    public ObjectNode set (String key, Node value) {
        super.put (key, value);
        return this;
    }

    public ObjectNode put (String key, Serializable value) {
        super.put (key, new PrimitiveNode (value));
        return this;
    }

    public ObjectNode addAll (Map<String, Object> props) {
        for (Map.Entry<String, Object> entry : props.entrySet ()) {
            this.put (entry.getKey (), NodeFactory.instance.pojoNode (entry.getValue ()));
        }
        return this;
    }

}
