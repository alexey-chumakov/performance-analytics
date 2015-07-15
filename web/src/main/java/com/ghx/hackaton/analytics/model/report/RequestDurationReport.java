package com.ghx.hackaton.analytics.model.report;

import com.ghx.hackaton.analytics.model.dto.RequestDuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestDurationReport {

    private Double totalDuration;
    private List<RequestDuration> totalRequestDurations;
    private Map<Long, Map<String, Double>> dailyDurations;

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

    public Map<Long, Map<String, Double>> getDailyDurations() {
        return dailyDurations;
    }

    public void setDailyDurations(Map<Long, Map<String, Double>> dailyDurations) {
        this.dailyDurations = dailyDurations;
    }
}
