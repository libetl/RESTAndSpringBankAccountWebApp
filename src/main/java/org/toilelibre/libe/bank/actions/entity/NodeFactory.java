package org.toilelibre.libe.bank.actions.entity;

import java.io.Serializable;
import java.util.HashMap;
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

    public <T extends Node> ObjectNode<T> objectNode () {
        return new ComplexObjectNode<T> ();
    }

    public Node pojoNode (Object object) {
        if (object instanceof Node) {
            return (Node) object;
        }
        if (object.getClass ().isArray () || object instanceof List) {
            return this.nodeAsArray (object);
        }
        if (object.getClass ().isPrimitive () || 
                object instanceof CharSequence || object instanceof Number) {
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
        Map<String, Object> props2 = this.transformChildrenProps (object);
        ComplexObjectNode<PrimitiveNode> on = new ComplexObjectNode<PrimitiveNode> ();
        on.addAll (props2);
        return on;
    }

    private Map<String, Object> transformChildrenProps (Object object) {
        ObjectMapper om = new ObjectMapper ();
        @SuppressWarnings ("unchecked")
        Map<String,Object> props = om.convertValue (object, Map.class);
        Map<String,Object> props2 = new HashMap<String, Object> ();
        for (Map.Entry<String, Object> entry : props.entrySet ()) {
            props2.put (entry.getKey (), this.pojoNode (entry.getValue ()));
        }
        return props2;
    }

}
