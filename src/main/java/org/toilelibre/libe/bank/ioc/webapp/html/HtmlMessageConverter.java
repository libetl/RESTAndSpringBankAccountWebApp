package org.toilelibre.libe.bank.ioc.webapp.html;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.toilelibre.libe.bank.actions.Response;
import org.toilelibre.libe.bank.actions.entity.ArrayNode;
import org.toilelibre.libe.bank.actions.entity.ComplexObjectNode;
import org.toilelibre.libe.bank.actions.entity.Node;
import org.toilelibre.libe.bank.actions.entity.NodeFactory;
import org.toilelibre.libe.bank.actions.entity.ObjectNode;
import org.toilelibre.libe.bank.actions.entity.PrimitiveNode;

public class HtmlMessageConverter implements GenericHttpMessageConverter<Object> {

    private static final String FOOTER_WITH_GO_BACK_LINK = "<p><a href=\"javascript:history.go(-1)\">Go back</a></p></body></html></body></html>";
    private static final String HEADER_WITH_DOCTYPE      = "<!doctype html><html><head><title>";

    @Override
    public boolean canRead (final Class<?> clazz, final MediaType mediaType) {
        return (mediaType == null) || mediaType.includes (this.getSupportedMediaTypes ().get (0));
    }

    @Override
    public boolean canRead (final Type type, final Class<?> contextClass, final MediaType mediaType) {
        return this.canRead (null, mediaType);
    }

    @Override
    public boolean canWrite (final Class<?> clazz, final MediaType mediaType) {
        return (mediaType == null) || mediaType.includes (this.getSupportedMediaTypes ().get (0));
    }

    @Override
    public boolean canWrite (final Type type, final Class<?> clazz, final MediaType mediaType) {
        return this.canWrite (clazz, mediaType);

    }

    @Override
    public List<MediaType> getSupportedMediaTypes () {
        return Collections.singletonList (MediaType.TEXT_HTML);
    }

    @Override
    public Object read (final Class<? extends Object> clazz, final HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        throw new IllegalArgumentException ("Cannot read text/html as a request body");
    }

    @Override
    public Object read (final Type type, final Class<?> contextClass, final HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return this.read (contextClass, inputMessage);
    }

    @Override
    public void write (final Object t, final MediaType contentType, final HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        this.write (t, null, contentType, outputMessage);

    }

    @SuppressWarnings ("unchecked")
    @Override
    public void write (final Object t, final Type type, final MediaType contentType, final HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (! (t instanceof Response)) {
            boolean written = false;
            written = this.writeError (t, outputMessage);
            this.writeDefaultResponse (outputMessage, written);
            return;
        }
        final Response<Object> response = (Response<Object>) t;
        outputMessage.getBody ().write (HtmlMessageConverter.HEADER_WITH_DOCTYPE.getBytes ());
        outputMessage.getBody ().write (response.getSelf ().getRel ().getBytes ());
        outputMessage.getBody ().write ("</title></head><body><h1>".getBytes ());
        outputMessage.getBody ().write (response.getSelf ().getRel ().getBytes ());
        outputMessage.getBody ().write ("</h1><p>".getBytes ());
        outputMessage.getBody ().write (response.getOk () == 1 ? "<p>Request successful</p>".getBytes () : "<p>Problem during request</p>".getBytes ());
        outputMessage.getBody ().write ("<h2>".getBytes ());
        outputMessage.getBody ().write (response.getType ().getBytes ());

        final Node content = NodeFactory.instance.pojoNode (response.getContent ());

        final ArrayNode links = new ArrayNode ();
        if ( (content instanceof ComplexObjectNode) && ((ComplexObjectNode<Node>) content).containsKey ("links")) {
            links.addAll ((ArrayNode) ((ComplexObjectNode<Node>) content).remove ("links"));
        }
        outputMessage.getBody ().write ("</h2><p>".getBytes ());
        this.writeContent (content, outputMessage, links);
        outputMessage.getBody ().write ("</p><p>".getBytes ());
        this.writeLinks (links, outputMessage);
        outputMessage.getBody ().write ("</p><p><a href=\"javascript:history.go(-1)\">Go back</a></p></body></html>".getBytes ());
    }

    private void writeContent (final ArrayNode value, final HttpOutputMessage outputMessage, final ArrayNode links) throws IOException {
        outputMessage.getBody ().write ("<ol>".getBytes ());
        if (value.size () == 0) {
            outputMessage.getBody ().write ( ("<li>Nothing</li>").getBytes ());
        }
        for (final Node node : value) {
            outputMessage.getBody ().write ( ("<li>").getBytes ());
            this.writeContent (node, outputMessage, links);
            outputMessage.getBody ().write ("</li>".getBytes ());
        }
        outputMessage.getBody ().write ("</ol>".getBytes ());
    }

    private void writeContent (final ComplexObjectNode<Node> content, final HttpOutputMessage outputMessage, final ArrayNode links) throws IOException {
        outputMessage.getBody ().write ("<ul>".getBytes ());

        if (content.containsKey ("links")) {
            links.addAll ((ArrayNode) (content).remove ("links"));
        }
        if (content.size () == 0) {
            outputMessage.getBody ().write ( ("<li>Nothing</li>").getBytes ());
        }
        for (final Entry<String, Node> node : content.entrySet ()) {
            outputMessage.getBody ().write ( ("<li>" + node.getKey () + " : ").getBytes ());
            this.writeContent (node.getValue (), outputMessage, links);
            outputMessage.getBody ().write ("</li>".getBytes ());
        }
        outputMessage.getBody ().write ("</ul>".getBytes ());
    }

    @SuppressWarnings ("unchecked")
    private void writeContent (final Node value, final HttpOutputMessage outputMessage, final ArrayNode links) throws IOException {
        if (value instanceof ArrayNode) {
            this.writeContent ( ((ArrayNode) value), outputMessage, links);
        }
        if (value instanceof ComplexObjectNode) {
            this.writeContent ( ((ComplexObjectNode<Node>) value), outputMessage, links);
        }
        if (value instanceof PrimitiveNode) {
            this.writeContent ( ((PrimitiveNode) value), outputMessage);
        }
    }

    private void writeContent (final PrimitiveNode value, final HttpOutputMessage outputMessage) throws IOException {
        outputMessage.getBody ().write ( ("<code>" + value.asText () + "</code>").getBytes ());
    }

    private void writeDefaultResponse (final HttpOutputMessage outputMessage, final boolean alreadyWritten) throws IOException {
        if (!alreadyWritten) {
            outputMessage.getBody ().write ( (HtmlMessageConverter.HEADER_WITH_DOCTYPE
                    + "Unrecognized response</title></head><body><h1>Problem</h1><p>Sorry, unrecognized response</p>" + HtmlMessageConverter.FOOTER_WITH_GO_BACK_LINK).getBytes ());
        }
    }

    private boolean writeError (final Object t, final HttpOutputMessage outputMessage) throws IOException {
        if ( (t instanceof ObjectNode) && "0".equals ( ((ObjectNode<?>) t).get ("ok").asText ())) {
            final ObjectNode<?> errorPayload = (ObjectNode<?>) t;
            outputMessage.getBody ()
                    .write ( (HtmlMessageConverter.HEADER_WITH_DOCTYPE + "Error</title></head><body><h1>" + errorPayload.get ("name").asText ()
                            + "</h1><p>The kind of this error is <code>" + errorPayload.get ("kind").asText () + "</code></p><p><u>Description :</u> "
                            + errorPayload.get ("description").asText () + "</p>" + HtmlMessageConverter.FOOTER_WITH_GO_BACK_LINK).getBytes ());
            return true;
        }
        return false;
    }

    private void writeLinks (final ArrayNode links, final HttpOutputMessage outputMessage) throws IOException {
        if (links.size () == 0) {
            return;
        }
        outputMessage.getBody ().write ("<h2>Links</h2><ul>".getBytes ());
        for (final Node link : links) {
            @SuppressWarnings ("unchecked")
            final ComplexObjectNode<PrimitiveNode> trueLink = (ComplexObjectNode<PrimitiveNode>) link;
            outputMessage.getBody ().write (
                    ("<li><a href=\"" + trueLink.get ("href").asText () + "\" title=\"" + trueLink.get ("rel").asText () + "\">" + trueLink.get ("href").asText () + "</a></li>")
                            .getBytes ());
        }
        outputMessage.getBody ().write ("</ul>".getBytes ());
    }
}