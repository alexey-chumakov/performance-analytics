package com.ghx.hackaton.analytics.web.controller;

import com.ghx.hackaton.analytics.model.dto.Alert;
import com.ghx.hackaton.analytics.service.alerts.AlertService;
import com.ghx.hackaton.analytics.service.alerts.impl.DegradationDetectionPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

/**
 * Created by achumakov on 7/20/2015.
 */
@Controller
@RequestMapping("/alerts")
public class AlertsController {

    @Autowired
    private AlertService alertService;

    @RequestMapping
    @ResponseBody
    public Collection<Alert> getAlerts(@RequestParam(required = false) String appName) {
        return alertService.getAlerts(appName);
    }

}
