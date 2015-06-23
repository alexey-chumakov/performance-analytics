package com.ghx.hackaton.analytcis.agent.base;

import com.ghx.hackaton.analytcis.agent.base.ExecutionInfo;
import com.ghx.hackaton.analytcis.agent.base.SimpleExecutionInfoHolder;

import java.util.HashMap;

/**
 * Created by achumakov on 6/23/2015.
 */
public class ThreadLocalExecutionInfoHolder<T> {

    private static ThreadLocal<SimpleExecutionInfoHolder> holder = new ThreadLocal<SimpleExecutionInfoHolder>();

    public ExecutionInfo getExecutionInfoForKey(T key) {
        SimpleExecutionInfoHolder externalCallStats = holder.get();
        if (externalCallStats == null) {
            externalCallStats = new SimpleExecutionInfoHolder();
            holder.set(externalCallStats);
        }
        return externalCallStats.getExecutionInfoForKey(key);
    }

    public HashMap<T, ExecutionInfo> getAndCleanup() {
        SimpleExecutionInfoHolder externalCallStats = holder.get();
        if (externalCallStats == null) {
            return null;
        }
        HashMap<T, ExecutionInfo> result = externalCallStats.getAndCleanup();
        return result;
    }

}
