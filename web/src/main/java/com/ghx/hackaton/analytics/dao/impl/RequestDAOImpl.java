package com.ghx.hackaton.analytics.dao.impl;

import com.ghx.hackaton.analytics.dao.RequestDAO;
import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.util.DateUtil;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class RequestDAOImpl extends AbstractEntityDAOImpl<Request> implements RequestDAO {

    @Override
    public List<Request> find(Date from, Date to) {
        Criteria criteria = getSession().createCriteria(getClazz());
        criteria.add(Restrictions.between("year", DateUtil.year(from), DateUtil.year(to)));
        criteria.add(Restrictions.between("month", DateUtil.month(from), DateUtil.month(to)));
        criteria.add(Restrictions.between("day", DateUtil.dayOfMonth(from), DateUtil.dayOfMonth(to)));
        criteria.add(Restrictions.between("hour", DateUtil.hourOfDay(from), DateUtil.hourOfDay(to)));

        criteria.setProjection(Projections.projectionList()
                .add(Projections.groupProperty("url").as("url"))
                .add(Projections.groupProperty("serverId").as("serverId"))
                .add(Projections.groupProperty("appName").as("appName"))
                .add(Projections.groupProperty("year").as("year"))
                .add(Projections.groupProperty("month").as("month"))
                .add(Projections.groupProperty("day").as("day"))
                .add(Projections.groupProperty("hour").as("hour"))
                .add(Projections.sum("count").as("count"))
                .add(Projections.sum("duration").as("duration"))
        );

        criteria.setResultTransformer(Transformers.aliasToBean(getClazz()));

        return criteria.list();
    }

}
