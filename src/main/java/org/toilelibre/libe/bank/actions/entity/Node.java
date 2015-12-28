package org.toilelibre.libe.bank.actions.entity;

import javax.xml.bind.annotation.XmlType;

@XmlType
public interface Node {


    public String asText ();

    public double asDouble ();
}
