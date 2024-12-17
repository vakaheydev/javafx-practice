package com.vaka.practice.service;

import com.vaka.practice.dao.JdbcEntityDao;
import com.vaka.practice.domain.Entity;
import com.vaka.practice.exception.EntityNotFoundException;
import com.vaka.practice.service.EntityService;
import com.vaka.practice.service.SimpleEntityService;
import com.vaka.practice.service.SimpleValidationService;
import com.vaka.practice.util.TestsUtil;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class EntityServiceTest {
    private EntityService service = new SimpleEntityService(new JdbcEntityDao(), new SimpleValidationService());

    @BeforeEach
    public void setup() {
        TestsUtil.clearDb();
    }

    @DisplayName("Should save and find entity with ID 1")
    @Test
    public void testShouldsaveAndFindEntity() throws EntityNotFoundException {
        Entity entity = new Entity("TestName", "TestDescription", null, null);
        service.save(entity);

        assertEquals(1, service.count());

        Entity entityById = service.findById(1);

        log.info(entityById.toString());

        assertNotNull(entityById);
        assertEquals(1, entityById.getId());
        assertEquals("TestName", entityById.getName());
        assertEquals("TestDescription", entityById.getDescription());
        assertNotNull(entityById.getCreatedAt());
        assertNotNull(entityById.getUpdatedAt());
    }

    @DisplayName("Should save, update and find entity with ID 1")
    @Test
    public void testShouldsaveUpdateFindEntity() throws EntityNotFoundException {
        Entity entity = new Entity("TestName", "TestDescription", null, null);
        service.save(entity);

        assertEquals(1, service.count());

        entity.setName("NewName");
        entity.setDescription("NewDescription");
        LocalDate savedAtTest = LocalDate.now();
        entity.setCreatedAt(savedAtTest);
        service.update(1, entity);

        Entity entityById = service.findById(1);

        log.info(entityById.toString());

        assertNotNull(entityById);
        assertEquals(1, entityById.getId());
        assertEquals("NewName", entityById.getName());
        assertEquals("NewDescription", entityById.getDescription());
        assertEquals(savedAtTest, entityById.getCreatedAt());
        assertNotNull(entityById.getUpdatedAt());
    }

    @DisplayName(("Should get all entities"))
    @Test
    public void testShouldFindAll() {
        Entity entity = new Entity("TestName", "TestDescription", null, null);
        service.save(entity);
        service.save(entity);
        service.save(entity);

        List<Entity> all = service.findAll();

        log.info(all.toString());

        assertEquals(3, all.size());
    }

    @DisplayName("Should get all with pagination")
    @Test
    public void testShouldGetAllWithPagination() {
        Entity entity = new Entity("TestName", "TestDescription", null, null);
        for (int i = 0; i < 100; i++) {
            service.save(entity);
        }

        assertEquals(100, service.count());
        assertEquals(100, service.findAll().size());

        for (int i = 1; i <= 20; i++) {
            List<Entity> allWithPagination = service.findAllWithPagination(i, 5);
            log.info("{} p. | list = {}", i, allWithPagination);
            assertEquals(5, allWithPagination.size());
            assertEquals(i * 5, allWithPagination.get(4).getId());
        }
    }

    @DisplayName("Should save and delete entity with ID 1")
    @Test
    public void testShouldsaveAndDelete() throws EntityNotFoundException {
        Entity entity = new Entity("TestName", "TestDescription", null, null);
        service.save(entity);

        assertEquals(1, service.count());

        service.delete(1);

        assertEquals(0, service.count());
        assertThrows(EntityNotFoundException.class, () -> service.findById(1));
    }

    @DisplayName("Should throw EntityNotFoundException")
    @Test
    public void testShouldThrowEntityNotFoundException() throws EntityNotFoundException {
        assertEquals(0, service.count());
        assertThrows(EntityNotFoundException.class, () -> service.findById(1));
        assertThrows(EntityNotFoundException.class, () -> service.update(1, new Entity(1, "xxx", "x", LocalDate.now(), LocalDate.now())));
        assertThrows(EntityNotFoundException.class, () -> service.delete(1));
    }

    @DisplayName("Should throw ValidationException (name size)")
    @Test
    public void testShouldThrowValidationExceptionByName() {
        Entity invalidEntity = new Entity(1, "x", "x", LocalDate.now(), LocalDate.now());
        assertEquals(0, service.count());
        assertThrows(ValidationException.class, () -> service.update(1, invalidEntity));
        assertThrows(ValidationException.class, () -> service.save(invalidEntity));
    }

    @DisplayName("Should throw ValidationException (description size)")
    @Test
    public void testShouldThrowValidationExceptionByDescription() {
        String invalidDescription = "x".repeat(500);
        Entity invalidEntity = new Entity(1, "xxx", invalidDescription, LocalDate.now(), LocalDate.now());
        assertEquals(0, service.count());
        assertThrows(ValidationException.class, () -> service.update(1, invalidEntity));
        assertThrows(ValidationException.class, () -> service.save(invalidEntity));
    }
}
