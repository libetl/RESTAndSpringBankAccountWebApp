package org.toilelibre.libe.bank.actions.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "resource")
@XmlJavaTypeAdapter(value = ComplexObjectNode.Adapter.class)
public class ComplexObjectNode extends HashMap<String, Node> implements ObjectNode{

    @XmlType
    public class KeyValue {
        public KeyValue() {
        }

        public KeyValue(String key, String value) {
            this.key = key;
            this.value = value;
        }

        //obviously needs setters/getters
        String key;
        String value;
    }
    public class Adapter extends XmlAdapter<List<KeyValue>, ComplexObjectNode> {


        @Override
        public ComplexObjectNode unmarshal(List<KeyValue> v) throws Exception {
            Map<String, Node> map = new HashMap<>(v.size());
            for (KeyValue keyValue : v) {
                map.put(keyValue.key, NodeFactory.instance.pojoNode (keyValue.value));
            }
            return new ComplexObjectNode (map);
        }

        @Override
        public List<KeyValue> marshal(ComplexObjectNode v) throws Exception {
            Set<String> keys = v.keySet();
            List<KeyValue> results = new ArrayList<>(v.size());
            for (String key : keys) {
                results.add(new KeyValue(key, v.get(key).toString ()));
            }
            return results;
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


    @Override
    public Set<Entry<String, Node>> entrySet () {
        return super.entrySet ();
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
