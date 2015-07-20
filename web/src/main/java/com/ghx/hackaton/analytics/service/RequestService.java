package com.ghx.hackaton.analytics.service;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.dto.RequestDuration;

import java.util.Date;
import java.util.List;

public interface RequestService {

    void saveOrUpdate(List<Request> requests);

    List<Request> find(Date from, Date to, String appName);

    void delete(Date from, Date to);

    List<RequestDuration> getAggregatedByDate(Date from, Date to, String appName);

    List<RequestDuration> getTotalByApp(Date from, Date to, String appName);

    List<Request> getMostFrequent(Date from, Date to, String appName, int howMany);

    List<Request> getSlowest(Date from, Date to, String appName, int howMany);

    List<Request> getAggregatedByDateForUrl(Date from, Date to, String url);

    List<String> getAppURLs(String appName);

}
