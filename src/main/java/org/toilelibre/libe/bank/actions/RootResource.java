package org.toilelibre.libe.bank.actions;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class RootResource {
    private static Logger LOGGER = LoggerFactory.getLogger (RootResource.class);
                                 
    @Inject
    private LinkHelper    linkHelper;
                          
    @RequestMapping (value = "/", method = RequestMethod.GET)
    public Response<ObjectNode> showAPIs (final HttpServletRequest request) throws JsonProcessingException {
        RootResource.LOGGER.info ("Showing Root Resource");
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        return new Response<ObjectNode> (this.linkHelper.get (), this.linkHelper.surroundWithLinks (factory.objectNode ().put ("title", "root")));
    }
    
}
