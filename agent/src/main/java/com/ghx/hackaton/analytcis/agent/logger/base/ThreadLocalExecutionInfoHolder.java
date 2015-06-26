package com.ghx.hackaton.analytcis.agent.logger.base;

import java.util.HashMap;

/**
 * Created by achumakov on 6/23/2015.
 */
public class ThreadLocalExecutionInfoHolder {

    private static ThreadLocal<SimpleExecutionInfoHolder> holder = new ThreadLocal<SimpleExecutionInfoHolder>();

    public ExecutionInfo getExecutionInfoForKey(String key) {
        SimpleExecutionInfoHolder externalCallStats = holder.get();
        if (externalCallStats == null) {
            externalCallStats = new SimpleExecutionInfoHolder();
            holder.set(externalCallStats);
        }
        return externalCallStats.getExecutionInfoForKey(key);
    }

    public HashMap<String, ExecutionInfo> getAndCleanup() {
        SimpleExecutionInfoHolder externalCallStats = holder.get();
        if (externalCallStats == null) {
            return null;
        }
        HashMap<String, ExecutionInfo> result = externalCallStats.getAndCleanup();
        return result;
    }

}
