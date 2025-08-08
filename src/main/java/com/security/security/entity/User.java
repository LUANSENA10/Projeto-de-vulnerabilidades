package com.security.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    private Long id;
    private String username;
    private String password; // Texto plano - VULNERÁVEL
    
    // Getters/Setters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}