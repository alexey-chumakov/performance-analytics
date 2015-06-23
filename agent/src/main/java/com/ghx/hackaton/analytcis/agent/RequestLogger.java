package com.ghx.hackaton.analytcis.agent;

import com.ghx.hackaton.analytcis.agent.base.ExecutionInfo;
import com.ghx.hackaton.analytcis.agent.base.SimpleExecutionInfoHolder;
import com.ghx.hackaton.analytcis.agent.base.ThreadLocalExecutionInfoHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by achumakov on 6/15/2015.
 */
public class RequestLogger {

    private static RequestLogger requestLogger = new RequestLogger();

    private SimpleExecutionInfoHolder<String> requestStats = new SimpleExecutionInfoHolder<String>();

    private ThreadLocalExecutionInfoHolder<ExternalSystemType> externalCallStats = new ThreadLocalExecutionInfoHolder<ExternalSystemType>();

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    public RequestLogger() {
        executorService.scheduleAtFixedRate(new EventEvictionThread(), 15, 15, TimeUnit.SECONDS);
    }

    public static RequestLogger getInstance() {
        return requestLogger;
    }

    public void logRequest(String url, Long processingTime) {
        try {
            ExecutionInfo info = getExecutionInfo(url);
            info.increaseExecutionTimes(processingTime);
        } catch (Exception e) {
            // Avoid any interruption in request processing
            e.printStackTrace();
        }
    }

    public void logExternalSystemUsage(ExternalSystemType type, Long processingTime) {
        try {
            ExecutionInfo info = externalCallStats.getExecutionInfoForKey(type);
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

    private class EventEvictionThread implements Runnable {

        public void run() {
            try {
                evictEvents();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void evictEvents() {
            HashMap<String, ExecutionInfo> executionInfo = requestStats.getAndCleanup();
            for (Map.Entry<String, ExecutionInfo> entry : executionInfo.entrySet()) {
                String url = entry.getKey();
                int executionCount = entry.getValue().getExecutionCount().get();
                long executionTimeTotal = entry.getValue().getExecutionTimeTotal().get();
                float processingTime = executionTimeTotal / executionCount;
                // FIXME send data instead of printing it
                System.out.println("URL: " + url + " : " + processingTime + "ms, " + executionCount + " times executed");
            }
        }

    }

}
