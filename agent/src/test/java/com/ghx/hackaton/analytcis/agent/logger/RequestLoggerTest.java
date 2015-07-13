package com.ghx.hackaton.analytcis.agent.logger;

import com.ghx.hackaton.analytcis.agent.commons.ExternalSystemType;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class RequestLoggerTest extends TestCase {

    private RequestLogger logger;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RequestLoggerTest(String testName) {
        super(testName);
        RequestLogger.setEvictionTime(1);
        logger = RequestLogger.getInstance();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(RequestLoggerTest.class);
    }

    /**
     *
     */
    public void testLogRequest() {
        logger.logRequestCompleted("testPath" ,"test URL", 100L);
        sleep();
    }

    /**
     *
     */
    public void testLogExternalSystemUsageSimple() {
        RequestLogger.setEvictionTime(1);
        RequestLogger logger = RequestLogger.getInstance();
        logger.logExternalSystemUsage(ExternalSystemType.MONGO_DB, 50L);
        logger.logRequestCompleted("testPath" ,"test URL with Mongo", 100L);
        logger.logRequestCompleted("testPath" ,"test URL without Mongo", 50L);
        sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
