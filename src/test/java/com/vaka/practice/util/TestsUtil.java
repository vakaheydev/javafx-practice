package com.vaka.practice.util;

import com.vaka.practice.dao.JdbcUtils;

public class TestsUtil {
    private TestsUtil() {};
    public static void clearDb() {
        JdbcUtils.resetSequence("Entity");
        JdbcUtils.deleteAll("Entity");
    }
}
