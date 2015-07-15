package com.ghx.hackaton.analytics.dao.impl;

import com.ghx.hackaton.analytics.dao.RequestDAO;
import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.dto.RequestDuration;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
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
        query.setLong("fromDate", from.getTime());
        query.setLong("toDate", to.getTime());
        query.setResultTransformer(Transformers.aliasToBean(getClazz()));

        return query.list();
    }

    @Override
    public Long findIdByExample(Request request) {
        Example example = Example.create(request);
        example.excludeProperty("id");
        example.excludeProperty("timestamp");
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
        query.setLong("fromDate", from.getTime());
        query.setLong("toDate", to.getTime());

        query.executeUpdate();
    }

    @Override
    public List<RequestDuration> getAggregatedByDate(Date from, Date to) {
        SQLQuery sqlQuery = (SQLQuery) getSession().getNamedQuery(Request.SELECT_DAILY_AGGREGATES_BY_DATE_RANGE_QUERY);
        sqlQuery.setLong("fromDate", from.getTime());
        sqlQuery.setLong("toDate", to.getTime());

        sqlQuery.addScalar("year", IntegerType.INSTANCE)
                .addScalar("month", IntegerType.INSTANCE)
                .addScalar("day", IntegerType.INSTANCE)
                .addScalar("systemName", StringType.INSTANCE)
                .addScalar("count", LongType.INSTANCE)
                .addScalar("duration", LongType.INSTANCE)
                .addScalar("avgDuration", DoubleType.INSTANCE);

        sqlQuery.setResultTransformer(Transformers.aliasToBean(RequestDuration.class));

        return sqlQuery.list();
    }

    @Override
    public RequestDuration getTotal(Date from, Date to) {
        SQLQuery sqlQuery = (SQLQuery) getSession().getNamedQuery(Request.SELECT_TOTAL_DURATION_QUERY);
        sqlQuery.setLong("fromDate", from.getTime());
        sqlQuery.setLong("toDate", to.getTime());

        sqlQuery.addScalar("count", LongType.INSTANCE)
                .addScalar("duration", LongType.INSTANCE)
                .addScalar("avgDuration", DoubleType.INSTANCE);

        sqlQuery.setResultTransformer(Transformers.aliasToBean(RequestDuration.class));

        return (RequestDuration) sqlQuery.uniqueResult();
    }
}
