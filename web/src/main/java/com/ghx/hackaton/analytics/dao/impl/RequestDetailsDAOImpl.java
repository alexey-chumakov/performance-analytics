package com.ghx.hackaton.analytics.dao.impl;

import com.ghx.hackaton.analytics.dao.RequestDetailsDAO;
import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.RequestDetails;
import com.ghx.hackaton.analytics.model.dto.RequestDuration;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class RequestDetailsDAOImpl extends AbstractEntityDAOImpl<RequestDetails> implements RequestDetailsDAO {

    @Override
    public int updateRequestDetails(RequestDetails requestDetails) {
        Query query = getSession().getNamedQuery(RequestDetails.UPDATE_QUERY);
        query.setLong("newCount", requestDetails.getCount());
        query.setLong("newDuration", requestDetails.getDuration());
        query.setLong("requestId", requestDetails.getRequest().getId());
        query.setString("systemName", requestDetails.getSystemName());

        return query.executeUpdate();
    }

    @Override
    public List<RequestDetails> find(Date from, Date to, Request request) {
        Query query = getSession().getNamedQuery(RequestDetails.SELECT_AGGREGATED_BY_REQUEST_AND_DATE_RANGE_QUERY);
        query.setLong("fromDate", from.getTime());
        query.setLong("toDate", to.getTime());
        query.setString("appName", request.getAppName());
        query.setString("serverId", request.getServerId());
        query.setString("url", request.getUrl());

        query.setResultTransformer(Transformers.aliasToBean(getClazz()));

        return query.list();
    }

    @Override
    public void delete(Date from, Date to) {
        Query query = getSession().getNamedQuery(RequestDetails.DELETE_BY_REQUEST_DATE_RANGE);
        query.setLong("fromDate", from.getTime());
        query.setLong("toDate", to.getTime());

        query.executeUpdate();
    }

    @Override
    public List<RequestDuration> getTotalBySystemNames(Date from, Date to) {
        SQLQuery sqlQuery = (SQLQuery) getSession().getNamedQuery(RequestDetails.SELECT_TOTAL_DURATION_BY_DATE_RANGE_QUERY);
        sqlQuery.setLong("fromDate", from.getTime());
        sqlQuery.setLong("toDate", to.getTime());

        sqlQuery.addScalar("appName", StringType.INSTANCE)
                .addScalar("systemName", StringType.INSTANCE)
                .addScalar("count", LongType.INSTANCE)
                .addScalar("duration", LongType.INSTANCE)
                .addScalar("avgDuration", DoubleType.INSTANCE);

        sqlQuery.setResultTransformer(Transformers.aliasToBean(RequestDuration.class));

        return sqlQuery.list();
    }
}
