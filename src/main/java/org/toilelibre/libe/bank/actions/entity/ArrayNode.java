package org.toilelibre.libe.bank.actions.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name = "array")
@XmlSeeAlso({PrimitiveNode.class, ComplexObjectNode.class})
public class ArrayNode extends ArrayList<Node> implements Node {

    /**
     * 
     */
    private static final long serialVersionUID = -2436235972384989497L;

    public boolean add (Node node) {
        return super.add (node);
    }

    @XmlAnyElement
    public ArrayList<Node> getContent () {
        return this;
    }
    
    @Override
    public String asText () {
        return this.toString ();
    }

    @Override
    public double asDouble () {
               return 0;
    }

    public void setAll (List<Object> props) {
        for (Object prop : props) {
            this.add (NodeFactory.instance.pojoNode (prop));
        }
        
    }


}
