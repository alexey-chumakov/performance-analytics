package com.ghx.hackaton.analytics.dao.impl;

import com.ghx.hackaton.analytics.dao.RequestDetailsDAO;
import com.ghx.hackaton.analytics.model.RequestDetails;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RequestDetailsDAOImpl extends AbstractEntityDAOImpl<RequestDetails> implements RequestDetailsDAO {

    @Override
    public List<RequestDetails> findByRequestId(long requestId) {
        Query query = getSession().getNamedQuery(RequestDetails.SELECT_BY_REQUEST_QUERY);
        query.setParameter("id", requestId);

        return query.list();
    }

}
