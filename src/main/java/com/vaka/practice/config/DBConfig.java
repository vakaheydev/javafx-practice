package com.vaka.practice.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DBConfig {
    private static final String DB_RESOURCE_PATH = "/db/practice.db";

    public static String getDatabaseUrl() {
        try {
            Path tempDbPath = Files.createTempFile("practice", ".db");

            try (var inputStream = DBConfig.class.getResourceAsStream(DB_RESOURCE_PATH)) {
                if (inputStream == null) {
                    throw new RuntimeException("Database file not found: " + DB_RESOURCE_PATH);
                }
                Files.copy(inputStream, tempDbPath, StandardCopyOption.REPLACE_EXISTING);
            }

            return "jdbc:sqlite:" + tempDbPath.toAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database from JAR", e);
        }
    }
}
