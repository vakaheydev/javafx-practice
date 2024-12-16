package com.vaka.practice.service;

import com.vaka.practice.dao.EntityDao;
import com.vaka.practice.domain.Entity;
import com.vaka.practice.exception.EntityNotFoundException;

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
    public List<Entity> findByName(String name) {
        return dao.findByName(name);
    }

    @Override
    public List<Entity> findAll() {
        return dao.findAll();
    }

    @Override
    public List<Entity> findAllWithPagination(int page, int pageSize) {
        return dao.findAllWithPagination(page, pageSize);
    }

    @Override
    public void delete(Integer id) throws EntityNotFoundException {
        dao.delete(id);
    }

    @Override
    public int count() {
        return dao.count();
    }

    @Override
    public void init() {
        List<Entity> entities = List.of(
                new Entity("Ivan", "ITHub student"),
                new Entity("Petr", "ITHub student"),
                new Entity("Nikita", "ITHub student"),
                new Entity("Aziz", "ITHub student"),
                new Entity("Petr I", "Imperator"),
                new Entity("Ekaterina II", "Imperator"),
                new Entity("Petr III", "Imperator"),
                new Entity("Ivan IV", "Tsar"),
                new Entity("Boris Godunov", "Tsar"),
                new Entity("Vasiliy III", "Tsar"),
                new Entity("Trump", "Politician"),
                new Entity("Biden", "Politician"),
                new Entity("Elon Musk", "Businessman"),
                new Entity("Mark Zuckerberg", "Businessman"),
                new Entity("Larry Page", "Businessman")
        );

        for (Entity entity : entities) {
            save(entity);
        }
    }
}
