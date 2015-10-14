package org.toilelibre.libe.bank.actions;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class RootResource {
    private static Logger                LOGGER = LoggerFactory.getLogger (RootResource.class);

    @Inject
    private LinkHelper                   linkHelper;

    @RequestMapping (value = "/", method = RequestMethod.GET)
    public Response<List<Link>> showAPIs (final HttpServletRequest request) throws JsonProcessingException {
        RootResource.LOGGER.info ("Showing Root Resource");
        final List<Link> links = linkHelper.getLinks (request.getRequestURL ().toString ());
        return new Response<List<Link>> (this.linkHelper.get (), links);
    }

}
