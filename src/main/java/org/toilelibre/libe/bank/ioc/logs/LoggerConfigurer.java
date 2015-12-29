package org.toilelibre.libe.bank.ioc.logs;

import java.util.Arrays;
import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.NopStatusListener;
import ch.qos.logback.core.status.StatusManager;

public class LoggerConfigurer {
    
    enum LevelAsEnum {
        TRACE, DEBUG, INFO, WARN, ERROR;
    }
    
    public void configure (final LoggerContext context) {
        this.disableLogbackInitLogs (context);
        this.setRootLevelOff (context);
        this.setSpringLevelOff (context);
        final Appender<ILoggingEvent> normalAppender = this.configureNormalAppender (context);
        final Appender<ILoggingEvent> problemAppender = this.configureProblemAppender (context);
        this.addAppenderToWebApp (context, normalAppender, problemAppender);
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
    
    private Appender<ILoggingEvent> configureNormalAppender (final LoggerContext context) {
        final ConsoleAppender<ILoggingEvent> result = new ConsoleAppender<ILoggingEvent> ();
        result.setContext (context);
        result.setName ("problem");
        
        this.addLevelRangeFilter (result, context, LevelAsEnum.WARN, LevelAsEnum.ERROR);
        this.setLayout (result, context, "%d{HH:mm:ss} %.-1level [%X{requestVerb} %X{requestPath}] - %msg%n %throwable{short}%n");

        result.start ();
        return result;
    }
    
    private Appender<ILoggingEvent> configureProblemAppender (final LoggerContext context) {
        final ConsoleAppender<ILoggingEvent> result = new ConsoleAppender<ILoggingEvent> ();
        result.setContext (context);
        result.setName ("normal");
        
        this.addLevelRangeFilter (result, context, LevelAsEnum.TRACE, LevelAsEnum.INFO);
        this.setLayout (result, context, "%d{HH:mm:ss} %.-1level [%X{requestVerb} %X{requestPath}] - %msg%n");
        result.start ();
        return result;
    }
    
    private void addAppenderToWebApp (final LoggerContext context, final Appender<ILoggingEvent> normalAppender, final Appender<ILoggingEvent> problemAppender) {
        final Logger webAppLogger = context.getLogger ("org.toilelibre.libe.bank");
        webAppLogger.addAppender (normalAppender);
        webAppLogger.addAppender (problemAppender);
        webAppLogger.setLevel (Level.DEBUG);
    }
    
    private void setLayout (final ConsoleAppender<ILoggingEvent> result, final LoggerContext context, final String layout) {
        final PatternLayout patternLayout = new PatternLayout ();
        patternLayout.setContext (context);
        patternLayout.setPattern (layout);
        patternLayout.start ();
        result.setLayout (patternLayout);
    }
    
    private void addLevelRangeFilter (final Appender<ILoggingEvent> appender, final LoggerContext context, final LevelAsEnum low, final LevelAsEnum high) {
        final List<LevelAsEnum> globalList = Arrays.asList (LevelAsEnum.values ());
        
        for (int i = 0 ; i < globalList.indexOf (low) ; i++) {
            this.addFilterReplyToLevel (appender, context, Level.valueOf (globalList.get (i).name ()), FilterReply.DENY);
        }
        
        for (int i = globalList.indexOf (low) ; i <= globalList.indexOf (high) ; i++) {
            this.addFilterReplyToLevel (appender, context, Level.valueOf (globalList.get (i).name ()), FilterReply.ACCEPT);
        }
        
        for (int i = globalList.indexOf (high) + 1 ; i < globalList.size () ; i++) {
            this.addFilterReplyToLevel (appender, context, Level.valueOf (globalList.get (i).name ()), FilterReply.DENY);
        }
    }
    
    private void addFilterReplyToLevel (final Appender<ILoggingEvent> appender, final LoggerContext context, final Level level, final FilterReply reply) {
        final LevelFilter newFilter = new LevelFilter ();
        newFilter.setContext (context);
        newFilter.setLevel (level);
        newFilter.setOnMatch (reply);
        newFilter.start ();
        appender.addFilter (newFilter);
    }
    
}
