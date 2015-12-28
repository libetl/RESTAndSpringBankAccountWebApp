package org.toilelibre.libe.bank.actions;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.toilelibre.libe.bank.actions.entity.Node;
import org.toilelibre.libe.bank.actions.entity.NodeFactory;
import org.toilelibre.libe.bank.actions.entity.ObjectNode;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class RootResource {
    private static Logger LOGGER = LoggerFactory.getLogger (RootResource.class);
                                 
    @Inject
    private LinkHelper    linkHelper;
                          
    @RequestMapping (value = "/", method = RequestMethod.GET)
    public Response<ObjectNode<Node>> showAPIs (final HttpServletRequest request) throws JsonProcessingException {
        RootResource.LOGGER.info ("Showing Root Resource");
        final NodeFactory factory = NodeFactory.instance;
        return new Response<ObjectNode<Node>> (this.linkHelper.get (), this.linkHelper.surroundWithLinks (factory.objectNode ().put ("title", "root")));
    }
    
}
