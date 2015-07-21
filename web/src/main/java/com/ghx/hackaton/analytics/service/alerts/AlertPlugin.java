package com.ghx.hackaton.analytics.service.alerts;

import com.ghx.hackaton.analytics.model.dto.Alert;

import java.util.Collection;

/**
 * Created by achumakov on 7/21/2015.
 */
public interface AlertPlugin {

    Collection<Alert> alerts();

}
