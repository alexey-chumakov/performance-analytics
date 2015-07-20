package com.ghx.hackaton.analytics.dao;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.RequestDetails;
import com.ghx.hackaton.analytics.model.dto.RequestDuration;

import java.util.Date;
import java.util.List;

public interface RequestDetailsDAO extends AbstractEntityDAO<RequestDetails> {

    int updateRequestDetails(RequestDetails requestDetails);

    List<RequestDetails> find(Date from, Date to, Request request);

    void delete(Date from, Date to);

    List<RequestDuration> getTotalBySystemNames(Date from, Date to, String appName);

}
