package com.ghx.hackaton.analytics.service;

import com.ghx.hackaton.analytics.model.report.RequestDurationReport;

import java.util.Date;

public interface RequestReportService {
    RequestDurationReport getDurationReport(Date from, Date to);
}
