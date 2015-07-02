package com.ghx.hackaton.analytics.web;

import com.ghx.hackaton.analytcis.Statistics;
import com.ghx.hackaton.analytics.server.StatisticsReceiver;
import com.ghx.hackaton.analytics.spring.ComponentConfig;
import com.ghx.hackaton.analytics.spring.HibernateConfig;
import com.ghx.hackaton.analytics.spring.WebConfig;
import org.apache.avro.ipc.ResponderServlet;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.io.IOException;

public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {

        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(ComponentConfig.class, HibernateConfig.class);

        container.addListener(new ContextLoaderListener(rootContext));

        AnnotationConfigWebApplicationContext dispatcherServletContext = new AnnotationConfigWebApplicationContext();
        dispatcherServletContext.register(WebConfig.class);

        ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", new DispatcherServlet(dispatcherServletContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        try {
            ServletRegistration.Dynamic avroResponder = container.addServlet("avroResponder", new ResponderServlet(new SpecificResponder(Statistics.class, new StatisticsReceiver())));
            avroResponder.setLoadOnStartup(1);
            avroResponder.addMapping("/avro/*");
        } catch (IOException e) {
            System.err.println("Failed to initialize avro responder servlet. " + e.toString());
        }
    }
}
