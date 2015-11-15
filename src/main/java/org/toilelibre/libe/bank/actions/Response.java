package org.toilelibre.libe.bank.actions;

public class Response<T> {
    
    private final Link   self;
    private final String type;
    private final T      content;
                         
    public Response (final Link self1, final T content1) {
        this.self = self1;
        this.type = content1.getClass ().getSimpleName ();
        this.content = content1;
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
}
