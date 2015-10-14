package org.toilelibre.libe.bank.testutils;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class SmartLogRule implements TestRule {

    static class LoggingStatement extends Statement {

        private final Description description;
        private final Statement   base;

        public LoggingStatement (final Statement base1, final Description description1) {
            this.base = base1;
            this.description = description1;
        }

        @Override
        public void evaluate () throws Throwable {
            final long start = System.currentTimeMillis ();
            MDC.put (SmartLogRule.TEST_CLASS, this.description.getTestClass ().getSimpleName ());
            MDC.put (SmartLogRule.TEST_METHOD, this.description.getMethodName ());
            SmartLogRule.LOGGER.debug (SmartLogRule.TEST_BEGIN);
            try {
                this.base.evaluate ();
            } catch (final Throwable e) {
                SmartLogRule.LOGGER.error (SmartLogRule.TEST_ERROR + " : Error in the current test", e);
                throw e;
            }
            final long end = System.currentTimeMillis ();
            SmartLogRule.LOGGER.debug (SmartLogRule.TEST_END + ", Duration : " + (end - start) + "ms");
            MDC.remove (SmartLogRule.TEST_CLASS);
            MDC.remove (SmartLogRule.TEST_METHOD);
        }

    }

    private static final String TEST_CLASS  = "TestClass";
    private static final String TEST_METHOD = "TestMethod";
    private static final String TEST_ERROR  = "/!\\TestError/!\\";
    private static final String TEST_BEGIN  = "Testing...";
    private static final String TEST_END    = "End of test (OK)";

    private static final Logger LOGGER      = LoggerFactory.getLogger (SmartLogRule.class);

    @Override
    public Statement apply (final Statement base, final Description description) {
        return new LoggingStatement (base, description);
    }
}
