package com.ghx.hackaton.analytics.service.alerts.impl;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.dto.Alert;
import com.ghx.hackaton.analytics.service.RequestService;
import com.ghx.hackaton.analytics.service.alerts.AlertPlugin;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by achumakov on 7/20/2015.
 */
@Component
public class DegradationDetectionPlugin implements AlertPlugin {

    private static final double STAT_SAMPLE_PERCENTAGE_THRESHOLD = 0.8;

    private static final int DEGRADATION_THRESHOLD_PERCENT = 20;

    private static int MILLIS_PER_DAY = 1000 * 60 * 60 * 24;

    @Autowired
    private RequestService requestService;

    // TODO refactor this
    public Collection<Alert> alerts() {
        Set<Alert> alerts = new HashSet<Alert>();
        List<String> appNames = requestService.getAppNames();
        for (String appName : appNames) {
            for (String url : requestService.getAppURLs(appName)) {
                // process 5 day short-term degradation
                Date endDate = new Date();
                Date startDate = DateUtils.addDays(endDate, -7);
                String message = getDegradationAlerts(url, 7);
                if (message != null) {
                    Alert alert = new Alert(appName, url, message, Alert.Status.danger);
                    alert.setStartEndDates(startDate, endDate);
                    alerts.add(alert);
                }

                // process 30 day long-term degradation
                endDate = new Date();
                startDate = DateUtils.addDays(endDate, -30);
                message = getDegradationAlerts(url, 30);
                if (message != null) {
                    Alert alert = new Alert(appName, url, message, Alert.Status.danger);
                    alert.setStartEndDates(startDate, endDate);
                    alerts.add(alert);
                }
            }
        }

        return alerts;
    }

    private String getDegradationAlerts(String url, Integer daysAgo) {
        Date endDate = new Date();
        Date startDate = DateUtils.addDays(endDate, -daysAgo);

        List<Request> durations = requestService.getAggregatedByDateForUrl(startDate, endDate, url);
        // We can't say anything on this since there is no enough stats
        if (durations.isEmpty() || durations.size() <  daysAgo * STAT_SAMPLE_PERCENTAGE_THRESHOLD) return null;

        Pair<Double, Double> result = findDegradationSlope(durations);
        if (result == null) return null;

        Double slope = result.getFirst();
        Double intercept = result.getSecond();

        Double degradation = 100 * (slope * endDate.getTime() / MILLIS_PER_DAY - slope * startDate.getTime() / MILLIS_PER_DAY) / (slope * startDate.getTime() / MILLIS_PER_DAY + intercept);

        if (degradation > DEGRADATION_THRESHOLD_PERCENT) {
            return String.format("Performance degradation found. Request processing time is increased by %.2f%% over the past %s days.", degradation, daysAgo);
        } else {
            return null;
        }

    }


    protected Pair<Double, Double> findDegradationSlope(List<Request> durations) {
        SimpleRegression simpleRegression = new SimpleRegression();
        for (Request duration : durations) {
            simpleRegression.addData(duration.getTimestamp() / MILLIS_PER_DAY, duration.getAvgDuration());
        }

        Double slope = simpleRegression.getSlope();
        Double intercept = simpleRegression.getIntercept();

        return new Pair<Double, Double>(slope, intercept);
    }


}
