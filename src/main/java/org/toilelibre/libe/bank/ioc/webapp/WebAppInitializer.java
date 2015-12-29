package org.toilelibre.libe.bank.ioc.webapp;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.toilelibre.libe.bank.ioc.logs.LogbackConfigListener;

/**
 * web.xml in Full JavaConfig
 *
 */
public class WebAppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup (final ServletContext servletContext) throws ServletException {
        final WebApplicationContext context = this.getContext ();
        servletContext.addListener (new LogbackConfigListener ());
        servletContext.addListener (new ContextLoaderListener (context));
        this.setServletOptions (servletContext.addServlet ("DispatcherServlet", new DispatcherServlet (context)));
        servletContext.addFilter (MDCFilter.class.getSimpleName (), MDCFilter.class).addMappingForUrlPatterns (EnumSet.of (DispatcherType.REQUEST, DispatcherType.ERROR), true,
                "/*");
    }
    
    private void setServletOptions (final Dynamic dispatcher) {
        dispatcher.setLoadOnStartup (1);
        dispatcher.setInitParameter ("throwExceptionIfNoHandlerFound", "true");
        dispatcher.addMapping ("/api/*");
    }
    
    private AnnotationConfigWebApplicationContext getContext () {
        final AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext ();
        context.setConfigLocations (WebAppConfig.class.getName (),
                WebAppContentNegociationConfig.class.getName (),
                WebMvcConfigurationSupportWithCustomArgumentResolvers.class.getName ());
        return context;
    }
    
}
