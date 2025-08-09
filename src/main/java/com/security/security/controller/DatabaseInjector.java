package com.security.security.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class DatabaseInjector {

    public static void createSchema(Connection conn) throws SQLException {
        String ddl = """
            CREATE TABLE users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(100) NOT NULL,
                password VARCHAR(100) NOT NULL
            );
        """;
        try (Statement st = conn.createStatement()) {
            st.execute(ddl);
        }
    }

    public static void seedData(Connection conn) throws SQLException {
        String insert = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insert)) {
            ps.setString(1, "alice");
            ps.setString(2, "alicepass");
            ps.executeUpdate();
            ps.setString(1, "bob");
            ps.setString(2, "bobpass");
            ps.executeUpdate();
        }
    }
}
