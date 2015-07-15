package com.ghx.hackaton.analytics.web.bean;

import com.ghx.hackaton.analytics.model.dto.RequestDuration;

import java.util.List;
import java.util.Map;

public class RequestDurationReportBean {

    private Double totalDuration;
    private List<RequestDuration> totalRequestDurations;

    // list of request durations represented as key-value properties e.g. {timestamp -> 1000000, systemName1 -> 10, systemName2 -> 20}
    private List<Map<String, Object>> dailyDurations;

    public Double getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Double totalDuration) {
        this.totalDuration = totalDuration;
    }

    public List<RequestDuration> getTotalRequestDurations() {
        return totalRequestDurations;
    }

    public void setTotalRequestDurations(List<RequestDuration> totalRequestDurations) {
        this.totalRequestDurations = totalRequestDurations;
    }

    public List<Map<String, Object>> getDailyDurations() {
        return dailyDurations;
    }

    public void setDailyDurations(List<Map<String, Object>> dailyDurations) {
        this.dailyDurations = dailyDurations;
    }
}
