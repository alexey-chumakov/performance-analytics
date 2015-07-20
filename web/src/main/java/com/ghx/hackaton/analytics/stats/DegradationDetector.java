package com.ghx.hackaton.analytics.stats;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.service.RequestService;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by achumakov on 7/20/2015.
 */
@Component
public class DegradationDetector {

    private static int MILLIS_PER_DAY = 1000*60*60*24;

    @Autowired
    private RequestService requestService;

    public String[] alerts() {
        fiveDaysAlert();
        monthsAlert();

        return null;


    }

    private void fiveDaysAlert() {
        Date now = new Date();
        Date fiveDaysBefore = DateUtils.addDays(now, -5);

        List<Request> durations = requestService.getAggregatedByDateForUrl(fiveDaysBefore, now, "JS");
        double slope = findDegradationSlope(durations);

        System.out.println(String.format("Slope of [JS] = %s", slope));
    }

    private void monthsAlert() {
        Date now = new Date();
        Date fiveDaysBefore = DateUtils.addMonths(now, -1);

        List<Request> durations = requestService.getAggregatedByDateForUrl(fiveDaysBefore, now, "JS");
        double slope = findDegradationSlope(durations);

        System.out.println(String.format("Slope of [JS] = %s", slope));
    }


    protected double findDegradationSlope(List<Request> durations) {
        SimpleRegression simpleRegression = new SimpleRegression();
        for (Request duration : durations) {
            simpleRegression.addData(duration.getTimestamp() / MILLIS_PER_DAY, duration.getAvgDuration());
        }

        return simpleRegression.getSlope();

    }




}
