package com.ghx.hackaton.analytics.service.impl;

import com.ghx.hackaton.analytics.dao.RequestDAO;
import com.ghx.hackaton.analytics.dao.RequestDetailsDAO;
import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.RequestDetails;
import com.ghx.hackaton.analytics.model.dto.RequestDuration;
import com.ghx.hackaton.analytics.service.RequestService;
import com.ghx.hackaton.analytics.service.order.Order;
import com.ghx.hackaton.analytics.service.pagination.Pagination;
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
    public List<Request> findAll(Date from, Date to, String appName) {
        return requestDAO.findAll(DateUtil.truncateToHour(from), DateUtil.truncateToHour(to), appName);
    }

    @Override
    public List<Request> findAggregatedByUrl(Date from, Date to, String appName, Order order, Pagination pagination) {
        return requestDAO.getAggregatedByUrlSorted(DateUtil.truncateToHour(from), DateUtil.truncateToHour(to), appName, order, pagination);
    }

    @Override
    public void delete(Date from, Date to) {
        requestDetailsDAO.delete(DateUtil.truncateToHour(from), DateUtil.truncateToHour(to));
        requestDAO.delete(DateUtil.truncateToHour(from), DateUtil.truncateToHour(to));
    }

    @Override
    public List<RequestDuration> getAggregatedByDate(Date from, Date to, String appName, String url) {
        return requestDAO.getAggregatedByDate(from, to, appName, url);
    }

    @Override
    public List<RequestDuration> getTotalByApp(Date from, Date to, String appName, String url) {
        return requestDAO.getTotalByApp(from, to, appName, url);
    }

    @Override
    public List<Request> getMostFrequent(Date from, Date to, String appName, int howMany) {
        return requestDAO.getAggregatedByUrlSorted(from, to, appName, new Order("count", false), new Pagination(1, howMany));
    }

    @Override
    public List<Request> getSlowest(Date from, Date to, String appName, int howMany) {
        return requestDAO.getAggregatedByUrlSorted(from, to, appName, new Order("avgDuration", false), new Pagination(1, howMany));
    }

    @Override
    public List<Request> getAggregatedByDateForUrl(Date from, Date to, String url) {
        return requestDAO.getAggregatedByDateForUrl(from, to, url);
    }

    public List<String> getAppNames() {
        return requestDAO.getAppNames();
    }

    @Override
    public List<String> getAppURLs(String appName) {
        return requestDAO.getAppURLs(appName);
    }

    @Override
    public long pagesCount(Date from, Date to, String appName, Pagination pagination) {
        long total = requestDAO.aggregatedByUrlCount(from, to, appName);
        int itemsPerPage = pagination.getItemsPerPage();
        return total / itemsPerPage + (total % itemsPerPage != 0 ? 1 : 0);
    }
}
