package com.ghx.hackaton.analytics.model.report;

import com.ghx.hackaton.analytics.model.dto.RequestDuration;

import java.util.List;

public class RequestUrlReport {
    private String url;
    private List<RequestDuration> dailyDurations;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<RequestDuration> getDailyDurations() {
        return dailyDurations;
    }

    public void setDailyDurations(List<RequestDuration> dailyDurations) {
        this.dailyDurations = dailyDurations;
    }
}
