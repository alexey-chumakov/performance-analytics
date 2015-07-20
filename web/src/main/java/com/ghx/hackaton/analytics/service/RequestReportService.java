package com.ghx.hackaton.analytics.service;

import com.ghx.hackaton.analytics.model.report.RequestDurationReport;
import com.ghx.hackaton.analytics.model.report.RequestUrlReport;

import java.util.Date;
import java.util.List;

public interface RequestReportService {
    List<RequestDurationReport> getDurationReport(Date from, Date to, String appName);

    List<RequestUrlReport> getMostFrequentRequestsReport(Date from, Date to, String appName, int howMany);
}
