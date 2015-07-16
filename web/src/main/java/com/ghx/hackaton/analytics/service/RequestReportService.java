package com.ghx.hackaton.analytics.service;

import com.ghx.hackaton.analytics.model.report.RequestDurationReport;

import java.util.Date;
import java.util.List;

public interface RequestReportService {
    List<RequestDurationReport> getDurationReport(Date from, Date to);
}
