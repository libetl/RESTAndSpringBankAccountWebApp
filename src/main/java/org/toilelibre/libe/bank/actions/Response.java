package org.toilelibre.libe.bank.actions;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="response")
public class Response<T> {
    
    @XmlElement (name="self")
    private Link   self;
    @XmlElement (name="type")
    private String type;
    private T      content;
    @XmlElement (name="ok")
    private int    ok;
                     
    public Response (){
    }
    
    public Response (final Link self1, final T content1) {
        this.self = self1;
        this.type = content1.getClass ().getSimpleName ();
        this.content = content1;
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
