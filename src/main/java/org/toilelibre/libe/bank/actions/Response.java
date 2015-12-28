package org.toilelibre.libe.bank.actions;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.toilelibre.libe.bank.actions.entity.ArrayNode;
import org.toilelibre.libe.bank.actions.entity.ComplexObjectNode;
import org.toilelibre.libe.bank.actions.entity.NodeFactory;
import org.toilelibre.libe.bank.actions.entity.PrimitiveNode;
import org.toilelibre.libe.bank.impl.account.balance.CustomerAccountBalance;
import org.toilelibre.libe.bank.impl.account.details.CustomerAccountDetails;

@XmlRootElement (name="response")
@XmlSeeAlso({ArrayNode.class, PrimitiveNode.class, ComplexObjectNode.class, CustomerAccountDetails.class, CustomerAccountBalance.class
             })
public class Response<T> {
    
    @XmlElement (name="self")
    private Link   self;
    @XmlElement (name="type")
    private String type;
    @XmlElement (name="content")
    private Object   contentAsNode;
    @XmlElement (name="ok")
    private int    ok;
    private T      content;
                     
    public Response (){
    }
    
    public Response (final Link self1, final T content1) {
        this.self = self1;
        this.type = content1.getClass ().getSimpleName ();
        this.content = content1;
        this.contentAsNode = NodeFactory.instance.pojoNode (content1);
        this.ok = 1;
    }
    
    public Link getSelf () {
        return this.self;
    }
    
    public String getType () {
        return this.type;
    }
    
    public T getContent () {
        return this.content;
    }

    public int getOk () {
        return ok;
    }
}
