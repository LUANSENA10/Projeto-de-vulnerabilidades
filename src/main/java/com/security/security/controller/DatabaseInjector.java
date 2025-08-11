/* Cria a tabela "users" e insere dados de exemplo no banco em memória só para testes.*/

package com.security.security.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;


// Classe auxiliar para criar a tabela e preencher o banco "temporário" da demonstração
public class DatabaseInjector {

    // Cria a tabela chamada "users" dentro do banco H
    public static void createSchema(Connection conn) throws SQLException {
        // Comando SQL para criar tabela com três campos
        String ddl = """
            CREATE TABLE users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(100) NOT NULL,
                password VARCHAR(100) NOT NULL
            );
        """;
        // Executa o comando acima para criar a tabela
        try (Statement st = conn.createStatement()) {
            st.execute(ddl);
        }
    }
    // Insere tres usuários de teste, "saori" ,"alice", "felix", nessa tabela
    public static void seedData(Connection conn) throws SQLException {
        String insert = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insert)) {
            ps.setString(1, "saori");
            ps.setString(2, "saoripass");
            ps.executeUpdate();
            ps.setString(1, "alice");
            ps.setString(2, "alicepass");
            ps.executeUpdate();
            ps.setString(1, "felix");
            ps.setString(2, "felixpass");
            ps.executeUpdate();
        }
    }
}
