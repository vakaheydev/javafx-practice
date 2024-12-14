package com.vaka.practice.service;

import com.vaka.practice.domain.Entity;
import com.vaka.practice.exception.EntityNotFoundException;

public interface EntityService {
    void create(Entity entity);
    void update(Integer id, Entity entity) throws EntityNotFoundException;
    Entity findById(Integer id) throws EntityNotFoundException;
    void delete(Integer id) throws EntityNotFoundException;
    int count();
}
