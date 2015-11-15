package org.toilelibre.libe.bank.testutils;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.impl.StaticLoggerBinder;

import ch.qos.logback.classic.LoggerContext;

public class LogbackConfigRule implements TestRule {
    
    private static boolean started = false;
    
    public LogbackConfigRule () {
    }
    
    @Override
    public Statement apply (final Statement base, final Description description) {
        
        if (LogbackConfigRule.started) {
            return base;
        }
        
        LogbackConfigRule.started = true;
        final LoggerContext context = (LoggerContext) StaticLoggerBinder.getSingleton ().getLoggerFactory ();
        new TestLoggerConfigurer ().configure (context);
        return base;
    }
    
}
