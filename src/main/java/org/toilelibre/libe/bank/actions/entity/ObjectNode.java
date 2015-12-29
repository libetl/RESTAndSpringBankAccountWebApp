package org.toilelibre.libe.bank.actions.entity;

import java.io.Serializable;
import java.util.Iterator;

public interface ObjectNode<T extends Node> extends Node, Iterable<String> {

    public Node get (String string);

    @Override
    public Iterator<String> iterator ();

    public ObjectNode<T> put (String string, Serializable value);

    public ObjectNode<T> set (String string, T node);

}
