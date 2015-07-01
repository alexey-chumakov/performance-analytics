package com.ghx.hackaton.analytics.dao.impl;

import com.ghx.hackaton.analytics.dao.AbstractEntityDAO;
import com.ghx.hackaton.analytics.model.AbstractEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractEntityDAOImpl<T extends AbstractEntity> implements AbstractEntityDAO<T> {

    @Autowired
    private SessionFactory sessionFactory;

    private Class<T> clazz;

    @SuppressWarnings("unchecked")
    @PostConstruct
    private void init() {
        clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public T find(long id) {
        return (T) getSession().get(clazz, id);
    }

    @Override
    public void save(T entity) {
        getSession().save(entity);
    }

    @Override
    public void save(List<T> entities) {
        for (T entity : entities) {
            getSession().save(entity);
        }
    }

    @Override
    public void update(T entity) {
        getSession().update(entity);
    }

    @Override
    public void delete(T entity) {
        getSession().delete(entity);
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public Class<T> getClazz() {
        return clazz;
    }
}
