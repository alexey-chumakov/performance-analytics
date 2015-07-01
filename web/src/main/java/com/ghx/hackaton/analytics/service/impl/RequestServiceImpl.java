package com.ghx.hackaton.analytics.service.impl;

import com.ghx.hackaton.analytics.dao.RequestDAO;
import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.service.RequestService;
import com.ghx.hackaton.analytics.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestDAO requestDAO;

    @Override
    public void save(List<Request> requests) {
        requestDAO.save(requests);
    }

    @Override
    public List<Request> find(Date from, Date to) {
        return requestDAO.find(DateUtil.truncateToHour(from), DateUtil.truncateToHour(to));
    }
}
