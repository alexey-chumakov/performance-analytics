package com.ghx.hackaton.analytics.web.bean;

import com.ghx.hackaton.analytics.model.dto.RequestDuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestDurationReportBean {

    private String appName;
    private String url;
    private Double totalDuration;
    private List<RequestDuration> totalRequestDurations;

    // list of request durations represented as key-value properties e.g. {timestamp -> 1000000, systemName1 -> 10, systemName2 -> 20}
    private List<Map<String, Object>> dailyDurations;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Double totalDuration) {
        this.totalDuration = totalDuration;
    }

    public List<RequestDuration> getTotalRequestDurations() {
        if (totalRequestDurations == null) {
            totalRequestDurations = new ArrayList<RequestDuration>();
        }
        return totalRequestDurations;
    }

    public void setTotalRequestDurations(List<RequestDuration> totalRequestDurations) {
        this.totalRequestDurations = totalRequestDurations;
    }

    public List<Map<String, Object>> getDailyDurations() {
        if (dailyDurations == null) {
            dailyDurations = new ArrayList<Map<String, Object>>();
        }
        return dailyDurations;
    }

    public void setDailyDurations(List<Map<String, Object>> dailyDurations) {
        this.dailyDurations = dailyDurations;
    }
}
