package com.ghx.hackaton.analytics.web.controller;

import com.ghx.hackaton.analytics.service.RequestService;
import com.ghx.hackaton.analytics.web.bean.RequestBean;
import com.ghx.hackaton.analytics.web.converter.ModelClientConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/request")
public class RequestController {

    private static final String MODEL = "requests";
    private static final String UI_DATE_FORMAT = "yyyy-MM-dd HH:mm";

    @Autowired
    private RequestService requestService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(List<RequestBean> requests) {
        requestService.save(ModelClientConverter.requestsToModel(requests));
    }

    @RequestMapping(value = "/forPeriod")
    public ModelAndView getRequests(@DateTimeFormat(pattern = UI_DATE_FORMAT)
                                    @RequestParam Calendar startDate,
                                    @DateTimeFormat(pattern = UI_DATE_FORMAT)
                                    @RequestParam Calendar endDate) {
        ModelAndView modelAndView = new ModelAndView("request-list");
        modelAndView.addObject(MODEL, requestService.find(startDate.getTime(), endDate.getTime()));
        return modelAndView;
    }
}
