package com.vaka.practice.dao;

import com.vaka.practice.domain.Entity;
import com.vaka.practice.exception.EntityNotFoundException;

public interface EntityDao {
    void create(Entity entity);
    Entity findById(Integer id) throws EntityNotFoundException;
    void update(Entity entity) throws EntityNotFoundException;
    void delete(Integer id) throws EntityNotFoundException;
    int count();
}
