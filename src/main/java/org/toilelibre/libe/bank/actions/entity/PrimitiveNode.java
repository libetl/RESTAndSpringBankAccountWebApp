package org.toilelibre.libe.bank.actions.entity;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize (using = PrimitiveNode.PrimitiveNodeWriter.class)
public class PrimitiveNode implements Node {

    public static class PrimitiveNodeWriter extends JsonSerializer<PrimitiveNode> {

        @Override
        public void serialize (final PrimitiveNode value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException, JsonProcessingException {
            if (value.value instanceof Number) {
                gen.writeNumber (value.asText ());
            } else if (value.value instanceof Boolean){
                gen.writeBoolean ((boolean)value.value);
            } else {
                gen.writeString (value.asText ());
            }
        }

    }

    private final Serializable value;

    public PrimitiveNode (final Serializable value1) {
        this.value = value1;
    }

    @Override
    public double asDouble () {
        return Double.parseDouble (this.value.toString ());
    }

    @Override
    public String asText () {
        return this.value.toString ();
    }

    @Override
    public boolean equals (final Object obj) {
        if (! (obj instanceof PrimitiveNode)) {
            return false;
        }
        return Objects.equals (this.value, ((PrimitiveNode) obj).value);
    }

    public String getString () {
        return this.value.toString ();
    }

    @Override
    public int hashCode () {
        return Objects.hashCode (this.value);
    }

    @Override
    public String toString () {
        return this.value == null ? "" : this.value.toString ();
    }

}
