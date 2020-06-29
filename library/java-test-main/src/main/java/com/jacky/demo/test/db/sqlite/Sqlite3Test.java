package com.jacky.demo.test.db.sqlite;

import com.jacky.tool.util.Util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Jacky on 2020/6/22
 * <p>
 * sqlite FTS 测试
 */
public class Sqlite3Test implements Closeable {
    private static final String SCHEMA = "jdbc:sqlite:";
    private static final String WORK_DIR = "/Users/yangjianfei/myWidget/test/sqlite3";
    private static final String DB_NAME = "fts_123.db";
    private static final String DB_URL = SCHEMA + WORK_DIR + File.separator + DB_NAME;
    private static final String TABLE_MESSAGE = "message";

    private Connection connection;

    private void start() throws Exception {
        prepareDir();
        prepareConnectionSafe();
        createDb();
        createVT();
    }

    private void createVT() throws Exception {
        final Statement statement = connection.createStatement();
        /*
         * SQL error or missing database (unknown tokenizer: icu)
         * "CREATE VIRTUAL TABLE IF NOT EXISTS messages USING fts3(id, content, tokenize=icu);";
         */
        final String CREATE_MESSAGE =
            "CREATE VIRTUAL TABLE IF NOT EXISTS "
                + TABLE_MESSAGE
                + " USING fts3(id, content, tokenize=);";
        statement.execute(CREATE_MESSAGE);
    }

    private void createDb() throws Exception {
        final DatabaseMetaData metaData = connection.getMetaData();
    }

    private void prepareConnectionSafe() throws Exception {
        connection = DriverManager.getConnection(DB_URL);
    }

    private void prepareDir() {
        new File(WORK_DIR).mkdirs();
    }

    @Override
    public void close() throws IOException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final Sqlite3Test sqlite3Test = new Sqlite3Test();
        try {
            sqlite3Test.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.closeQuietly(sqlite3Test);
        }
    }
}
