package com.vaka.practice.dao;

import com.vaka.practice.domain.Entity;
import com.vaka.practice.exception.EntityNotFoundException;

import java.util.List;

public interface EntityDao {
    void create(Entity entity);
    Entity findById(Integer id);

    List<Entity> findByName(String name);

    List<Entity> findAll();

    List<Entity> findAllWithPagination(int page, int pageSize);

    void update(Entity entity);

    void delete(Integer id);

    int count();
}
