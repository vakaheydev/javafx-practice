package com.vaka.practice.service;

import com.vaka.practice.domain.Entity;
import jakarta.validation.*;

public class SimpleValidationService implements ValidationService {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();
    @Override
    public void validateEntity(Entity entity) {
        var violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Entity> violation : violations) {
                sb.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("\n");
            }
            throw new ValidationException(sb.toString());
        }
    }
}
