package com.vaka.practice.service;

import com.vaka.practice.dao.EntityDao;
import com.vaka.practice.domain.Entity;
import com.vaka.practice.exception.EntityNotFoundException;

public class SimpleEntityService implements EntityService {
    private final EntityDao dao;

    public SimpleEntityService(EntityDao entityDao) {
        this.dao = entityDao;
    }

    @Override
    public void create(Entity entity) {
        dao.create(entity);
    }

    @Override
    public void update(Integer id, Entity entity) throws EntityNotFoundException {
        entity.setId(id);
        dao.update(entity);
    }

    @Override
    public Entity findById(Integer id) throws EntityNotFoundException {
        return dao.findById(id);
    }

    @Override
    public void delete(Integer id) throws EntityNotFoundException {
        dao.delete(id);
    }

    @Override
    public int count() {
        return dao.count();
    }
}
