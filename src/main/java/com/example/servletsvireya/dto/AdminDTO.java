package com.example.servletsvireya.dto;

import com.example.servletsvireya.model.Admin;
import com.example.servletsvireya.model.Eta;

public class AdminDTO {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private int idEta;
    private String nomeEta;

    //Construtores
    public AdminDTO() {
    }
    public AdminDTO(int id, Admin admin, int idEta, String nomeEta) {
        this.id = id;
        this.nome = admin.getNome();
        this.email = admin.getEmail();
        this.senha = admin.getSenha();
        this.idEta = idEta;
        this.nomeEta = nomeEta;
    }

    //Getters e Setters
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

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public int getIdEta() { return idEta; }
    public void setIdEta(int idEta) { this.idEta = idEta; }

    public String getNomeEta() { return nomeEta; }
    public void setNomeEta(String nomeEta) { this.nomeEta = nomeEta; }
}
