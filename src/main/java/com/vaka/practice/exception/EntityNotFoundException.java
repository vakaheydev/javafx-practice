package com.vaka.practice.exception;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(Integer id) {
        super(String.format("Entity with ID %d is not found", id));
    }
}
