package org.toilelibre.libe.bank.ioc.webapp.yaml;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.toilelibre.libe.bank.actions.entity.NodeFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.DumperOptions;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;

public class YamlMessageConverter implements GenericHttpMessageConverter<Object> {
    public static final String           APPLICATION_YAML = "application/yaml";
    private static final List<MediaType> YAML_ONLY        = Collections.singletonList (MediaType.parseMediaType (YamlMessageConverter.APPLICATION_YAML));;

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
        return YamlMessageConverter.YAML_ONLY;
    }

    @Override
    public Object read (final Class<? extends Object> clazz, final HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return NodeFactory.instance.pojoNode (new Yaml ().load (inputMessage.getBody ()));
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
        outputMessage.getHeaders ().setContentType (this.getSupportedMediaTypes ().get (0));
        @SuppressWarnings ("unchecked")
        final Map<String, Object> jsonObject = new ObjectMapper ().convertValue (t, Map.class);
        final DumperOptions options = new DumperOptions ();
        options.setDefaultFlowStyle (DumperOptions.FlowStyle.BLOCK);
        final Yaml yaml = new Yaml (options);
        final String yamlAsString = yaml.dump (jsonObject);

        outputMessage.getBody ().write (yamlAsString.getBytes ());

    }
}