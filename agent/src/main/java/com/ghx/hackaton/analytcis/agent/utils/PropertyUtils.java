package com.ghx.hackaton.analytcis.agent.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by achumakov on 7/7/2015.
 */
public class PropertyUtils {

    private static String PROPERTIES_FILE_NAME = "analytics-agent.properties";

    private static Properties properties = new Properties();

    static {
        InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        try {
            properties.load(in);
        } catch (IOException e) {
            // No property file, that's ok
            System.out.println(String.format("Can't find %s property file. Analytics agent won't be able to send collected data to server", PROPERTIES_FILE_NAME));
        }
    }

    public static String getServerURL() {
        return properties.get("server.url").toString();
    }

}
