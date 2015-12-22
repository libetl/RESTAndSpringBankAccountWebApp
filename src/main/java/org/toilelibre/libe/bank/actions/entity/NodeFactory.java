package org.toilelibre.libe.bank.actions.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class NodeFactory {

    public static final NodeFactory instance = new NodeFactory ();

    private NodeFactory () {
    }
    
    public ArrayNode arrayNode () {
        return new ArrayNode ();
    }

    public ObjectNode objectNode () {
        return new ComplexObjectNode ();
    }

    public Node pojoNode (Object object) {
        if (object.getClass ().isArray ()) {
            return this.nodeAsArray (object);
        }
        if (object.getClass ().isPrimitive () || 
                object instanceof CharSequence) {
            return new PrimitiveNode ((Serializable)object);
        }
        return this.nodeAsMap (object);
    }

    private Node nodeAsArray (Object object) {
        ObjectMapper om = new ObjectMapper ();
        @SuppressWarnings ("unchecked")
        List<Object> props = om.convertValue (object, List.class);
        ArrayNode on = new ArrayNode ();
        on.setAll (props);
        return on;
    }
    private Node nodeAsMap (Object object) {
        ObjectMapper om = new ObjectMapper ();
        System.out.println (object);
        @SuppressWarnings ("unchecked")
        Map<String,Object> props = om.convertValue (object, Map.class);
        ComplexObjectNode on = new ComplexObjectNode ();
        on.addAll (props);
        return on;
    }

}
