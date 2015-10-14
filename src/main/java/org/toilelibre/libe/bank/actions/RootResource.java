package org.toilelibre.libe.bank.actions;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class RootResource {
    private static Logger                LOGGER = LoggerFactory.getLogger (RootResource.class);

    @Inject
    private RequestMappingHandlerMapping handlerMapping;
    @Inject
    private LinkHelper                   linkHelper;

    @RequestMapping (value = "/", method = RequestMethod.GET)
    public Response<List<Link>> showAPIs (final HttpServletRequest request) throws JsonProcessingException {
        RootResource.LOGGER.info ("Showing Root Resource");
        final List<Link> links = this.getLinks (request.getRequestURL ().toString ());
        return new Response<List<Link>> (this.linkHelper.get (), links);
    }

    private List<Link> getLinks (final String requestURL) {
        final List<Link> result = new LinkedList<Link> ();
        for (final Entry<RequestMappingInfo, HandlerMethod> entry : this.handlerMapping.getHandlerMethods ().entrySet ()) {
            result.add (new Link (this.linkHelper.methodToFriendlyName (entry.getValue ().getMethod ()), requestURL + entry.getKey ().getPatternsCondition ().getPatterns ().iterator ().next ().replaceFirst ("^/", "")));
        }
        return result;
    }
}
