package com.ghx.hackaton.analytics.service.impl;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.dto.RequestDuration;
import com.ghx.hackaton.analytics.model.report.RequestDurationReport;
import com.ghx.hackaton.analytics.model.report.RequestUrlReport;
import com.ghx.hackaton.analytics.service.RequestDetailsService;
import com.ghx.hackaton.analytics.service.RequestReportService;
import com.ghx.hackaton.analytics.service.RequestService;
import com.ghx.hackaton.analytics.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Service
public class RequestReportServiceImpl implements RequestReportService {

    private static final String JAVA_KEY = "Java";

    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestDetailsService requestDetailsService;

    @Override
    public List<RequestDurationReport> getDurationReport(Date from, Date to, String appName, String url) {

        // appName -> total duration
        Map<String, RequestDuration> appsTotalRequestDurations = groupByAppName(requestService.getTotalByApp(from, to, appName, url));

        // appName -> total duration by days and system names
        Map<String, List<RequestDuration>> appsDailyRequestDurations = groupListByAppName(requestService.getAggregatedByDate(from, to, appName, url));

        // appName -> total duration by system names
        Map<String, List<RequestDuration>> appsTotalBySystemName = groupListByAppName(requestDetailsService.getTotalBySystemNames(from, to, appName, url));

        Set<String> apps = new TreeSet<String>();
        apps.addAll(appsTotalRequestDurations.keySet());
        apps.addAll(appsDailyRequestDurations.keySet());
        apps.addAll(appsTotalBySystemName.keySet());

        List<RequestDurationReport> reports = new ArrayList<RequestDurationReport>();
        for (String app : apps) {
            RequestDuration totalRequestDuration = appsTotalRequestDurations.get(app);
            List<RequestDuration> dailyRequestDurations = appsDailyRequestDurations.get(app);
            dailyRequestDurations = dailyRequestDurations != null ? dailyRequestDurations : new ArrayList<RequestDuration>();
            List<RequestDuration> totalBySystemName = appsTotalBySystemName.get(app);
            totalBySystemName = totalBySystemName != null ? totalBySystemName : new ArrayList<RequestDuration>();

            RequestDurationReport report = new RequestDurationReport();
            report.setAppName(app);
            report.setUrl(url);
            addTotalDurationInfo(totalRequestDuration, report);
            addTotalDurationsBySystemNameInfo(totalBySystemName, totalRequestDuration, report);
            addDailyDurationsInfo(dailyRequestDurations, report);

            reports.add(report);
        }

        return reports;
    }

    private Map<String, RequestDuration> groupByAppName(List<RequestDuration> requestDurations) {
        Map<String, RequestDuration> grouped = new HashMap<String, RequestDuration>();
        for (RequestDuration requestDuration : requestDurations) {
            grouped.put(requestDuration.getAppName(), requestDuration);
        }
        return grouped;
    }

    private Map<String, List<RequestDuration>> groupListByAppName(List<RequestDuration> requestDurations) {
        Map<String, List<RequestDuration>> grouped = new HashMap<String, List<RequestDuration>>();
        for (RequestDuration requestDuration : requestDurations) {
            String appName = requestDuration.getAppName();
            if (!grouped.containsKey(appName)) {
                grouped.put(appName, new ArrayList<RequestDuration>());
            }
            grouped.get(appName).add(requestDuration);
        }
        return grouped;
    }

    private void addTotalDurationInfo(RequestDuration totalDuration, RequestDurationReport report) {
        report.setTotalDuration(totalDuration.getAvgDuration());
    }

    private void addTotalDurationsBySystemNameInfo(List<RequestDuration> totalBySystemName, RequestDuration totalDuration, RequestDurationReport report) {
        addOtherDuration(totalDuration, totalSystemDuration(totalBySystemName), totalBySystemName);
        report.setTotalRequestDurations(totalBySystemName);
    }

    private long totalSystemDuration(List<RequestDuration> totalBySystemName) {
        long totalDuration = 0;
        for (RequestDuration requestDuration : totalBySystemName) {
            totalDuration += requestDuration.getDuration();
        }
        return totalDuration;
    }

    private void addOtherDuration(RequestDuration total, long totalSystemDuration, List<RequestDuration> totalBySystemName) {
        RequestDuration requestDuration = new RequestDuration();
        requestDuration.setAvgDuration(avg(total.getDuration() - totalSystemDuration, total.getCount()));
        requestDuration.setSystemName(JAVA_KEY);
        totalBySystemName.add(requestDuration);
    }

    private double avg(long total, long count) {
        return count != 0 ? (double) total / count : 0;
    }

    private void addDailyDurationsInfo(List<RequestDuration> dailyRequestDurations, RequestDurationReport report) {
        // timestamp -> (systemName -> duration)
        Map<Long, Map<String, Double>> durations = new LinkedHashMap<Long, Map<String, Double>>();

        for (RequestDuration rd : dailyRequestDurations) {
            long timestamp = DateUtil.timestamp(rd.getYear(), rd.getMonth(), rd.getDay());
            if (!durations.containsKey(timestamp)) {
                durations.put(timestamp, new LinkedHashMap<String, Double>());
            }
            durations.get(timestamp).put(rd.getSystemName(), rd.getAvgDuration());
        }
        report.setDailyDurations(durations);
    }
}
