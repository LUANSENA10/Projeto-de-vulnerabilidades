package com.security.security.controller;

import com.security.security.repository.model.UserInjectorModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


public class SqlInjectionDemo {
    public static void main(String[] args) {
        String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
        try (Connection conn = DriverManager.getConnection(url, "sa", "")) {
            DatabaseInjector.createSchema(conn);
            DatabaseInjector.seedData(conn);

            UserInjectControl control = new UserInjectControl();

            System.out.println("=== Busca INSEGURA ===");
            String maliciousInput = "' OR '1'='1";
            List<UserInjectorModel> insecure = control.searchUserInsecure(conn, maliciousInput);
            insecure.forEach(user -> System.out.printf("id=%d username=%s%n", user.getId(), user.getUsername()));
            if (insecure.size() > 1)
                System.out.println(">> INJECTION BEM-SUCEDIDA!");

            System.out.println("\n=== Busca SEGURA ===");
            List<UserInjectorModel> secure = control.searchUserSecure(conn, maliciousInput);
            secure.forEach(user -> System.out.printf("id=%d username=%s%n", user.getId(), user.getUsername()));
            if (secure.size() == 0)
                System.out.println(">> Protegido!");

            System.out.println("\n=== Busca SEGURA com usuário real ===");
            List<UserInjectorModel> userAlice = control.searchUserSecure(conn, "alice");
            userAlice.forEach(user -> System.out.printf("id=%d username=%s%n", user.getId(), user.getUsername()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
