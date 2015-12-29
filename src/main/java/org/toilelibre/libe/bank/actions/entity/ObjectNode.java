package org.toilelibre.libe.bank.actions.entity;

import java.io.Serializable;
import java.util.Iterator;

public interface ObjectNode<T extends Node> extends Node, Iterable<String> {


    public Node get (String string);

    public Iterator<String> iterator ();

    public ObjectNode<T> set (String string, T node);

    public ObjectNode<T> put (String string, Serializable value);

}
