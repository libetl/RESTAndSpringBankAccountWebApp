package org.toilelibre.libe.bank.actions.entity;

import java.util.ArrayList;
import java.util.List;

public class ArrayNode extends ArrayList<Node>implements Node {

    /**
     *
     */
    private static final long serialVersionUID = -2436235972384989497L;

    @Override
    public boolean add (final Node node) {
        return super.add (node);
    }

    @Override
    public double asDouble () {
        return 0;
    }

    @Override
    public String asText () {
        return this.toString ();
    }

    public ArrayList<Node> getContent () {
        return this;
    }

    public void setAll (final List<Object> props) {
        for (final Object prop : props) {
            this.add (NodeFactory.instance.pojoNode (prop));
        }

    }

}
