package org.toilelibre.libe.bank.ioc.webapp.html;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

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

    private static final String HREF                     = "href";
    private static final String RELATIONSHIP             = "rel";
    private static final String SUGGESTED_LINK           = "suggestedLink";
    private static final String BINDING                  = "binding";
    private static final String METHODS                  = "methods";
    private static final String SCRIPT_UPDATE_LINK       = "<script type=\"text/javascript\">function updateLink (link) {var newLink = $('#link-' + link).data ('model');"
            + "var vars = $('[id^=link-' + link + '-]');$.each (vars, function (i) {newLink = newLink.replace ($(vars [i]).data('model'), $(vars [i]).val ());});$('a#link-' + link).attr ('href', newLink);$('form#link-' + link).attr ('action', newLink);}$(\"input[type='text']\").val (\"\");</script>";
    private static final String FOOTER_WITH_GO_BACK_LINK = "<p><a id=\"goBack\" href=\"javascript:history.go(-1)\">Go back</a></p>" + HtmlMessageConverter.SCRIPT_UPDATE_LINK
            + "</body></html>";
    private static final String HEADER_WITH_DOCTYPE      = "<!doctype html><html><head><script src=\"//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js\"></script>"
            + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" "
            + "integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\"/><link rel=\"stylesheet\" href=\""
            + "https://bootswatch.com/4/darkly/bootstrap.css\"/><script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" "
            + "integrity=\"sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS\" crossorigin=\"anonymous\"></script><title>";

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

    private String displayReplaceableParams (final String linkId, final String url) {
        return url.replaceAll ("\\{([^}]+)\\}",
                "<input type=\"text\" data-model=\"{$1}\" id=\"link-" + linkId + "-$1\" onclick=\"javascript:return false\" onselect=\"javascript:updateLink ('" + linkId
                        + "')\" onkeyup=\"javascript:updateLink ('" + linkId + "')\" placeholder=\"$1\"/>");
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
        outputMessage.getHeaders ().setContentType (MediaType.TEXT_HTML);
        final OutputStream stream = outputMessage.getBody ();
        if (! (t instanceof Response)) {
            boolean written = false;
            written = this.writeError (t, stream);
            this.writeDefaultResponse (stream, written);
            return;
        }
        final Response<Object> response = (Response<Object>) t;
        stream.write (HtmlMessageConverter.HEADER_WITH_DOCTYPE.getBytes ());
        stream.write (response.getSelf ().getRel ().getBytes ());
        stream.write ("</title></head><body><h1>".getBytes ());
        stream.write (response.getSelf ().getRel ().getBytes ());
        stream.write ("</h1><p>".getBytes ());
        stream.write (response.getOk () == 1 ? "<p>Request successful</p>".getBytes () : "<p>Problem during request</p>".getBytes ());
        stream.write ("<h2>".getBytes ());
        stream.write (response.getType ().getBytes ());

        final Node content = NodeFactory.instance.pojoNode (response.getContent ());

        final ArrayNode links = new ArrayNode ();
        if ( (content instanceof ComplexObjectNode) && ((ComplexObjectNode<Node>) content).containsKey ("links")) {
            links.addAll ((ArrayNode) ((ComplexObjectNode<Node>) content).remove ("links"));
        }
        stream.write ("</h2>".getBytes ());
        this.writeContent (content, stream, links);
        this.writeLinks ("Links", links, stream);
        stream.write (HtmlMessageConverter.FOOTER_WITH_GO_BACK_LINK.getBytes ());
    }

    private void writeContent (final ArrayNode value, final OutputStream stream, final ArrayNode links) throws IOException {
        stream.write ("<ol>".getBytes ());
        if (value.size () == 0) {
            stream.write ( ("<li>Nothing</li>").getBytes ());
        }
        for (final Node node : value) {
            stream.write ( ("<li>").getBytes ());
            this.writeContent (node, stream, links);
            stream.write ("</li>".getBytes ());
        }
        stream.write ("</ol>".getBytes ());
    }

    private void writeContent (final ComplexObjectNode<Node> content, final OutputStream stream, final ArrayNode links) throws IOException {
        stream.write ("<ul>".getBytes ());

        if (content.containsKey ("links")) {
            links.addAll ((ArrayNode) (content).remove ("links"));
        }
        if (content.size () == 0) {
            stream.write ( ("<li>Nothing</li>").getBytes ());
        }
        for (final Entry<String, Node> node : content.entrySet ()) {
            stream.write ( ("<li>" + node.getKey () + " : ").getBytes ());
            this.writeContent (node.getValue (), stream, links);
            stream.write ("</li>".getBytes ());
        }
        stream.write ("</ul>".getBytes ());
    }

    @SuppressWarnings ("unchecked")
    private void writeContent (final Node value, final OutputStream stream, final ArrayNode links) throws IOException {
        if (value instanceof ArrayNode) {
            this.writeContent ( ((ArrayNode) value), stream, links);
        }
        if (value instanceof ComplexObjectNode) {
            this.writeContent ( ((ComplexObjectNode<Node>) value), stream, links);
        }
        if (value instanceof PrimitiveNode) {
            this.writeContent ( ((PrimitiveNode) value), stream);
        }
    }

    private void writeContent (final PrimitiveNode value, final OutputStream stream) throws IOException {
        stream.write ( ("<code>" + value.asText () + "</code>").getBytes ());
    }

    private void writeDefaultResponse (final OutputStream stream, final boolean alreadyWritten) throws IOException {
        if (!alreadyWritten) {
            stream.write ( (HtmlMessageConverter.HEADER_WITH_DOCTYPE + "Unrecognized response</title></head><body><h1>Problem</h1><p>Sorry, unrecognized response</p>"
                    + HtmlMessageConverter.FOOTER_WITH_GO_BACK_LINK).getBytes ());
        }
    }

    @SuppressWarnings ("unchecked")
    private boolean writeError (final Object t, final OutputStream stream) throws IOException {
        if ( (t instanceof ObjectNode) && "0".equals ( ((ObjectNode<?>) t).get ("ok").asText ())) {
            final ObjectNode<?> errorPayload = (ObjectNode<?>) t;
            stream.write (
                    (HtmlMessageConverter.HEADER_WITH_DOCTYPE + "Error</title></head><body><h1>" + errorPayload.get ("name").asText () + "</h1><p>The kind of this error is <code>"
                            + errorPayload.get ("kind").asText () + "</code></p><p><u>Description :</u> " + errorPayload.get ("description").asText () + "</p>").getBytes ());
            this.writeSuggestedLink ((ObjectNode<Node>) t, stream);
            stream.write (HtmlMessageConverter.FOOTER_WITH_GO_BACK_LINK.getBytes ());
            return true;
        }
        return false;
    }

    @SuppressWarnings ("unchecked")
    private void writeForm (final ComplexObjectNode<PrimitiveNode> trueLink, final UUID linkUuid, final OutputStream stream) throws IOException {
        boolean foundUnknownParameters = false;
        stream.write ( ("<li id=\"" + linkUuid.toString () + "\">" + trueLink.get (HtmlMessageConverter.RELATIONSHIP).asText () + " : ").getBytes ());
        stream.write ( ("<div class=\"col-12\"><div class=\"well bs-component\"><form class=\"form-horizontal\" data-model=\"" + trueLink.get (HtmlMessageConverter.HREF).asText ()
                + "\" method=\"" + ((ArrayNode) trueLink.get (HtmlMessageConverter.METHODS)).get (0).asText () + "\" enctype=\"x-www-form-urlencoded\" id=\"link-"
                + linkUuid.toString () + "\" action=\"" + trueLink.get (HtmlMessageConverter.HREF).asText () + "\">").getBytes ());
        stream.write ( ("<fieldset><legend title=\"" + linkUuid.toString () + "\">"
                + this.displayReplaceableParams (linkUuid.toString (), trueLink.get (HtmlMessageConverter.HREF).asText ()) + "</legend>").getBytes ());
        for (final Node param : (ArrayNode) trueLink.get ("params")) {
            if ("unknownParameters".equals ( ((ComplexObjectNode<PrimitiveNode>) param).get (HtmlMessageConverter.BINDING).asText ())) {
                foundUnknownParameters = true;
            }
            this.writeInputField (param, linkUuid.toString (), trueLink, stream);
        }

        stream.write ("<input class=\"btn btn-success\" type=\"submit\" value=\"send\"/></fieldset></form>".getBytes ());
        if (!"POST".equals ( ((ArrayNode) trueLink.get (HtmlMessageConverter.METHODS)).get (0).asText ()) || foundUnknownParameters) {
            this.writeSubmissionScript (linkUuid.toString (), stream);
        }
        stream.write ("</div></div></li>".getBytes ());

    }

    private void writeInputField (final Node param, final String linkId, final ComplexObjectNode<PrimitiveNode> trueLink, final OutputStream stream) throws IOException {
        @SuppressWarnings ("unchecked")
        final ComplexObjectNode<PrimitiveNode> paramAsNode = (ComplexObjectNode<PrimitiveNode>) param;
        stream.write ( ("<div class=\"form-group\"><label class=\"col-lg-2 control-label\" for=\"" + paramAsNode.get (HtmlMessageConverter.BINDING).asText () + "\">"
                + paramAsNode.get (HtmlMessageConverter.BINDING).asText () + "</label><div class=\"col-lg-10\"><input id=\"link-" + linkId + "-"
                + paramAsNode.get (HtmlMessageConverter.BINDING).asText () + "\" type=\"text\" name=\"" + paramAsNode.get (HtmlMessageConverter.BINDING).asText ()
                + "\" type=\"submit\" placeholder=\"" + paramAsNode.get ("type").asText () + "\"/></div></div>").getBytes ());
    }

    private void writeLinks (final String headerTitle, final ArrayNode links, final OutputStream stream) throws IOException {
        if (links.size () == 0) {
            return;
        }
        stream.write ( ("<h2>" + headerTitle + "</h2><ul>").getBytes ());
        for (final Node link : links) {
            @SuppressWarnings ("unchecked")
            final ComplexObjectNode<PrimitiveNode> trueLink = (ComplexObjectNode<PrimitiveNode>) link;
            final UUID linkUuid = UUID.randomUUID ();
            if ( ((ArrayNode) trueLink.get (HtmlMessageConverter.METHODS)).contains (new PrimitiveNode ("GET"))) {
                this.writeSimpleGetLink (trueLink, linkUuid, stream);
            } else if (! ((ArrayNode) trueLink.get (HtmlMessageConverter.METHODS)).isEmpty ()) {
                this.writeForm (trueLink, linkUuid, stream);
            }
        }
        stream.write ("</ul>".getBytes ());
    }

    private void writeSimpleGetLink (final ComplexObjectNode<PrimitiveNode> trueLink, final UUID linkUuid, final OutputStream stream) throws IOException {
        stream.write ( ("<li id=\"" + linkUuid + "\">" + trueLink.get (HtmlMessageConverter.RELATIONSHIP).asText () + " : <a href=\""
                + trueLink.get (HtmlMessageConverter.HREF).asText () + "\" data-model=\"" + trueLink.get (HtmlMessageConverter.HREF).asText () + "\" id=\"link-" + linkUuid
                + "\" title=\"" + trueLink.get (HtmlMessageConverter.RELATIONSHIP).asText () + "\">"
                + this.displayReplaceableParams (linkUuid.toString (), trueLink.get (HtmlMessageConverter.HREF).asText ()) + "</a></li>").getBytes ());
    }

    private void writeSubmissionScript (final String linkId, final OutputStream stream) throws IOException {
        stream.write ( ("<script type=\"text/javascript\">$(document).ready(function() {$('#link-" + linkId
                + "').on('submit', function(e) {e.preventDefault();var $this = $(this);var renderResult = function(html) {html = html.responseText ? html.responseText : html;    var body = html.substring (html.indexOf ('<body>'));    body = body.substring (0, html.indexOf ('</body>'));    $('body').html ($(body).wrapAll('<div>').parent().html());    $('#goBack').attr ('href', 'javascript:history.go (0)');};$.ajax({url: $this.attr('action'),type: $this.attr('method'),data: $('#link-"
                + linkId + "-unknownParameters').length > 0 ? $('#link-" + linkId
                + "-unknownParameters').val () : $this.serialize(),dataType: \"html\",success: renderResult,error: renderResult});});});</script>").getBytes ());
    }

    private void writeSuggestedLink (final ObjectNode<Node> content, final OutputStream stream) throws IOException {
        final ArrayNode links = new ArrayNode ();
        if ( (content instanceof ComplexObjectNode) && ((ComplexObjectNode<Node>) content).containsKey (HtmlMessageConverter.SUGGESTED_LINK)) {
            links.addAll ((ArrayNode) ((ComplexObjectNode<Node>) content).remove (HtmlMessageConverter.SUGGESTED_LINK));
        }
        this.writeLinks ("Suggested link", links, stream);

    }
}
