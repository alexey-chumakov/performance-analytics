package com.ghx.hackaton.analytics.service.impl;

import com.ghx.hackaton.analytics.model.dto.RequestDuration;
import com.ghx.hackaton.analytics.model.report.RequestDurationReport;
import com.ghx.hackaton.analytics.service.RequestDetailsService;
import com.ghx.hackaton.analytics.service.RequestReportService;
import com.ghx.hackaton.analytics.service.RequestService;
import com.ghx.hackaton.analytics.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RequestReportServiceImpl implements RequestReportService {

    private static final String JAVA_KEY = "Java";

    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestDetailsService requestDetailsService;

    @Override
    public RequestDurationReport getDurationReport(Date from, Date to) {
        RequestDuration totalRequestDuration = requestService.getTotal(from, to);
        List<RequestDuration> dailyRequestDurations = requestService.getAggregatedByDate(from, to);
        List<RequestDuration> totalBySystemName = requestDetailsService.getTotalBySystemNames(from, to);
        addOtherDuration(totalRequestDuration.getAvgDuration(), totalSystemDuration(totalBySystemName), totalBySystemName);

        // timestamp -> (systemName -> duration)
        Map<Long, Map<String, Double>> durations = new LinkedHashMap<Long, Map<String, Double>>();

        for (RequestDuration rd : dailyRequestDurations) {
            long timestamp = DateUtil.timestamp(rd.getYear(), rd.getMonth(), rd.getDay());
            if (!durations.containsKey(timestamp)) {
                durations.put(timestamp, new LinkedHashMap<String, Double>());
            }
            durations.get(timestamp).put(rd.getSystemName(), rd.getAvgDuration());
        }

        RequestDurationReport report = new RequestDurationReport();
        report.setTotalDuration(totalRequestDuration.getAvgDuration());
        report.setTotalRequestDurations(totalBySystemName);
        report.setDailyDurations(durations);

        return report;
    }

    private long totalSystemDuration(List<RequestDuration> totalBySystemName) {
        long total = 0;
        for (RequestDuration requestDuration : totalBySystemName) {
            total += requestDuration.getAvgDuration();
        }
        return total;
    }

    private void addOtherDuration(double totalDuration, double totalSystemDuration, List<RequestDuration> totalBySystemName) {
        RequestDuration requestDuration = new RequestDuration();
        requestDuration.setAvgDuration(totalDuration - totalSystemDuration);
        requestDuration.setSystemName(JAVA_KEY);
        totalBySystemName.add(requestDuration);
    }
}
