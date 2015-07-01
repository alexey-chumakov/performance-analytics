package com.ghx.hackaton.analytics.dao;

import com.ghx.hackaton.analytics.model.RequestDetails;

import java.util.List;

public interface RequestDetailsDAO extends AbstractEntityDAO<RequestDetails> {

    List<RequestDetails> findByRequestId(long requestId);

}
