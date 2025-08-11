/*Classe simples que representa o usuário, com atributos e métodos básicos.*/
package com.security.security.entity;

public class Userinject{
    private int id;
    private String username;
    private String password;

    public Userinject() {} // Construtor padrão

    // Define o modelo de usuário para uso com banco/outros processos
    public Userinject(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Métodos para pegar cada atributo (getters)
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    // Métodos para alterar atributo (setters)
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}
