package com.vaka.practice.service;

import com.vaka.practice.dao.EntityDao;
import com.vaka.practice.domain.Entity;
import com.vaka.practice.exception.EntityNotFoundException;
import jakarta.validation.ValidationException;

import java.util.List;

public class SimpleEntityService implements EntityService {
    private final EntityDao dao;
    private final ValidationService validationService;

    public SimpleEntityService(EntityDao dao, ValidationService validationService) {
        this.dao = dao;
        this.validationService = validationService;
    }

    @Override
    public void save(Entity entity) {
        validationService.validateEntity(entity);
        dao.create(entity);
    }

    @Override
    public void update(Integer id, Entity entity) throws EntityNotFoundException {
        validationService.validateEntity(entity);
        entity.setId(id);
        dao.update(entity);
    }

    @Override
    public Entity findById(Integer id) throws EntityNotFoundException {
        return dao.findById(id);
    }

    @Override
    public List<Entity> findAll() {
        return dao.findAll();
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
