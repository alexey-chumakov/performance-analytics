package com.ghx.hackaton.analytics.web.controller;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.RequestDetails;
import com.ghx.hackaton.analytics.service.RequestDetailsService;
import com.ghx.hackaton.analytics.service.RequestReportService;
import com.ghx.hackaton.analytics.service.RequestService;
import com.ghx.hackaton.analytics.service.order.Order;
import com.ghx.hackaton.analytics.service.pagination.Pagination;
import com.ghx.hackaton.analytics.web.bean.RequestDurationReportBean;
import com.ghx.hackaton.analytics.web.converter.ModelClientConverter;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/request")
public class RequestController {

    private static final String UI_DATE_FORMAT = "yyyy-MM-dd";

    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestDetailsService requestDetailsService;
    @Autowired
    private RequestReportService requestReportService;

    @RequestMapping(value = "/forPeriod")
    @ResponseBody
    public Map<String, Object> getRequests(@DateTimeFormat(pattern = UI_DATE_FORMAT)
                                    @RequestParam Calendar startDate,
                                    @DateTimeFormat(pattern = UI_DATE_FORMAT)
                                    @RequestParam Calendar endDate,
                                    @RequestParam(required = false) String appName,
                                    @ModelAttribute Pagination pagination,
                                    @ModelAttribute Order order) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("totalPages", requestService.pagesCount(startDate.getTime(), DateUtils.addDays(endDate.getTime(), 1), appName, pagination));
        data.put("requests", requestService.findAggregatedByUrl(startDate.getTime(), DateUtils.addDays(endDate.getTime(), 1), appName, order, pagination));
        return data;
    }

    @RequestMapping(value = "/details")
    @ResponseBody
    public List<RequestDetails> getRequestDetails(@DateTimeFormat(pattern = UI_DATE_FORMAT)
                                     @RequestParam Calendar startDate,
                                     @DateTimeFormat(pattern = UI_DATE_FORMAT)
                                     @RequestParam Calendar endDate,
                                     @ModelAttribute Request request) {
        return requestDetailsService.findByRequest(startDate.getTime(), DateUtils.addDays(endDate.getTime(), 1), request);
    }

    @RequestMapping(value = "/durationReport")
    @ResponseBody
    public List<RequestDurationReportBean> getDurationReport(@DateTimeFormat(pattern = UI_DATE_FORMAT)
                                                  @RequestParam Calendar startDate,
                                                  @DateTimeFormat(pattern = UI_DATE_FORMAT)
                                                  @RequestParam Calendar endDate,
                                                  @RequestParam(required = false) String appName) {
        return ModelClientConverter.toBean(requestReportService.getDurationReport(startDate.getTime(), DateUtils.addDays(endDate.getTime(), 1), appName, null));
    }

    @RequestMapping(value = "/url-details")
    @ResponseBody
    public List<RequestDurationReportBean> getUrlDetails(@DateTimeFormat(pattern = UI_DATE_FORMAT)
                                    @RequestParam Calendar startDate,
                                    @DateTimeFormat(pattern = UI_DATE_FORMAT)
                                    @RequestParam Calendar endDate,
                                    @RequestParam String reqUrl,
                                    @RequestParam(required = false) String appName) {
        return ModelClientConverter.toBean(requestReportService.getDurationReport(startDate.getTime(), DateUtils.addDays(endDate.getTime(), 1), appName, reqUrl));
    }
}
