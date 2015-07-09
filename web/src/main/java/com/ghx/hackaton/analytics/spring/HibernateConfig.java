package com.ghx.hackaton.analytics.spring;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    @Bean
    public SessionFactory sessionFactory() throws IOException {
        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource());
        builder.scanPackages("com.ghx.hackaton.analytics.model")
                .addProperties(hibernateProperties());

        return builder.buildSessionFactory();
    }

    @Bean
    public Properties hibernateProperties() throws IOException {
        PropertiesFactoryBean pf = new PropertiesFactoryBean();
        pf.setLocation(new ClassPathResource("hibernate.properties"));
        pf.afterPropertiesSet();
        return pf.getObject();
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://192.168.0.137:3306/ghx-hackaton");
        ds.setUsername("ghx_hackaton");
        ds.setPassword("ghx_hackaton");
        return ds;
    }

    @Bean
    public HibernateTransactionManager txManager() throws IOException {
        return new HibernateTransactionManager(sessionFactory());
    }
}
