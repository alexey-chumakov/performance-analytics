package com.ghx.hackaton.analytcis.agent.base;

import java.util.HashMap;

/**
 * Created by achumakov on 6/23/2015.
 */
public class SimpleExecutionInfoHolder<T extends Object> {

    private HashMap<T, ExecutionInfo> executionInfoMap = new HashMap<T, ExecutionInfo>();

    public ExecutionInfo getExecutionInfoForKey(T key) {
        ExecutionInfo info = executionInfoMap.get(key);
        if (info == null) {
            info = new ExecutionInfo();
            executionInfoMap.put(key, info);
        }
        return info;
    }

    public HashMap<T, ExecutionInfo> getAndCleanup() {
        HashMap<T, ExecutionInfo> result = executionInfoMap;
        executionInfoMap = new HashMap<T, ExecutionInfo>();
        return result;
    }
}
