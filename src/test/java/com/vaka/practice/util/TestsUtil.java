package com.vaka.practice.util;

public class TestsUtil {
    private TestsUtil() {
    }

    ;

    public static void clearDb() {
        JdbcUtils.resetSequence("Entity");
        JdbcUtils.deleteAll("Entity");
    }
}
