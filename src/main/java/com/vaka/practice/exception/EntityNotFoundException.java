package com.vaka.practice.exception;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(Object id) {
        super(String.format("Entity with identifier %s is not found", id));
    }
}
