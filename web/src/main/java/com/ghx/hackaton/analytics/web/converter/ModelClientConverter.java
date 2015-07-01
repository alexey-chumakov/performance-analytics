package com.ghx.hackaton.analytics.web.converter;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.RequestDetails;
import com.ghx.hackaton.analytics.util.DateUtil;
import com.ghx.hackaton.analytics.web.bean.RequestBean;
import com.ghx.hackaton.analytics.web.bean.RequestDetailsBean;

import java.util.ArrayList;
import java.util.List;

public class ModelClientConverter {

    public static List<Request> requestsToModel(List<RequestBean> beans) {
        List<Request> model = new ArrayList<>();

        for (RequestBean bean : beans) {
            Request request = new Request();
            request.setYear(DateUtil.year(bean.getTimestamp()));
            request.setMonth(DateUtil.month(bean.getTimestamp()));
            request.setDay(DateUtil.dayOfMonth(bean.getTimestamp()));
            request.setHour(DateUtil.hourOfDay(bean.getTimestamp()));
            request.setMinute(DateUtil.minute(bean.getTimestamp()));
            request.setAppName(bean.getAppName());
            request.setServerId(bean.getServerId());
            request.setUrl(bean.getUrl());
            request.setCount((long) bean.getCount());
            request.setDuration((long) bean.getDuration());
            request.setDetails(requestDetailsToModel(bean.getDetails()));
            model.add(request);
        }

        return model;
    }

    public static List<RequestDetails> requestDetailsToModel(List<RequestDetailsBean> beans) {
        List<RequestDetails> model = new ArrayList<>();

        for (RequestDetailsBean bean : beans) {
            RequestDetails requestDetails = new RequestDetails();
            requestDetails.setSystemName(bean.getSystemName());
            requestDetails.setCount(bean.getCount());
            requestDetails.setDuration(bean.getDuration());
            model.add(requestDetails);
        }

        return model;
    }
}
