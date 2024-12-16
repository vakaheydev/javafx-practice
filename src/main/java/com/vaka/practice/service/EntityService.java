package com.vaka.practice.service;

import com.vaka.practice.domain.Entity;
import com.vaka.practice.exception.EntityNotFoundException;

import java.util.List;

public interface EntityService {
    void save(Entity entity);

    void update(Integer id, Entity entity) throws EntityNotFoundException;

    Entity findById(Integer id) throws EntityNotFoundException;

    boolean existsById(Integer id);

    List<Entity> findByName(String name);

    List<Entity> findAll();

    List<Entity> findAllWithPagination(int page, int pageSize);

    void delete(Integer id) throws EntityNotFoundException;

    int count();

    void init();
}
