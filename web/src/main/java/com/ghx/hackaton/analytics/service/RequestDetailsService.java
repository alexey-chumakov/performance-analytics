package com.ghx.hackaton.analytics.service;

import com.ghx.hackaton.analytics.model.RequestDetails;

import java.util.List;

public interface RequestDetailsService {

    List<RequestDetails> findByRequestId(long requestId);

}
