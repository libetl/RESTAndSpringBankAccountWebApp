package org.toilelibre.libe.bank.actions.entity;

import java.io.IOException;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize (using = PrimitiveNode.PrimitiveNodeWriter.class)
@XmlType
public class PrimitiveNode implements Node {

    public static class PrimitiveNodeWriter extends JsonSerializer<PrimitiveNode> {

        @Override
        public void serialize (PrimitiveNode value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            if (value.value instanceof Number || value.value instanceof Boolean) {
                gen.writeRaw (value.asText ());
            }else {
                gen.writeString (value.asText ());
            }
        }
        
    }
    

    private Object value;
    
    public PrimitiveNode (Serializable value1) {
        this.value = value1;
    }

    public PrimitiveNode () {
    }

    @Override
    public String asText () {
        return this.value.toString ();
    }
    
}
