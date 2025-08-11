/* Roda as duas consultas — uma vulnerável e outra protegida — mostrando como ocorre o ataque e como evitar.*/
package com.security.security.controller;

import com.security.security.repository.model.UserInjectorModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

// Simula a execução do teste de SQL Injection
public class SqlInjectionDemo {
    public static void main(String[] args) {
        // Usa banco H2 em memória (só existe enquanto o programa roda)
        String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
        try (Connection conn = DriverManager.getConnection(url, "sa", "")) {
            // Cria estrutura e insere dados
            DatabaseInjector.createSchema(conn);
            DatabaseInjector.seedData(conn);

            UserInjectControl control = new UserInjectControl();

            // Simula busca insegura, que é vulnerável a SQL injection
            System.out.println("=== Busca INSEGURA ===");
            String maliciousInput = "' OR '1'='1";// input que simula um ataque
            List<UserInjectorModel> insecure = control.searchUserInsecure(conn, maliciousInput);
            //Imprime cada usuário encontrado (normalmente deveria ser só 1, mas o ataque pega mais)
            insecure.forEach(user -> System.out.printf("id=%d username=%s%n", user.getId(), user.getUsername()));
            if (insecure.size() > 1)
                System.out.println(">> INJECTION BEM-SUCEDIDA!"); //indica que o ataque teve sucesso

            // Repete com a busca segura
            // tenta usar o comando SQL ("' OR '1'='1") para realizar a busca mais no modo protegido.
            System.out.println("\n=== Busca SEGURA ===");
            List<UserInjectorModel> secure = control.searchUserSecure(conn, maliciousInput);
            secure.forEach(user -> System.out.printf("id=%d username=%s%n", user.getId(), user.getUsername()));
            if (secure.size() == 0)
                System.out.println(">> Protegido!");// protege contra ataque

            // Busca por usuário real, mostrando que consulta funciona corretamente
            System.out.println("\n=== Busca SEGURA com usuário real ===");
            List<UserInjectorModel> userFelix= control.searchUserSecure(conn, "felix");
            userFelix.forEach(user -> System.out.printf("id=%d username=%s%n", user.getId(), user.getUsername()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
