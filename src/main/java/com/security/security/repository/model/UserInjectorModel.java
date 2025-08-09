package com.security.security.repository.model;

public class UserInjectorModel {
    private int id;
    private String username;

    public UserInjectorModel(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
}
