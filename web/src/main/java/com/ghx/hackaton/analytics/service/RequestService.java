package com.ghx.hackaton.analytics.service;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.dto.RequestDuration;
import com.ghx.hackaton.analytics.service.order.Order;
import com.ghx.hackaton.analytics.service.pagination.Pagination;

import java.util.Date;
import java.util.List;

public interface RequestService {

    void saveOrUpdate(List<Request> requests);

    List<Request> findAll(Date from, Date to, String appName);

    List<Request> findAggregatedByUrl(Date from, Date to, String appName, Order order, Pagination pagination);

    void delete(Date from, Date to);

    List<RequestDuration> getAggregatedByDate(Date from, Date to, String appName, String url);

    List<RequestDuration> getTotalByApp(Date from, Date to, String appName, String url);

    List<Request> getMostFrequent(Date from, Date to, String appName, int howMany);

    List<Request> getSlowest(Date from, Date to, String appName, int howMany);

    List<Request> getAggregatedByDateForUrl(Date from, Date to, String url);

    List<String> getAppNames();

    List<String> getAppURLs(String appName);

    long pagesCount(Date from, Date to, String appName, Pagination pagination);

}
