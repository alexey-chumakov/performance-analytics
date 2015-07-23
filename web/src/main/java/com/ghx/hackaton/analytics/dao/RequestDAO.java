package com.ghx.hackaton.analytics.dao;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.dto.RequestDuration;
import com.ghx.hackaton.analytics.service.order.Order;
import com.ghx.hackaton.analytics.service.pagination.Pagination;

import java.util.Date;
import java.util.List;

public interface RequestDAO extends AbstractEntityDAO<Request> {

    int updateRequest(Request request);

    List<Request> findAll(Date from, Date to, String appName);

    Long findIdByExample(Request request);

    void delete(Date from, Date to);

    List<RequestDuration> getAggregatedByDate(Date from, Date to, String appName, String url);

    List<RequestDuration> getTotalByApp(Date from, Date to, String appName, String url);

    List<Request> getAggregatedByUrlSorted(Date from, Date to, String appName, Order order, Pagination pagination);

    List<Request> getAggregatedByDateForUrl(Date from, Date to, String url);

    List<String> getAppNames();

    List<String> getAppURLs(String appName);

    long aggregatedByUrlCount(Date from, Date to, String appName);
}
