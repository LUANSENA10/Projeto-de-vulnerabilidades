/*Implementa a lógica das buscas, destacando diferença entre código seguro (PreparedStatement) e inseguro (concatenação) contra SQL Injection.*/
package com.security.security.controller;

import com.security.security.repository.model.UserInjectorModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Classe que executa buscas usando SQL — tanto vulnerável como segura
public class UserInjectControl{

    // Busca insegura: concatena valor do usuário direto na SQL — isso permite ataque!
    public List<UserInjectorModel> searchUserInsecure(Connection conn, String username) {
        //pega o texto que o usuario digitou, e coloca dentro da fase do SQL
        //inseguro porque o usuario pode digitar algum comando do banco, e assim retornar todos usuarios.
        String sql = "SELECT id, username FROM users WHERE username = '" + username + "'";
        System.out.println("SQL gerado (inseguro): " + sql);
        List<UserInjectorModel> users = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new UserInjectorModel(rs.getInt("id"), rs.getString("username")));
            }
        } catch (SQLException e) {
            System.out.println("Erro na consulta insegura: " + e.getMessage());
        }
        return users;
    }

    // Busca segura: usa PreparedStatement — evita que comandos maliciosos sejam executados
    public List<UserInjectorModel> searchUserSecure(Connection conn, String username) {
        String sql = "SELECT id, username FROM users WHERE username = ?";
        System.out.println("PreparedStatement: " + sql + " | parâmetro = " + username);
        List<UserInjectorModel> users = new ArrayList<>();
        //PreparedStatement : separa dados de comandos, não permitindo que o usuario atribua um comando SQL que quebre a segurança
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(new UserInjectorModel(rs.getInt("id"), rs.getString("username")));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro na consulta segura: " + e.getMessage());
        }
        return users;
    }
}
