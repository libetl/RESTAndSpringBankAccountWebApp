package org.toilelibre.libe.bank.actions;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

public class Response<T> {

    private Link   self;
    private String type;
    private int    ok;
    @JacksonXmlElementWrapper
    private T      content;

    public Response () {
    }

    public Response (final Link self1, final T content1) {
        this.self = self1;
        this.type = content1.getClass ().getSimpleName ();
        this.content = content1;
        this.ok = 1;
    }

    public T getContent () {
        return this.content;
    }

    public int getOk () {
        return this.ok;
    }

    public Link getSelf () {
        return this.self;
    }

    public String getType () {
        return this.type;
    }
}
