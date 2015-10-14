package org.toilelibre.libe.bank.testutils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.status.NopStatusListener;
import ch.qos.logback.core.status.StatusManager;

public class TestLoggerConfigurer {

    public void configure (final LoggerContext context) {
        this.disableLogbackInitLogs (context);
        this.setRootLevelOff (context);
        this.setSpringLevelOff (context);
        final Appender<ILoggingEvent> stdout = this.configureTestAppender (context);
        this.addAppenderToTests (context, stdout);
    }

    private void setSpringLevelOff (final LoggerContext context) {
        final Logger springLogger = context.getLogger ("org.springframework");
        springLogger.setLevel (Level.OFF);
    }

    private void setRootLevelOff (final LoggerContext context) {
        final Logger rootLogger = context.getLogger (org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.detachAndStopAllAppenders ();
    }

    private void disableLogbackInitLogs (final LoggerContext context) {
        final StatusManager statusManager = context.getStatusManager ();
        statusManager.add (new NopStatusListener ());
    }

    private Appender<ILoggingEvent> configureTestAppender (final LoggerContext context) {
        final ConsoleAppender<ILoggingEvent> result = new ConsoleAppender<ILoggingEvent> ();
        result.setContext (context);
        result.setName ("STDOUT");
        this.setLayout (result, context, "%d{HH:mm:ss} %.-1level %X{TestClass}.%X{TestMethod} - %msg%n");
        result.start ();
        return result;
    }

    private void addAppenderToTests (final LoggerContext context, final Appender<ILoggingEvent> stdout) {
        final Logger webAppLogger = context.getLogger ("org.toilelibre.libe.bank");
        webAppLogger.addAppender (stdout);
        webAppLogger.setLevel (Level.DEBUG);
    }

    private void setLayout (final ConsoleAppender<ILoggingEvent> result, final LoggerContext context, final String layout) {
        final PatternLayout patternLayout = new PatternLayout ();
        patternLayout.setContext (context);
        patternLayout.setPattern (layout);
        patternLayout.start ();
        result.setLayout (patternLayout);
    }
}
