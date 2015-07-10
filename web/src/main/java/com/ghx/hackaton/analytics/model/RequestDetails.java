package com.ghx.hackaton.analytics.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
        @NamedQuery(name = RequestDetails.SELECT_AGGREGATED_BY_REQUEST_AND_DATE_RANGE_QUERY,
                query = "select " +
                        "rd.systemName as systemName, " +
                        "sum(rd.count) as count, " +
                        "sum(rd.duration) as duration " +
                        "from RequestDetails rd join rd.request r " +
                        "where :fromYear <= r.year and r.year <= :toYear " +
                        "and :fromMonth <= r.month and r.month <= :toMonth " +
                        "and :fromDay <= r.day and r.day <= :toDay " +
                        "and r.appName = :appName and r.serverId = :serverId " +
                        "and r.url = :url " +
                        "group by rd.systemName"),

        @NamedQuery(name = RequestDetails.UPDATE_QUERY,
                query = "update RequestDetails rd set rd.count = rd.count + :newCount, rd.duration = rd.duration + :newDuration " +
                        "where rd.systemName = :systemName " +
                        "and rd.request.id = :requestId"),

        @NamedQuery(name = RequestDetails.DELETE_BY_REQUEST_DATE_RANGE,
                query = "delete from RequestDetails rd " +
                        "where rd.request.id in " +
                        "(select r.id from Request r " +
                        "where :fromDate <= r.timestamp and r.timestamp <= :toDate)")
})

@Entity
@Table(name = "REQUEST_DETAILS")
public class RequestDetails extends AbstractEntity {

    public static final String SELECT_AGGREGATED_BY_REQUEST_AND_DATE_RANGE_QUERY = "RequestDetails.SELECT_AGGREGATED_BY_REQUEST_AND_DATE_RANGE_QUERY";
    public static final String UPDATE_QUERY = "RequestDetails.UPDATE_QUERY";
    public static final String DELETE_BY_REQUEST_DATE_RANGE = "RequestDetails.DELETE_BY_REQUEST_DATE_RANGE";

    @Column(name = "SYSTEM_NAME", nullable = false)
    private String systemName;

    @Column(name = "COUNT", nullable = false)
    private Long count;

    @Column(name = "DURATION", nullable = false)
    private Long duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUEST_ID", nullable = false)
    private Request request;

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
