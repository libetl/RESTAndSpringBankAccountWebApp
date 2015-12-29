package org.toilelibre.libe.bank.actions.entity;

import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


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
    
    private Serializable value;

    public PrimitiveNode () {
    }
    
    public PrimitiveNode (Serializable value1) {
        this.value = value1;
    }


    @Override
    public String asText () {
        return this.value.toString ();
    }

    @Override
    public double asDouble () {
        return Double.parseDouble (this.value.toString ());
    }

    public String getString () {
        return this.value.toString ();
    }
    
}
