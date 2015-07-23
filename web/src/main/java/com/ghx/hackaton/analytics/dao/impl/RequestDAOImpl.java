package com.ghx.hackaton.analytics.dao.impl;

import com.ghx.hackaton.analytics.dao.RequestDAO;
import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.dto.RequestDuration;
import com.ghx.hackaton.analytics.service.pagination.Pagination;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class RequestDAOImpl extends AbstractEntityDAOImpl<Request> implements RequestDAO {

    private static final String SELECT_TOTAL_DURATION_QUERY_TEMPLATE = "SELECT " +
            "r.APP_NAME as appName, " +
            "COALESCE (SUM(r.COUNT), 0) as count, " +
            "COALESCE (SUM(r.DURATION), 0) as duration, " +
            "COALESCE (SUM(r.DURATION) / SUM(r.COUNT), 0) as avgDuration " +
            "FROM REQUEST r " +
            "WHERE :fromDate <= r.TIMESTAMP and r.TIMESTAMP <= :toDate %s " +
            "GROUP BY r.APP_NAME";

    private static final String SELECT_DAILY_AGGREGATES_BY_DATE_RANGE_QUERY_TEMPLATE =
            "(SELECT r.YEAR as year, r.MONTH as month, r.DAY as day, r.APP_NAME as appName, 'Total' as systemName, " +
            "COALESCE (SUM(r.COUNT), 0) as count, " +
            "COALESCE (SUM(r.DURATION), 0) as duration, " +
            "COALESCE (SUM(r.DURATION) / SUM(r.COUNT), 0) as avgDuration " +
            "FROM REQUEST r " +
            "WHERE :fromDate <= r.TIMESTAMP and r.TIMESTAMP <= :toDate %s " +
            "GROUP BY r.YEAR, r.MONTH, r.DAY, r.APP_NAME) " +
            "union " +
            "(SELECT r.YEAR as year, r.MONTH as month, r.DAY as day, r.APP_NAME as appName, rd.SYSTEM_NAME as systemName, " +
            "COALESCE (SUM(rd.COUNT), 0) as count, " +
            "COALESCE (SUM(rd.DURATION), 0) as duration, " +
            "COALESCE (SUM(rd.DURATION) / SUM(rd.COUNT), 0) as avgDuration " +
            "FROM REQUEST_DETAILS rd JOIN REQUEST r ON r.id = rd.REQUEST_ID " +
            "WHERE :fromDate <= r.TIMESTAMP and r.TIMESTAMP <= :toDate %s " +
            "GROUP BY r.YEAR, r.MONTH, r.DAY, r.APP_NAME, rd.SYSTEM_NAME) " +
            "ORDER BY year, month, day";

    private static final String COUNT_DISTINCT_APP_SERVER_URL_TEMPLATE = "SELECT " +
            "COUNT(*) as count FROM (SELECT * FROM REQUEST r WHERE :fromDate <= r.TIMESTAMP and r.TIMESTAMP <= :toDate %s GROUP BY r.APP_NAME, r.SERVER_ID, r.URL) sub";

    @Override
    public int updateRequest(Request request) {
        Query query = getSession().getNamedQuery(Request.UPDATE_QUERY);
        query.setLong("newCount", request.getCount());
        query.setLong("newFailedCount", request.getFailedCount());
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
    public List<Request> findAll(Date from, Date to, String appName) {
        Criteria criteria = createCriteria(from, to, appName);
        addProjection(criteria);
        criteria.setResultTransformer(Transformers.aliasToBean(Request.class));

        return criteria.list();
    }

    private void addProjection(Criteria criteria) {
        criteria.setProjection(Projections.projectionList()
                .add(Projections.property("timestamp"), "timestamp")
                .add(Projections.property("year"), "year")
                .add(Projections.property("month"), "month")
                .add(Projections.property("day"), "day")
                .add(Projections.property("hour"), "hour")
                .add(Projections.property("minute"), "minute")
                .add(Projections.property("appName"), "appName")
                .add(Projections.property("serverId"), "serverId")
                .add(Projections.property("url"), "url")
                .add(Projections.property("count"), "count")
                .add(Projections.property("failedCount"), "failedCount")
                .add(Projections.property("duration"), "duration")
                .add(Projections.sqlProjection("DURATION /COUNT AS AVG_DURATION", new String[]{"AVG_DURATION"}, new Type[]{DoubleType.INSTANCE}), "avgDuration"));
    }

    @Override
    public Long findIdByExample(Request request) {
        Example example = Example.create(request);
        example.excludeProperty("id");
        example.excludeProperty("timestamp");
        example.excludeProperty("count");
        example.excludeProperty("failedCount");
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
    public List<RequestDuration> getAggregatedByDate(Date from, Date to, String appName, String url) {
        String where = buildWhere(appName, url);
        String sql = String.format(SELECT_DAILY_AGGREGATES_BY_DATE_RANGE_QUERY_TEMPLATE, where, where);

        SQLQuery sqlQuery = getSession().createSQLQuery(sql);
        sqlQuery.setLong("fromDate", from.getTime());
        sqlQuery.setLong("toDate", to.getTime());
        if (appName != null) {
            sqlQuery.setString("appName", appName);
        }
        if (url != null) {
            sqlQuery.setString("url", url);
        }

        sqlQuery.addScalar("year", IntegerType.INSTANCE)
                .addScalar("month", IntegerType.INSTANCE)
                .addScalar("day", IntegerType.INSTANCE)
                .addScalar("appName", StringType.INSTANCE)
                .addScalar("systemName", StringType.INSTANCE)
                .addScalar("count", LongType.INSTANCE)
                .addScalar("duration", LongType.INSTANCE)
                .addScalar("avgDuration", DoubleType.INSTANCE);

        sqlQuery.setResultTransformer(Transformers.aliasToBean(RequestDuration.class));

        return sqlQuery.list();
    }

    @Override
    public List<RequestDuration> getTotalByApp(Date from, Date to, String appName, String url) {
        String sql = String.format(SELECT_TOTAL_DURATION_QUERY_TEMPLATE, buildWhere(appName, url));
        SQLQuery sqlQuery = getSession().createSQLQuery(sql);
        sqlQuery.setLong("fromDate", from.getTime());
        sqlQuery.setLong("toDate", to.getTime());
        if (appName != null) {
            sqlQuery.setString("appName", appName);
        }
        if (url != null) {
            sqlQuery.setString("url", url);
        }

        sqlQuery.addScalar("appName", StringType.INSTANCE)
                .addScalar("count", LongType.INSTANCE)
                .addScalar("duration", LongType.INSTANCE)
                .addScalar("avgDuration", DoubleType.INSTANCE);

        sqlQuery.setResultTransformer(Transformers.aliasToBean(RequestDuration.class));

        return sqlQuery.list();
    }

    private String buildWhere(String appName, String url) {
        String where = "";
        if (appName != null) {
            where += " and r.APP_NAME = :appName";
        }
        if (url != null) {
            where += " and r.URL = :url";
        }
        return where;
    }

    @Override
    public List<Request> getAggregatedByUrlSorted(Date from, Date to, String appName, com.ghx.hackaton.analytics.service.order.Order order, Pagination pagination) {
        Criteria criteria = createCriteria(from, to, appName);

        addAggregatedByUrlProjection(criteria);
        addOrder(criteria, order);
        addPagination(criteria, pagination);
        criteria.setResultTransformer(Transformers.aliasToBean(Request.class));

        return criteria.list();
    }

    private Criteria createCriteria(Date from, Date to, String appName) {
        Criteria criteria = getSession().createCriteria(Request.class)
                .add(Restrictions.ge("timestamp", from.getTime()))
                .add(Restrictions.le("timestamp", to.getTime()));

        if (appName != null) {
            criteria.add((Restrictions.eq("appName", appName)));
        }

        return criteria;
    }

    private void addOrder(Criteria criteria,  com.ghx.hackaton.analytics.service.order.Order order) {
        if (order != null) {
            criteria.addOrder(order.isAscending() ? Order.asc(order.getSortField()) : Order.desc(order.getSortField()));
        }
    }

    private void addPagination(Criteria criteria, Pagination pagination) {
        if (pagination != null) {
            criteria.setFirstResult(pagination.getFirstResult());
            criteria.setMaxResults(pagination.getItemsPerPage());
        }
    }

    private void addAggregatedByUrlProjection(Criteria criteria) {
        criteria.setProjection(Projections.projectionList()
                .add(Projections.groupProperty("appName"), "appName")
                .add(Projections.groupProperty("serverId"), "serverId")
                .add(Projections.groupProperty("url"), "url")
                .add(Projections.sum("count"), "count")
                .add(Projections.sum("duration"), "duration")
                .add(Projections.sqlProjection("sum(DURATION)/sum(COUNT) AS AVG_DURATION", new String[] { "AVG_DURATION" }, new Type[] {DoubleType.INSTANCE }), "avgDuration"));
    }

    @Override
    public List<Request> getAggregatedByDateForUrl(Date from, Date to, String url) {
        Query query = getSession().getNamedQuery(Request.SELECT_AGGREGATED_BY_DATE_FOR_URL_QUERY);
        query.setLong("fromDate", from.getTime());
        query.setLong("toDate", to.getTime());
        query.setString("url", url);

        query.setResultTransformer(Transformers.aliasToBean(Request.class));

        return query.list();
    }

    @Override
    public List<String> getAppNames() {
        return getSession()
                .createQuery("select distinct request.appName from Request request")
                .list();
    }

    @Override
    public List<String> getAppURLs(String appName) {
        return getSession()
                .createQuery("select distinct request.url from Request request where request.appName = :appName")
                .setString("appName", appName)
                .list();
    }

    @Override
    public long aggregatedByUrlCount(Date from, Date to, String appName) {
        String sql = String.format(COUNT_DISTINCT_APP_SERVER_URL_TEMPLATE, buildWhere(appName, null));
        SQLQuery sqlQuery = getSession().createSQLQuery(sql);
        sqlQuery.setLong("fromDate", from.getTime());
        sqlQuery.setLong("toDate", to.getTime());
        if (appName != null) {
            sqlQuery.setString("appName", appName);
        }

        sqlQuery.addScalar("count", LongType.INSTANCE);

        return (Long) sqlQuery.uniqueResult();
    }
}
