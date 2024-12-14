package com.vaka.practice.exception;

import java.sql.SQLException;

public class ConstraintError extends SQLException {
    public ConstraintError(Throwable cause) {
        super(cause);
    }
}
