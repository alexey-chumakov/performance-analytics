package com.ghx.hackaton.analytics.stats;

import com.ghx.hackaton.analytics.model.dto.Alert;
import com.ghx.hackaton.analytics.service.alerts.impl.DegradationDetectionPlugin;
import com.ghx.hackaton.analytics.spring.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.List;

/**
 * Created by achumakov on 7/20/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class DegradationDetectionPluginTest {

    @Autowired
    private DegradationDetectionPlugin degradationDetector;

    @Test
    public void test() {
        Collection<Alert> alerts = degradationDetector.alerts();
        for (Alert alert : alerts) {
            System.out.println(alert);
        }
    }

}
