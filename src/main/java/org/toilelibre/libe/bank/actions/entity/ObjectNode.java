package org.toilelibre.libe.bank.actions.entity;

import java.io.Serializable;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlType;

@XmlType
public interface ObjectNode extends Node {


    public Node get (String string);

    public Iterator<String> fieldNames ();

    public ObjectNode set (String string, Node node);

    public ObjectNode put (String string, Serializable value);

}
