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
        @NamedQuery(name = RequestDetails.SELECT_BY_REQUEST_QUERY,
                query = "from RequestDetails rd where rd.request.id = :id")
})

@Entity
@Table(name = "REQUEST_DETAILS")
public class RequestDetails extends AbstractEntity {

    public static final String SELECT_BY_REQUEST_QUERY = "RequestDetails.SELECT_BY_REQUEST_QUERY";

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
