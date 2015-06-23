package com.ghx.hackaton.analytcis.agent.base;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by achumakov on 6/15/2015.
 */
public class ExecutionInfo {

    private AtomicInteger executionCount = new AtomicInteger(0);

    private AtomicLong executionTimeTotal = new AtomicLong(0);

    public void increaseExecutionTimes(long duration) {
        executionCount.incrementAndGet();
        executionTimeTotal.addAndGet(duration);
    }

    public AtomicInteger getExecutionCount() {
        return executionCount;
    }

    public AtomicLong getExecutionTimeTotal() {
        return executionTimeTotal;
    }
}
