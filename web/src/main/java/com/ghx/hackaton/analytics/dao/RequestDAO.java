package com.ghx.hackaton.analytics.dao;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.dto.RequestDuration;

import java.util.Date;
import java.util.List;

public interface RequestDAO extends AbstractEntityDAO<Request> {

    int updateRequest(Request request);

    List<Request> find(Date from, Date to, String appName);

    Long findIdByExample(Request request);

    void delete(Date from, Date to);

    List<RequestDuration> getAggregatedByDate(Date from, Date to, String appName);

    List<RequestDuration> getTotalByApp(Date from, Date to, String appName);

    List<Request> getTopAggregatedByUrlSorted(Date from, Date to, String appName, String field, boolean asc, int howMany);

}
