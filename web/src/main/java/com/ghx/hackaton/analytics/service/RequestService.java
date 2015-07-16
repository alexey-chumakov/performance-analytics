package com.ghx.hackaton.analytics.service;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.dto.RequestDuration;

import java.util.Date;
import java.util.List;

public interface RequestService {

    void saveOrUpdate(List<Request> requests);

    List<Request> find(Date from, Date to);

    void delete(Date from, Date to);

    List<RequestDuration> getAggregatedByDate(Date from, Date to);

    List<RequestDuration> getTotalByApp(Date from, Date to);

}
