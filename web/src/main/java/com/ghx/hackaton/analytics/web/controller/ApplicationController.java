package com.ghx.hackaton.analytics.web.controller;

import com.ghx.hackaton.analytics.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private RequestService requestService;

    @RequestMapping(value = "/names")
    @ResponseBody
    public List<String> getAlerts() {
        return requestService.getAppNames();
    }
}