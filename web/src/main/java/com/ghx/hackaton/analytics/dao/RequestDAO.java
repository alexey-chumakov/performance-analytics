package com.ghx.hackaton.analytics.dao;

import com.ghx.hackaton.analytics.model.Request;

import java.util.Date;
import java.util.List;

public interface RequestDAO extends AbstractEntityDAO<Request> {

    List<Request> find(Date from, Date to);

}
