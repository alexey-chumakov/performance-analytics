package com.ghx.hackaton.analytics.dao;

import com.ghx.hackaton.analytics.model.AbstractEntity;

import java.util.List;

public interface AbstractEntityDAO<T extends AbstractEntity> {

    T find(long id);

    void save(T entity);

    void save(List<T> entities);

    void update(T entity);

    void delete(T entity);

}
