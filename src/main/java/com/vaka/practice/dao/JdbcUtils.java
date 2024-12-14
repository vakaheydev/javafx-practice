package com.vaka.practice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtils {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/db/practice.db";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void resetSequence(String tableName) {
        execute(String.format("UPDATE sqlite_sequence SET seq = 0 WHERE name = '%s';", tableName));
    }

    public static void deleteAll(String tableName) {
        execute(String.format("DELETE FROM %s", tableName));
    }

    private static void execute(String sql) {
        try (var con = getConnection()) {
            con.prepareStatement(sql).executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
