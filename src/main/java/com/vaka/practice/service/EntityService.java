package com.vaka.practice.service;

import com.vaka.practice.domain.Entity;
import com.vaka.practice.exception.EntityNotFoundException;

import java.util.List;

public interface EntityService {
    void save(Entity entity);
    void update(Integer id, Entity entity) throws EntityNotFoundException;
    Entity findById(Integer id) throws EntityNotFoundException;
    List<Entity> findAll();
    void delete(Integer id) throws EntityNotFoundException;
    int count();
}
