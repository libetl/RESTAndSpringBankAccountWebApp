package org.toilelibre.libe.bank.ioc.webapp.html;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.toilelibre.libe.bank.actions.entity.NodeFactory;

public class WwwFormDataMessageConverter implements GenericHttpMessageConverter<Object> {

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
        return false;
    }

    @Override
    public boolean canWrite (final Type type, final Class<?> clazz, final MediaType mediaType) {
        return this.canWrite (clazz, mediaType);

    }

    private void ensureParamCorrectlyFormed (final String [] fields) {
        if (fields.length < 2) {
            throw new IllegalArgumentException (String.format ("Malformed request, expected a form param 'a=b', got '%s'", fields [0]));
        }
    }

    @Override
    public List<MediaType> getSupportedMediaTypes () {
        return Collections.singletonList (MediaType.APPLICATION_FORM_URLENCODED);
    }

    @Override
    public Object read (final Class<? extends Object> clazz, final HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        final Scanner scanner = new Scanner (inputMessage.getBody ());
        final Map<String, String> map = new LinkedHashMap<String, String> ();
        final String body = scanner.useDelimiter ("\\A").next ();
        scanner.close ();
        final String [] pairs = body.split ("\\&");
        for (final String pair : pairs) {
            final String [] fields = pair.split ("=");
            this.ensureParamCorrectlyFormed (fields);
            final String key = URLDecoder.decode (fields [0], "UTF-8");
            final String value = URLDecoder.decode (fields [1], "UTF-8");

            map.put (key, value);
        }
        return NodeFactory.instance.pojoNode (map);
    }

    @Override
    public Object read (final Type type, final Class<?> contextClass, final HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return this.read (contextClass, inputMessage);
    }

    @Override
    public void write (final Object t, final MediaType contentType, final HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        this.write (t, null, contentType, outputMessage);

    }

    @Override
    public void write (final Object t, final Type type, final MediaType contentType, final HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
    }

}