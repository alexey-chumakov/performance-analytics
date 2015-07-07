package com.ghx.hackaton.analytics.service;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.RequestDetails;

import java.util.Date;
import java.util.List;

public interface RequestDetailsService {

    List<RequestDetails> findByRequest(Date from, Date to, Request request);

}
