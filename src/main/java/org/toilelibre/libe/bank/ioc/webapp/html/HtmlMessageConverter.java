package org.toilelibre.libe.bank.ioc.webapp.html;

import java.io.IOException;
import java.io.OutputStream;
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

    private static final String SCRIPT_UPDATE_LINK       = "<script type=\"text/javascript\">function updateLink (link) {var newLink = $('#link-' + link).data ('model');"
            + "var vars = $('[id^=link-' + link + '-]');$.each (vars, function (i) {newLink = newLink.replace ($(vars [i]).data('model'), $(vars [i]).val ());});$('a#link-' + link).attr ('href', newLink);$('form#link-' + link).attr ('action', newLink);}$(\"input[type='text']\").val (\"\");</script>";
    private static final String FOOTER_WITH_GO_BACK_LINK = "<p><a id=\"goBack\" href=\"javascript:history.go(-1)\">Go back</a></p>" + HtmlMessageConverter.SCRIPT_UPDATE_LINK
            + "</body></html>";
    private static final String HEADER_WITH_DOCTYPE      = "<!doctype html><html><head>" + "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js\"></script>"
            + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" "
            + "integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\"/><link rel=\"stylesheet\" href=\""
            + "https://bootswatch.com/darkly/bootstrap.css\"/><script src=\"" + "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" "
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

    private String displayReplaceableParams (final String rel, final String url) {
        return url.replaceAll ("\\{([^}]+)\\}",
                "<input type=\"text\" data-model=\"{$1}\" id=\"link-" + rel + "-$1\" onclick=\"javascript:return false\" onselect=\"javascript:updateLink ('" + rel
                        + "')\" onkeyup=\"javascript:updateLink ('" + rel + "')\" placeholder=\"$1\"/>");
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
        this.writeLinks (links, stream);
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

    private boolean writeError (final Object t, final OutputStream stream) throws IOException {
        if ( (t instanceof ObjectNode) && "0".equals ( ((ObjectNode<?>) t).get ("ok").asText ())) {
            final ObjectNode<?> errorPayload = (ObjectNode<?>) t;
            stream.write ( (HtmlMessageConverter.HEADER_WITH_DOCTYPE + "Error</title></head><body><h1>" + errorPayload.get ("name").asText ()
                    + "</h1><p>The kind of this error is <code>" + errorPayload.get ("kind").asText () + "</code></p><p><u>Description :</u> "
                    + errorPayload.get ("description").asText () + "</p>" + HtmlMessageConverter.FOOTER_WITH_GO_BACK_LINK).getBytes ());
            return true;
        }
        return false;
    }

    private void writeForm (final ComplexObjectNode<PrimitiveNode> trueLink, final OutputStream stream) throws IOException {
        stream.write ( ("<li id=\"" + trueLink.get ("rel").asText () + "\">" + trueLink.get ("rel").asText () + " : ").getBytes ());
        stream.write ( ("<div class=\"col-12\"><div class=\"well bs-component\"><form class=\"form-horizontal\" data-model=\"" + trueLink.get ("href").asText () + "\" method=\""
                + ((ArrayNode) trueLink.get ("methods")).get (0).asText () + "\" enctype=\"x-www-form-urlencoded\" id=\"link-" + trueLink.get ("rel").asText () + "\" action=\""
                + trueLink.get ("href").asText () + "\">").getBytes ());
        stream.write ( ("<fieldset><legend title=\"" + trueLink.get ("rel").asText () + "\">"
                + this.displayReplaceableParams (trueLink.get ("rel").asText (), trueLink.get ("href").asText ()) + "</legend>").getBytes ());
        for (final Node param : (ArrayNode) trueLink.get ("params")) {
            this.writeInputField (param, trueLink, stream);
        }

        stream.write ("<input class=\"btn btn-success\" type=\"submit\" value=\"send\"/></fieldset></form>".getBytes ());
        if (!"POST".equals ( ((ArrayNode) trueLink.get ("methods")).get (0).asText ())) {
            this.writeSubmissionScript (trueLink, stream);
        }
        stream.write ("</div></div></li>".getBytes ());

    }

    private void writeInputField (final Node param, final ComplexObjectNode<PrimitiveNode> trueLink, final OutputStream stream) throws IOException {
        @SuppressWarnings ("unchecked")
        final ComplexObjectNode<PrimitiveNode> paramAsNode = (ComplexObjectNode<PrimitiveNode>) param;
        stream.write ( ("<div class=\"form-group\"><label class=\"col-lg-2 control-label\" for=\"" + paramAsNode.get ("binding").asText () + "\">"
                + paramAsNode.get ("binding").asText () + "</label><div class=\"col-lg-10\"><input id=\"link-" + trueLink.get ("rel").asText () + "-"
                + paramAsNode.get ("binding").asText () + "\" type=\"text\" name=\"" + paramAsNode.get ("binding").asText () + "\" type=\"submit\" placeholder=\""
                + paramAsNode.get ("type").asText () + "\"/></div></div>").getBytes ());
    }

    private void writeLinks (final ArrayNode links, final OutputStream stream) throws IOException {
        if (links.size () == 0) {
            return;
        }
        stream.write ("<h2>Links</h2><ul>".getBytes ());
        for (final Node link : links) {
            @SuppressWarnings ("unchecked")
            final ComplexObjectNode<PrimitiveNode> trueLink = (ComplexObjectNode<PrimitiveNode>) link;
            if ( ((ArrayNode) trueLink.get ("methods")).contains (new PrimitiveNode ("GET"))) {
                this.writeSimpleGetLink (trueLink, stream);
            } else if (! ((ArrayNode) trueLink.get ("methods")).isEmpty ()) {
                this.writeForm (trueLink, stream);
            }
        }
        stream.write ("</ul>".getBytes ());
    }

    private void writeSimpleGetLink (final ComplexObjectNode<PrimitiveNode> trueLink, final OutputStream stream) throws IOException {
        stream.write ( ("<li id=\"" + trueLink.get ("rel").asText () + "\">" + trueLink.get ("rel").asText () + " : <a href=\"" + trueLink.get ("href").asText ()
                + "\" data-model=\"" + trueLink.get ("href").asText () + "\" id=\"link-" + trueLink.get ("rel").asText () + "\" title=\"" + trueLink.get ("rel").asText () + "\">"
                + this.displayReplaceableParams (trueLink.get ("rel").asText (), trueLink.get ("href").asText ()) + "</a></li>").getBytes ());
    }

    private void writeSubmissionScript (final ComplexObjectNode<PrimitiveNode> trueLink, final OutputStream stream) throws IOException {
        stream.write ( ("<script type=\"text/javascript\">" + "$(document).ready(function() {$('#link-" + trueLink.get ("rel").asText () + "').on('submit', function(e) {"
                + "e.preventDefault();" + "var $this = $(this);" + "console.log ($('#link-" + trueLink.get ("rel").asText () + "-unknownParameters'));" + "$.ajax({"
                + "url: $this.attr('action')," + "type: $this.attr('method')," + "data: $('#link-" + trueLink.get ("rel").asText () + "-unknownParameters').length > 0 ? $('#link-"
                + trueLink.get ("rel").asText () + "-unknownParameters').val () : $this.serialize()," + "dataType: \"html\"," + "success: function(html) {"
                + "    var body = html.substring (html.indexOf ('<body>'));" + "    body = body.substring (0, html.indexOf ('</body>'));"
                + "    $('body').html ($(body).wrapAll('<div>').parent().html());" + "    $('#goBack').attr ('href', 'javascript:history.go (0)');" + "}" + "});});});</script>")
                        .getBytes ());
    }
}