package com.ghx.hackaton.analytics.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = { "com.ghx.hackaton.analytics.dao", "com.ghx.hackaton.analytics.model", "com.ghx.hackaton.analytics.service" })
@Import(HibernateConfig.class)
public class TestConfig {
}
