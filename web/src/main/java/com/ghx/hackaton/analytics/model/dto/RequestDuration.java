package com.ghx.hackaton.analytics.model.dto;

import com.ghx.hackaton.analytics.util.DateUtil;

public class RequestDuration {
    private Integer year;
    private Integer month;
    private Integer day;
    private String appName;
    private String systemName;
    private Long duration;
    private Long count;
    private Double avgDuration;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Double getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(Double avgDuration) {
        this.avgDuration = avgDuration;
    }

    public Long getTimestamp() {
        if (year != null && month != null && day != null) {
            return DateUtil.timestamp(year, month, day);
        }
        return null;
    }
}
