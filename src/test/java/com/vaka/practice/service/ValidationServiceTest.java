package com.vaka.practice.service;

import com.vaka.practice.domain.Entity;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationServiceTest {
    private ValidationService validationService = new SimpleValidationService();

    @DisplayName("Should pass the validation")
    @Test
    public void testShouldPassValidation() {
        Entity invalidEntity = new Entity("name", "description");
        assertDoesNotThrow(() -> validationService.validateEntity(invalidEntity));
    }

    @DisplayName("Should pass the validation #2")
    @Test
    public void testShouldPassValidation2() {
        Entity invalidEntity = new Entity("name", "");
        assertDoesNotThrow(() -> validationService.validateEntity(invalidEntity));
    }

    @DisplayName("Should throw validation exception (name size < 3)")
    @Test
    public void testShouldThrowValidationExceptionByName() {
        Entity invalidEntity = new Entity("x", "description");
        assertThrows(ValidationException.class, () -> validationService.validateEntity(invalidEntity));
    }

    @DisplayName("Should throw validation exception (name is empty)")
    @Test
    public void testShouldThrowValidationExceptionByName2() {
        Entity invalidEntity = new Entity("", "description");
        assertThrows(ValidationException.class, () -> validationService.validateEntity(invalidEntity));
    }

    @DisplayName("Should throw validation exception (description size)")
    @Test
    public void testShouldThrowValidationExceptionByDescription() {
        String invalidDescription = "x".repeat(500);
        Entity invalidEntity = new Entity("x", invalidDescription);
        assertThrows(ValidationException.class, () -> validationService.validateEntity(invalidEntity));
    }
}
