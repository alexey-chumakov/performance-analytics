package com.ghx.hackaton.analytcis.agent.logger.base;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by achumakov on 6/15/2015.
 */
public class ExecutionInfo {

    private AtomicInteger executionCount = new AtomicInteger(0);

    private AtomicLong executionTimeTotal = new AtomicLong(0);

    private Map<String, ExecutionInfo> details;

    public void increaseExecutionTimes(long duration) {
        executionCount.incrementAndGet();
        executionTimeTotal.addAndGet(duration);
    }

    public Integer getExecutionCount() {
        return executionCount.get();
    }

    public Long getExecutionTimeTotal() {
        return executionTimeTotal.get();
    }

    public void add(ExecutionInfo info) {
        if (info != null) {
            executionCount.addAndGet(info.getExecutionCount());
            executionTimeTotal.addAndGet(info.getExecutionTimeTotal());
        }
    }

    public void addDetails(Map<String, ExecutionInfo> newDetails) {
        if (details == null) {
            // When adding first details
            details = newDetails;
        } else {
            // When adding more details to already existing
            for (String key : newDetails.keySet()) {
                ExecutionInfo executionInfo = details.get(key);
                ExecutionInfo newExecutionInfo = newDetails.get(key);
                if (executionInfo == null) {
                    details.put(key, newExecutionInfo);
                } else {
                    executionInfo.add(newExecutionInfo);
                }
            }
        }
    }

    public Map<String, ExecutionInfo> getDetails() {
        return details;
    }
}
