package com.ghx.hackaton.analytcis.agent.logger;

import com.ghx.hackaton.analytcis.agent.logger.base.ExecutionInfo;
import com.ghx.hackaton.analytcis.agent.logger.base.SimpleExecutionInfoHolder;
import com.ghx.hackaton.analytcis.agent.logger.base.ThreadLocalExecutionInfoHolder;
import com.ghx.hackaton.analytcis.agent.commons.ExternalSystemType;
import com.ghx.hackaton.analytcis.agent.sender.AvroSenderImpl;
import com.ghx.hackaton.analytcis.agent.sender.Sender;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by achumakov on 6/15/2015.
 */
public class RequestLogger {

    private static RequestLogger requestLogger;

    private static long evictionTime = 30L;

    private SimpleExecutionInfoHolder requestStats = new SimpleExecutionInfoHolder();

    private ThreadLocalExecutionInfoHolder threadLocalStats = new ThreadLocalExecutionInfoHolder();

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    public RequestLogger() {
        executorService.scheduleAtFixedRate(new EventEvictionThread(), evictionTime, evictionTime, TimeUnit.SECONDS);
    }

    public static void setEvictionTime(long time) {
        evictionTime = time;
    }

    public static synchronized RequestLogger getInstance() {
        if (requestLogger == null) {
            requestLogger = new RequestLogger();
        }
        return requestLogger;
    }

    public void logRequestCompleted(String url, Long processingTime) {
        try {
            ExecutionInfo info = getExecutionInfo(url);
            info.increaseExecutionTimes(processingTime);
            HashMap<String, ExecutionInfo> requestDetails = threadLocalStats.getAndCleanup();
            info.addDetails(requestDetails);
        } catch (Exception e) {
            // Avoid any interruption in request processing
            e.printStackTrace();
        }
    }

    public void logExternalSystemUsage(ExternalSystemType type, long processingTime) {
        try {
            ExecutionInfo info = threadLocalStats.getExecutionInfoForKey(type.name());
            info.increaseExecutionTimes(processingTime);
        } catch (Exception e) {
            // Avoid any interruption in request processing
            e.printStackTrace();
        }
    }

    /**
     * Groups all JS/Image requests.
     *
     * @param url
     * @return
     */
    private ExecutionInfo getExecutionInfo(String url) {
        if (url.endsWith(".js")) {
            return requestStats.getExecutionInfoForKey("JS");
        } else if (url.endsWith(".gif") || url.endsWith(".png") || url.endsWith(".jpg") || url.endsWith(".ico")) {
            return requestStats.getExecutionInfoForKey("Image");
        }
        return requestStats.getExecutionInfoForKey(url);
    }

    protected class EventEvictionThread implements Runnable {

        private Sender sender;

        public void run() {
            try {
                sender = new AvroSenderImpl();
                evictEvents();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected void evictEvents() {
            sender.send(requestStats.getAndCleanup());
        }
    }

}
