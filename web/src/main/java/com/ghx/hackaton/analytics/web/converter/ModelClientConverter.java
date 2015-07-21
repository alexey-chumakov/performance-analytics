package com.ghx.hackaton.analytics.web.converter;

import com.ghx.hackaton.analytics.model.report.RequestDurationReport;
import com.ghx.hackaton.analytics.web.bean.RequestDurationReportBean;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModelClientConverter {

    public static RequestDurationReportBean toBean(RequestDurationReport report) {
        RequestDurationReportBean bean = new RequestDurationReportBean();
        bean.setAppName(report.getAppName());
        bean.setUrl(report.getUrl());
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

    public static List<RequestDurationReportBean> toBean(List<RequestDurationReport> reports) {
        List<RequestDurationReportBean> beans = new ArrayList<RequestDurationReportBean>();
        for (RequestDurationReport report : reports) {
            beans.add(toBean(report));
        }
        return beans;
    }
}
