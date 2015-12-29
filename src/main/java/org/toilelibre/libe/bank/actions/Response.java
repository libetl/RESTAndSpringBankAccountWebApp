package org.toilelibre.libe.bank.actions;

public class Response<T> {
    
    private Link   self;
    private String type;
    private int    ok;
    private T      content;
                     
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
