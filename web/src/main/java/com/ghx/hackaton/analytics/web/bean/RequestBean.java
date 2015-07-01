package com.ghx.hackaton.analytics.web.bean;

import java.util.List;

public class RequestBean {

    private long timestamp;
    private String appName;
    private String serverId;
    private String url;
    private int count;
    private int duration;
    private List<RequestDetailsBean> details;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<RequestDetailsBean> getDetails() {
        return details;
    }

    public void setDetails(List<RequestDetailsBean> details) {
        this.details = details;
    }
}
