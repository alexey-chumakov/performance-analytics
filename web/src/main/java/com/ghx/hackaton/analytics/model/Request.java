package com.ghx.hackaton.analytics.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@NamedNativeQueries({
        @NamedNativeQuery(name = Request.SELECT_TOTAL_DURATION_QUERY,
                query = "SELECT " +
                        "COALESCE (SUM(r.COUNT), 0) as count, " +
                        "COALESCE (SUM(r.DURATION), 0) as duration, " +
                        "COALESCE (SUM(r.DURATION) / SUM(r.COUNT), 0) as avgDuration " +
                        "FROM REQUEST r " +
                        "WHERE :fromDate <= r.TIMESTAMP and r.TIMESTAMP <= :toDate"
        ),
        @NamedNativeQuery(name = Request.SELECT_DAILY_AGGREGATES_BY_DATE_RANGE_QUERY,
                query = "(SELECT r.YEAR as year, r.MONTH as month, r.DAY as day, 'Total' as systemName, " +
                        "COALESCE (SUM(r.COUNT), 0) as count, " +
                        "COALESCE (SUM(r.DURATION), 0) as duration, " +
                        "COALESCE (SUM(r.DURATION) / SUM(r.COUNT), 0) as avgDuration " +
                        "FROM REQUEST r " +
                        "WHERE :fromDate <= r.TIMESTAMP and r.TIMESTAMP <= :toDate " +
                        "GROUP BY r.YEAR, r.MONTH, r.DAY) " +
                        "union " +
                        "(SELECT r.YEAR as year, r.MONTH as month, r.DAY as day, rd.SYSTEM_NAME as systemName, " +
                        "COALESCE (SUM(rd.COUNT), 0) as count, " +
                        "COALESCE (SUM(rd.DURATION), 0) as duration, " +
                        "COALESCE (SUM(rd.DURATION) / SUM(rd.COUNT), 0) as avgDuration " +
                        "FROM REQUEST_DETAILS rd JOIN REQUEST r ON r.id = rd.REQUEST_ID " +
                        "WHERE :fromDate <= r.TIMESTAMP and r.TIMESTAMP <= :toDate " +
                        "GROUP BY r.YEAR, r.MONTH, r.DAY, rd.SYSTEM_NAME) " +
                        "ORDER BY year, month, day"
        )
})
@NamedQueries({
        @NamedQuery(name = Request.SELECT_AGGREGATED_BY_DATE_RANGE_QUERY,
                query = "select " +
                        "r.timestamp as timestamp, " +
                        "r.year as year, " +
                        "r.month as month, " +
                        "r.day as day, " +
                        "r.hour as hour, " +
                        "r.appName as appName, " +
                        "r.serverId as serverId, " +
                        "r.url as url, " +
                        "sum(r.count) as count, " +
                        "sum(r.duration) as duration, " +
                        "sum(r.duration) / sum(r.count) as avgDuration " +
                        "from Request r " +
                        "where :fromDate <= r.timestamp and r.timestamp <= :toDate " +
                        "group by r.year, r.month, r.day, r.hour, r.appName, r.serverId, r.url"),

        @NamedQuery(name = Request.UPDATE_QUERY,
                query = "update Request r set r.count = r.count + :newCount, r.failedCount = r.failedCount + :newFailedCount, " +
                        "r.duration = r.duration + :newDuration " +
                        "where r.year = :year and r.month = :month " +
                        "and r.day = :day and r.hour = :hour " +
                        "and r.minute = :minute and r.appName = :appName " +
                        "and r.serverId = :serverId and r.url = :url"),

        @NamedQuery(name = Request.DELETE_BY_DATE_RANGE_QUERY,
                query = "delete Request r " +
                        "where :fromDate <= r.timestamp and r.timestamp <= :toDate")
})
@Entity
@Table(name = "REQUEST")
public class Request extends AbstractEntity {

    public static final String SELECT_TOTAL_DURATION_QUERY = "Request.SELECT_TOTAL_DURATION_QUERY";
    public static final String SELECT_DAILY_AGGREGATES_BY_DATE_RANGE_QUERY = "Request.SELECT_DAILY_AGGREGATES_BY_DATE_RANGE_QUERY";

    public static final String SELECT_AGGREGATED_BY_DATE_RANGE_QUERY = "Request.SELECT_AGGREGATED_BY_DATE_RANGE_QUERY";
    public static final String UPDATE_QUERY = "Request.UPDATE_QUERY";
    public static final String DELETE_BY_DATE_RANGE_QUERY = "Request.DELETE_BY_DATE_RANGE_QUERY";

    @Column(name = "TIMESTAMP", nullable = false)
    private Long timestamp;

    @Column(name = "YEAR", nullable = false)
    private Integer year;

    @Column(name = "MONTH", nullable = false)
    private Integer month;

    @Column(name = "DAY", nullable = false)
    private Integer day;

    @Column(name = "HOUR", nullable = false)
    private Integer hour;

    @Column(name = "MINUTE", nullable = false)
    private Integer minute;

    @Column(name = "APP_NAME", nullable = false)
    private String appName;

    @Column(name = "SERVER_ID", nullable = false)
    private String serverId;

    @Column(name = "URL", nullable = false)
    private String url;

    @Column(name = "COUNT", nullable = false)
    private Long count;

    @Column(name = "FAILED_COUNT", nullable = false)
    private Long failedCount;

    @Column(name = "DURATION", nullable = false)
    private Long duration;

    @Column(name = "AVG_DURATION", insertable = false, updatable = false)
    private Double avgDuration;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "request")
    private List<RequestDetails> details;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

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

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Long failedCount) {
        this.failedCount = failedCount;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Double getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(Double avgDuration) {
        this.avgDuration = avgDuration;
    }

    public List<RequestDetails> getDetails() {
        return details;
    }

    public void setDetails(List<RequestDetails> details) {
        this.details = details;
    }
}
