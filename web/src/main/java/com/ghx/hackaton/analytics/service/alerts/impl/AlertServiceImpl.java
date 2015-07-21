package com.ghx.hackaton.analytics.service.alerts.impl;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.dto.Alert;
import com.ghx.hackaton.analytics.service.RequestService;
import com.ghx.hackaton.analytics.service.alerts.AlertPlugin;
import com.ghx.hackaton.analytics.service.alerts.AlertService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by achumakov on 7/21/2015.
 */
@Service
public class AlertServiceImpl implements AlertService {

    /**
     * Set period for how many days from now we are looking when
     * creating Top N most frequent and most slowest requests.
     */
    private static final int WARNING_PERIOD_IN_DAYS = 7;

    /**
     * Top N records to fetch for warnings. 5 by default.
     */
    public static final int TOP_N_RECORDS = 5;

    @Autowired
    private RequestService requestService;

    @Autowired
    private DegradationDetectionPlugin degradationDetectionPlugin;

    private List<AlertPlugin> plugins = new ArrayList<AlertPlugin>();

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    private List<Alert> recalculatedAlerts = new ArrayList<Alert>();

    @PostConstruct
    public void init() {
        plugins.add(degradationDetectionPlugin);
        executorService.scheduleAtFixedRate(new Scheduler(), 0, 5, TimeUnit.MINUTES);
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdownNow();
    }

    public List<Alert> getAlerts(String appName) {
        List<Alert> alerts = new ArrayList<Alert>();
        alerts.addAll(getRecalculatedAlerts(appName));
        alerts.addAll(getWarnings(appName));
        return alerts;
    }

    private List<Alert> getWarnings(String appName) {
        Date endDate = new Date();
        Date startDate = DateUtils.addDays(endDate, -WARNING_PERIOD_IN_DAYS);
        List<Alert> warnings = new ArrayList<Alert>();
        List<Request> frequentRequests = requestService.getMostFrequent(startDate, endDate, appName, TOP_N_RECORDS);
        List<Request> slowestRequests = requestService.getSlowest(startDate, endDate, appName, TOP_N_RECORDS);
        for (Request request : slowestRequests) {
            String message = String.format("One of Top %s most slowest requests. Called %s times for last %s days. "
                            + "Average request processing time is %s ms.",
                    TOP_N_RECORDS, request.getCount(), WARNING_PERIOD_IN_DAYS, request.getAvgDuration());
            Alert warning = new Alert(appName, request.getUrl(), message, Alert.Status.warning);
            warnings.add(warning);
        }
        for (Request request : frequentRequests) {
            String message = String.format("One of Top %s most frequently called URLs. Called %s times for last %s days. "
                            + "Average request processing time is %s ms.",
                    TOP_N_RECORDS, request.getCount(), WARNING_PERIOD_IN_DAYS, request.getAvgDuration());
            Alert warning = new Alert(appName, request.getUrl(), message, null);
            warnings.add(warning);
        }
        return warnings;
    }

    private void recalculateAlerts() {
        List<Alert> alerts = new ArrayList<Alert>();
        for (AlertPlugin plugin : plugins) {
            alerts.addAll(plugin.alerts());
        }
        recalculatedAlerts = alerts;
    }

    private List<Alert> getRecalculatedAlerts(String appName) {
        if (appName == null) {
            return recalculatedAlerts;
        }
        List<Alert> alerts = new ArrayList<Alert>();
        for (Alert alert : recalculatedAlerts) {
            if (appName.equals(alert.getApplication())) {
                alerts.add(alert);
            }
        }
        return alerts;
    }

    protected class Scheduler implements Runnable {

        public void run() {
            try {
                recalculateAlerts();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

    }

}
