package org.toilelibre.libe.bank.actions;

import org.springframework.web.bind.annotation.RequestMethod;

public class Link {

    private final String           rel;
    private final String           href;
    private final RequestMethod [] methods;
    private final String []        params;
                                   
    public Link (final String rel1, final String href1, RequestMethod [] methods1, String [] params) {
        this.rel = rel1;
        this.href = href1;
        this.methods = methods1;
        this.params = params;
    }
    
    public String getRel () {
        return this.rel;
    }
    
    public String getHref () {
        return this.href;
    }
    
    public RequestMethod [] getMethods () {
        return methods.clone ();
    }
    
    public String [] getParams () {
        return params;
    }
    
    @Override
    public String toString () {
        StringBuffer stringBuffer = new StringBuffer ();
        for (RequestMethod method : this.methods) {
            if (stringBuffer.length () > 0) {
                stringBuffer.append ('\n');
            }
            stringBuffer.append (method.name ()).append (' ').append (this.href).append (' ');
        }
        return stringBuffer.append ("(" + this.rel + ")").toString ();
    }
}
