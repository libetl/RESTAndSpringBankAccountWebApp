package org.toilelibre.libe.bank.ioc.webapp;

import java.util.Arrays;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class ServerMain {
    public static void main (String [] args) throws Exception {
        Logger logbackLogger = (Logger) LoggerFactory.getLogger ("org.eclipse.jetty");
        logbackLogger.setLevel (Level.WARN);
        Server server = new Server (8080);

        WebAppContext webappContext = new WebAppContext ();
        webappContext.setContextPath ("/");
        webappContext.getServletContext ().setExtendedListenerTypes (true);
        webappContext.setConfigurationClasses (Arrays.asList (WebAppInitializer.class.getName ()));

        server.setHandler (webappContext);
        server.start ();
        server.join ();
    }
}
