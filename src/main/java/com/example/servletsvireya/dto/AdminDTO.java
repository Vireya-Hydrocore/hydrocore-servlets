package com.example.servletsvireya.dto;

public class AdminDTO {
    private int id;
    private String nome;
    private String email;
    private int idEta;
    private String nomeEta;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdEta() {
        return idEta;
    }

    public void setIdEta(int idEta) {
        this.idEta = idEta;
    }
}
