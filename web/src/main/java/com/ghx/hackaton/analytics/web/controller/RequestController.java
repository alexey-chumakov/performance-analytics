package com.ghx.hackaton.analytics.web.controller;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.RequestDetails;
import com.ghx.hackaton.analytics.service.RequestDetailsService;
import com.ghx.hackaton.analytics.service.RequestService;
import com.ghx.hackaton.analytics.web.bean.RequestBean;
import com.ghx.hackaton.analytics.web.converter.ModelClientConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/request")
public class RequestController {

    private static final String UI_DATE_FORMAT = "yyyy-MM-dd";

    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestDetailsService requestDetailsService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(List<RequestBean> requests) {
        requestService.saveOrUpdate(ModelClientConverter.requestsToModel(requests));
    }

    @RequestMapping(value = "/forPeriod")
    @ResponseBody
    public List<Request> getRequests(@DateTimeFormat(pattern = UI_DATE_FORMAT)
                                    @RequestParam Calendar startDate,
                                    @DateTimeFormat(pattern = UI_DATE_FORMAT)
                                    @RequestParam Calendar endDate) {
        return requestService.find(startDate.getTime(), endDate.getTime());
    }

    @RequestMapping(value = "/details")
    @ResponseBody
    public List<RequestDetails> getRequestDetails(@DateTimeFormat(pattern = UI_DATE_FORMAT)
                                     @RequestParam Calendar startDate,
                                     @DateTimeFormat(pattern = UI_DATE_FORMAT)
                                     @RequestParam Calendar endDate,
                                     @ModelAttribute Request request) {
        return requestDetailsService.findByRequest(startDate.getTime(), endDate.getTime(), request);
    }
}
