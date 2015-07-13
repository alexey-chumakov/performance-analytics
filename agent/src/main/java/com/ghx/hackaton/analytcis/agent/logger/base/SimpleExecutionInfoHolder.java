package com.ghx.hackaton.analytcis.agent.logger.base;

import java.util.HashMap;

/**
 * Created by achumakov on 6/23/2015.
 */
public class SimpleExecutionInfoHolder {

    private HashMap<String, ExecutionInfo> executionInfoMap = new HashMap<String, ExecutionInfo>();

    public ExecutionInfo getExecutionInfoForKey(String key, String contextPath) {
        ExecutionInfo info = executionInfoMap.get(key);
        if (info == null) {
            info = new ExecutionInfo(contextPath);
            executionInfoMap.put(key, info);
        }
        if (info.getContextPath() == null) {
            info.setContextPath(contextPath);
        }
        return info;
    }

    public ExecutionInfo getExecutionInfoForKey(String key) {
        ExecutionInfo info = executionInfoMap.get(key);
        if (info == null) {
            info = new ExecutionInfo();
            executionInfoMap.put(key, info);
        }
        return info;
    }

    public HashMap<String, ExecutionInfo> getAndCleanup() {
        HashMap<String, ExecutionInfo> result = executionInfoMap;
        executionInfoMap = new HashMap<String, ExecutionInfo>();
        return result;
    }
}
