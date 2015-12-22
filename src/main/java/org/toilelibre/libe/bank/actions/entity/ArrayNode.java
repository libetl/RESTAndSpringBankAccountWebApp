package org.toilelibre.libe.bank.actions.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class ArrayNode extends ArrayList<Node> implements Node {

    /**
     * 
     */
    private static final long serialVersionUID = -2436235972384989497L;

    public boolean add (Node node) {
        return super.add (node);
    }

    @Override
    public String asText () {
        return this.toString ();
    }

    public void setAll (List<Object> props) {
        for (Object prop : props) {
            this.add (NodeFactory.instance.pojoNode (prop));
        }
        
    }


}
