package com.ghx.hackaton.analytics.dao.impl;

import com.ghx.hackaton.analytics.dao.RequestDAO;
import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.util.DateUtil;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class RequestDAOImpl extends AbstractEntityDAOImpl<Request> implements RequestDAO {

    @Override
    public int updateRequest(Request request) {
        Query query = getSession().getNamedQuery(Request.UPDATE_QUERY);
        query.setLong("newCount", request.getCount());
        query.setLong("newDuration", request.getDuration());
        query.setInteger("year", request.getYear());
        query.setInteger("month", request.getMonth());
        query.setInteger("day", request.getDay());
        query.setInteger("hour", request.getHour());
        query.setInteger("minute", request.getMinute());
        query.setString("appName", request.getAppName());
        query.setString("serverId", request.getServerId());
        query.setString("url", request.getUrl());

        return query.executeUpdate();
    }

    @Override
    public List<Request> find(Date from, Date to) {
        Query query = getSession().getNamedQuery(Request.SELECT_AGGREGATED_BY_DATE_RANGE_QUERY);
        query.setInteger("fromYear", DateUtil.year(from));
        query.setInteger("toYear", DateUtil.year(to));
        query.setInteger("fromMonth", DateUtil.month(from));
        query.setInteger("toMonth", DateUtil.month(to));
        query.setInteger("fromDay", DateUtil.dayOfMonth(from));
        query.setInteger("toDay", DateUtil.dayOfMonth(to));
        query.setResultTransformer(Transformers.aliasToBean(getClazz()));

        return query.list();
    }

    @Override
    public Long findIdByExample(Request request) {
        Example example = Example.create(request);
        example.excludeProperty("id");
        example.excludeProperty("count");
        example.excludeProperty("duration");

        return (Long) getSession()
                .createCriteria(getClazz())
                .add(example)
                .setProjection(Projections.property("id"))
                .uniqueResult();
    }

    @Override
    public void delete(Date from, Date to) {
        Query query = getSession().getNamedQuery(Request.DELETE_BY_DATE_RANGE_QUERY);
        query.setInteger("fromYear", DateUtil.year(from));
        query.setInteger("toYear", DateUtil.year(to));
        query.setInteger("fromMonth", DateUtil.month(from));
        query.setInteger("toMonth", DateUtil.month(to));
        query.setInteger("fromDay", DateUtil.dayOfMonth(from));
        query.setInteger("toDay", DateUtil.dayOfMonth(to));

        query.executeUpdate();
    }

    @Override
    public void update(Request request) {
        Query query = getSession().getNamedQuery(Request.UPDATE_QUERY);
        query.setInteger("year", DateUtil.year(request.getYear()));
        query.setInteger("month", DateUtil.year(request.getMonth()));
        query.setInteger("day", DateUtil.year(request.getDay()));

    }
}
