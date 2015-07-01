package com.ghx.hackaton.analytics.service;

import com.ghx.hackaton.analytics.model.Request;

import java.util.Date;
import java.util.List;

public interface RequestService {

    void save(List<Request> requests);

    List<Request> find(Date from, Date to);

}
