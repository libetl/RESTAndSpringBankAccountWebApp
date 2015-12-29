package org.toilelibre.libe.bank.ioc.logs;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.impl.StaticLoggerBinder;

import ch.qos.logback.classic.LoggerContext;

public class LogbackConfigListener implements ServletContextListener {

    private boolean started;

    public LogbackConfigListener () {
        this.started = false;
    }

    @Override
    public void contextDestroyed (final ServletContextEvent sce) {

    }

    @Override
    public void contextInitialized (final ServletContextEvent sce) {

        if (this.started) {
            return;
        }

        this.started = true;
        final LoggerContext context = (LoggerContext) StaticLoggerBinder.getSingleton ().getLoggerFactory ();
        final LoggerConfigurer configurer = new LoggerConfigurer ();
        configurer.configure (context);
    }

}
