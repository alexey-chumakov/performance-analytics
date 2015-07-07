package com.ghx.hackaton.analytics.service.impl;

import com.ghx.hackaton.analytics.dao.RequestDetailsDAO;
import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.RequestDetails;
import com.ghx.hackaton.analytics.service.RequestDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RequestDetailsServiceImpl implements RequestDetailsService {

    @Autowired
    private RequestDetailsDAO requestDetailsDAO;

    @Override
    public List<RequestDetails> findByRequest(Date from, Date to, Request request) {
        return requestDetailsDAO.find(from, to, request);
    }
}
