package com.ghx.hackaton.analytics.service;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.RequestDetails;
import com.ghx.hackaton.analytics.model.dto.RequestDuration;

import java.util.Date;
import java.util.List;

public interface RequestDetailsService {

    List<RequestDetails> findByRequest(Date from, Date to, Request request);

    List<RequestDuration> getTotalBySystemNames(Date from, Date to, String app);

}
