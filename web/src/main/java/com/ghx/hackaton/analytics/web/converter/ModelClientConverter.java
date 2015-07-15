package com.ghx.hackaton.analytics.web.converter;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.RequestDetails;
import com.ghx.hackaton.analytics.model.report.RequestDurationReport;
import com.ghx.hackaton.analytics.util.DateUtil;
import com.ghx.hackaton.analytics.web.bean.RequestBean;
import com.ghx.hackaton.analytics.web.bean.RequestDetailsBean;
import com.ghx.hackaton.analytics.web.bean.RequestDurationReportBean;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModelClientConverter {

    public static List<Request> requestsToModel(List<RequestBean> beans) {
        List<Request> model = new ArrayList<Request>();

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
        List<RequestDetails> model = new ArrayList<RequestDetails>();

        for (RequestDetailsBean bean : beans) {
            RequestDetails requestDetails = new RequestDetails();
            requestDetails.setSystemName(bean.getSystemName());
            requestDetails.setCount(bean.getCount());
            requestDetails.setDuration(bean.getDuration());
            model.add(requestDetails);
        }

        return model;
    }

    public static RequestDurationReportBean toBean(RequestDurationReport report) {
        RequestDurationReportBean bean = new RequestDurationReportBean();
        bean.setTotalDuration(report.getTotalDuration());
        bean.setTotalRequestDurations(report.getTotalRequestDurations());

        List<Map<String, Object>> dailyDurations = new ArrayList<Map<String, Object>>();
        for (Map.Entry<Long, Map<String, Double>> entry : report.getDailyDurations().entrySet()) {
            Map<String, Object> reqDuration = new LinkedHashMap<String, Object>();
            reqDuration.put("timestamp", entry.getKey());
            reqDuration.putAll(entry.getValue());
            dailyDurations.add(reqDuration);
        }

        bean.setDailyDurations(dailyDurations);
        return bean;
    }
}
