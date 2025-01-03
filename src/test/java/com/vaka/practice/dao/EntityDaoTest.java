package com.vaka.practice.dao;

import com.vaka.practice.domain.Entity;
import com.vaka.practice.util.TestsUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class EntityDaoTest {
    private EntityDao entityDao = new JdbcEntityDao();

    @AfterAll
    public static void destroy() {
        TestsUtil.clearDb();
    }

    @BeforeEach
    public void setup() {
        TestsUtil.clearDb();
    }

    @DisplayName("Should create and find entity with ID 1")
    @Test
    public void testShouldCreateAndFindEntity() {
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
    public void testShouldCreateUpdateFindEntity() {
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

    @DisplayName("Should get all with pagination")
    @Test
    public void testShouldGetAllWithPagination() {
        Entity entity = new Entity("TestName", "TestDescription", null, null);
        for (int i = 0; i < 100; i++) {
            entityDao.create(entity);
        }

        assertEquals(100, entityDao.count());
        assertEquals(100, entityDao.findAll().size());

        for (int i = 1; i <= 20; i++) {
            List<Entity> allWithPagination = entityDao.findAllWithPagination(i, 5);
            log.info("{} p. | list = {}", i, allWithPagination);
            assertEquals(5, allWithPagination.size());
            assertEquals(i * 5, allWithPagination.get(4).getId());
        }
    }

    @DisplayName("Should create and delete entity with ID 1")
    @Test
    public void testShouldCreateAndDelete() {
        Entity entity = new Entity("TestName", "TestDescription", null, null);
        entityDao.create(entity);

        assertEquals(1, entityDao.count());

        entityDao.delete(1);

        assertEquals(0, entityDao.count());
        assertNull(entityDao.findById(1));
    }

    @DisplayName("Should find null (wrong ID)")
    @Test
    public void testShouldThrowEntityNotFoundException() {
        assertEquals(0, entityDao.count());
        assertNull(entityDao.findById(1));
    }
}
