package com.security.security.controller;

import com.security.security.repository.model.UserInjectorModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserInjectControl{

    // Busca insegura (concatena SQL, vulnerável!)
    public List<UserInjectorModel> searchUserInsecure(Connection conn, String username) {
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

    // Busca segura (PreparedStatement)
    public List<UserInjectorModel> searchUserSecure(Connection conn, String username) {
        String sql = "SELECT id, username FROM users WHERE username = ?";
        System.out.println("PreparedStatement: " + sql + " | parâmetro = " + username);
        List<UserInjectorModel> users = new ArrayList<>();
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
