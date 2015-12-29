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

    private Node nodeAsArray (final Object object) {
        final ObjectMapper om = new ObjectMapper ();
        @SuppressWarnings ("unchecked")
        final List<Object> props = om.convertValue (object, List.class);
        final ArrayNode on = new ArrayNode ();
        on.setAll (props);
        return on;
    }

    private Node nodeAsMap (final Object object) {
        final Map<String, Object> props2 = this.transformChildrenProps (object);
        final ComplexObjectNode<PrimitiveNode> on = new ComplexObjectNode<PrimitiveNode> ();
        on.addAll (props2);
        return on;
    }

    public <T extends Node> ObjectNode<T> objectNode () {
        return new ComplexObjectNode<T> ();
    }

    public Node pojoNode (final Object object) {
        if (object == null) {
            return new PrimitiveNode (null);
        }
        if (object instanceof Node) {
            return (Node) object;
        }
        if (object.getClass ().isArray () || (object instanceof List)) {
            return this.nodeAsArray (object);
        }
        if (object.getClass ().isPrimitive () || (object instanceof CharSequence) || (object instanceof Number)) {
            return new PrimitiveNode ((Serializable) object);
        }
        return this.nodeAsMap (object);
    }

    private Map<String, Object> transformChildrenProps (final Object object) {
        final ObjectMapper om = new ObjectMapper ();
        @SuppressWarnings ("unchecked")
        final Map<String, Object> props = om.convertValue (object, Map.class);
        final Map<String, Object> props2 = new HashMap<String, Object> ();
        for (final Map.Entry<String, Object> entry : props.entrySet ()) {
            props2.put (entry.getKey (), this.pojoNode (entry.getValue ()));
        }
        return props2;
    }

}
