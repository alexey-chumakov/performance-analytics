package com.ghx.hackaton.analytcis.agent.logger;

import com.ghx.hackaton.analytcis.agent.logger.base.ExecutionInfo;
import com.ghx.hackaton.analytcis.agent.logger.base.SimpleExecutionInfoHolder;
import com.ghx.hackaton.analytcis.agent.logger.base.ThreadLocalExecutionInfoHolder;
import com.ghx.hackaton.analytcis.agent.commons.ExternalSystemType;

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

    private SimpleExecutionInfoHolder requestStats = new SimpleExecutionInfoHolder();

    private ThreadLocalExecutionInfoHolder threadLocalStats = new ThreadLocalExecutionInfoHolder();

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    public RequestLogger() {
        executorService.scheduleAtFixedRate(new EventEvictionThread(), 30, 30, TimeUnit.SECONDS);
    }

    public static RequestLogger getInstance() {
        return requestLogger;
    }

    public void logRequest(String url, Long processingTime) {
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

    public void logExternalSystemUsage(ExternalSystemType type, Long processingTime) {
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

    private class EventEvictionThread implements Runnable {

        public void run() {
            try {
                evictEvents();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void evictEvents() {
            HashMap<String, ExecutionInfo> loggedRequests = requestStats.getAndCleanup();
            for (Map.Entry<String, ExecutionInfo> entry : loggedRequests.entrySet()) {
                String url = entry.getKey();
                ExecutionInfo executionInfo = entry.getValue();
                int executionCount = executionInfo.getExecutionCount();
                long executionTimeTotal = executionInfo.getExecutionTimeTotal();
                float processingTime = executionTimeTotal / executionCount;

                // FIXME send data instead of printing it
                boolean hasSubCalls = false;
                StringBuilder subCalls = new StringBuilder();
                subCalls.append("[");
                for (Map.Entry<String, ExecutionInfo> detail : executionInfo.getDetails().entrySet()) {
                    subCalls.append(detail.getKey());
                    subCalls.append(": ");
                    subCalls.append(detail.getValue().getExecutionCount());
                    hasSubCalls = true;
                }
                if (hasSubCalls) {
                    subCalls.setLength(subCalls.length() - 2);
                }
                subCalls.append("]");
                String subCallsString = hasSubCalls ? subCalls.toString() : "";

                System.out.println("URL: " + url + " : " + processingTime + "ms, " + executionCount + " times executed " + subCallsString);
            }
        }

    }

}
