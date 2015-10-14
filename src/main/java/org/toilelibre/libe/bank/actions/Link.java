package org.toilelibre.libe.bank.actions;

public class Link {
    private final String rel;
    private final String href;

    public Link (final String rel1, final String href1) {
        this.rel = rel1;
        this.href = href1;
    }

    public String getRel () {
        return this.rel;
    }

    public String getHref () {
        return this.href;
    }
}
