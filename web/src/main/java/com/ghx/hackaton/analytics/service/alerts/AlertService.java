package com.ghx.hackaton.analytics.service.alerts;

import com.ghx.hackaton.analytics.model.dto.Alert;

import java.util.List;

/**
 * Created by achumakov on 7/21/2015.
 */
public interface AlertService {

    List<Alert> getAlerts(String appName);

}
