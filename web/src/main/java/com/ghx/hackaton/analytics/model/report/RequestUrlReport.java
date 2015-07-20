package com.ghx.hackaton.analytics.model.report;

import com.ghx.hackaton.analytics.model.Request;

import java.util.List;

public class RequestUrlReport {
    private String url;
    private Long totalCount;
    private Double avgDuration;
    private List<Request> dailyRequests;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Double getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(Double avgDuration) {
        this.avgDuration = avgDuration;
    }

    public List<Request> getDailyRequests() {
        return dailyRequests;
    }

    public void setDailyRequests(List<Request> dailyRequests) {
        this.dailyRequests = dailyRequests;
    }
}
