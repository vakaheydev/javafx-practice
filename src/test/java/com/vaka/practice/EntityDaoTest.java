package com.vaka.practice;

import com.vaka.practice.dao.EntityDao;
import com.vaka.practice.dao.JdbcEntityDao;
import com.vaka.practice.dao.JdbcUtils;
import com.vaka.practice.domain.Entity;
import com.vaka.practice.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class EntityDaoTest {
    private EntityDao entityDao = new JdbcEntityDao();
    private static final String TABLE_NAME = "Entity";

    @BeforeEach
    public void setup() {
        JdbcUtils.resetSequence(TABLE_NAME);
        JdbcUtils.deleteAll(TABLE_NAME);
    }

    @DisplayName("Should create and find entity with ID 1")
    @Test
    public void testShouldCreateAndFindEntity() throws EntityNotFoundException {
        Entity entity = new Entity("TestName", "TestDescription", null, null);
        entityDao.create(entity);

        assertEquals(1, entityDao.count());

        Entity entityById = entityDao.findById(1);

        log.info(entityById.toString());

        assertNotNull(entityById);
        assertEquals(1, entityById.getId());
        assertEquals("TestName", entityById.getName());
        assertEquals("TestDescription", entityById.getDescription());
        assertNotNull(entityById.getCreatedAt());
        assertNotNull(entityById.getUpdatedAt());
    }

    @DisplayName("Should create, update and find entity with ID 1")
    @Test
    public void testShouldCreateUpdateFindEntity() throws EntityNotFoundException {
        Entity entity = new Entity("TestName", "TestDescription", null, null);
        entityDao.create(entity);

        assertEquals(1, entityDao.count());

        entity.setId(1);
        entity.setName("NewName");
        entity.setDescription("NewDescription");
        LocalDate createdAtTest = LocalDate.now();
        entity.setCreatedAt(createdAtTest);
        entityDao.update(entity);

        Entity entityById = entityDao.findById(1);

        log.info(entityById.toString());

        assertNotNull(entityById);
        assertEquals(1, entityById.getId());
        assertEquals("NewName", entityById.getName());
        assertEquals("NewDescription", entityById.getDescription());
        assertEquals(createdAtTest, entityById.getCreatedAt());
        assertNotNull(entityById.getUpdatedAt());
    }

    @DisplayName(("Should get all entities"))
    @Test
    public void testShouldFindAll() {
        Entity entity = new Entity("TestName", "TestDescription", null, null);
        entityDao.create(entity);
        entityDao.create(entity);
        entityDao.create(entity);

        List<Entity> all = entityDao.findAll();

        log.info(all.toString());

        assertEquals(3, all.size());
    }

    @DisplayName("Should create and delete entity with ID 1")
    @Test
    public void testShouldCreateAndDelete() throws EntityNotFoundException {
        Entity entity = new Entity("TestName", "TestDescription", null, null);
        entityDao.create(entity);

        assertEquals(1, entityDao.count());

        entityDao.delete(1);

        assertEquals(0, entityDao.count());
        assertThrows(EntityNotFoundException.class, () -> entityDao.findById(1));
    }

    @DisplayName("Should throw EntityNotFoundException")
    @Test
    public void testShouldThrowEntityNotFoundException() throws EntityNotFoundException {
        assertEquals(0, entityDao.count());
        assertThrows(EntityNotFoundException.class, () -> entityDao.findById(1));
        assertThrows(EntityNotFoundException.class, () -> entityDao.update(new Entity(1, "x", "x", LocalDate.now(),
                LocalDate.now()       )));
        assertThrows(EntityNotFoundException.class, () -> entityDao.delete(1));
    }
}
