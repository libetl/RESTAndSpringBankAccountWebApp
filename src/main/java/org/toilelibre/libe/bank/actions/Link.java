package org.toilelibre.libe.bank.actions;

import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.JsonNode;

public class Link {
    private final String rel;
    private final String href;
    private final RequestMethod[] methods;
    private final JsonNode params;

    public Link (final String rel1, final String href1, RequestMethod[] methods1, JsonNode params1) {
        this.rel = rel1;
        this.href = href1;
        this.methods = methods1;
        this.params = params1;
    }

    public String getRel () {
        return this.rel;
    }

    public String getHref () {
        return this.href;
    }

    public RequestMethod[] getMethods() {
        return methods.clone();
    }

    public JsonNode getParams() {
        return params;
    }
    
}
