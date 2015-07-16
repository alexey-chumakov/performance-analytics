package com.ghx.hackaton.analytics.service.impl;

import com.ghx.hackaton.analytics.dao.RequestDAO;
import com.ghx.hackaton.analytics.dao.RequestDetailsDAO;
import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.RequestDetails;
import com.ghx.hackaton.analytics.model.dto.RequestDuration;
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

    private static int ROUND_MINUTES = 5;

    @Autowired
    private RequestDAO requestDAO;
    @Autowired
    private RequestDetailsDAO requestDetailsDAO;

    @Override
    public void saveOrUpdate(List<Request> requests) {
        for (Request request : requests) {
            roundMinutes(request);
            Long existentId = requestDAO.findIdByExample(request);
            if (existentId != null) {
                request.setId(existentId);
                requestDAO.updateRequest(request);
                updateDetails(request.getDetails());
            } else {
                requestDAO.save(request);
            }
        }
    }

    private void roundMinutes(Request request) {
        if (request.getMinute() != null) {
            request.setMinute(request.getMinute() - request.getMinute() % ROUND_MINUTES);
        }
        if (request.getTimestamp() != null) {
            request.setTimestamp(request.getTimestamp() - request.getTimestamp() % (1000 * 60 * ROUND_MINUTES));
        }
    }

    private void updateDetails(List<RequestDetails> requestDetails) {
        if (requestDetails != null) {
            for (RequestDetails requestDetail : requestDetails) {
                int updated = requestDetailsDAO.updateRequestDetails(requestDetail);
                if (updated == 0) {
                    requestDetailsDAO.save(requestDetail);
                }
            }
        }
    }

    @Override
    public List<Request> find(Date from, Date to) {
        return requestDAO.find(DateUtil.truncateToHour(from), DateUtil.truncateToHour(to));
    }

    @Override
    public void delete(Date from, Date to) {
        requestDetailsDAO.delete(DateUtil.truncateToHour(from), DateUtil.truncateToHour(to));
        requestDAO.delete(DateUtil.truncateToHour(from), DateUtil.truncateToHour(to));
    }

    @Override
    public List<RequestDuration> getAggregatedByDate(Date from, Date to) {
        return requestDAO.getAggregatedByDate(from, to);
    }

    @Override
    public List<RequestDuration> getTotalByApp(Date from, Date to) {
        return requestDAO.getTotalByApp(from, to);
    }
}
