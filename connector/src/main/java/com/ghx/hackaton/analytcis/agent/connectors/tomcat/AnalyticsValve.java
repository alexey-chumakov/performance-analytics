package com.ghx.hackaton.analytcis.agent.connectors.tomcat;

import com.ghx.hackaton.analytcis.agent.logger.RequestLogger;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 *
 * Should be registered in server.xml as <Valve className="com.ghx.hackaton.analytcis.agent.connectors.tomcat.AnalyticsValve" /> in <Engine> section
 * Created by achumakov on 6/15/2015.
 */
public class AnalyticsValve extends ValveBase {

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        getNext().invoke(request, response);
        long end = System.currentTimeMillis();
        RequestLogger.getInstance().setAppName(request.getContextPath());
        RequestLogger.getInstance().logRequestCompleted(request.getRequestURL().toString(), end - start);
    }
}
