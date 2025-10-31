package com.example.servletsvireya.dto;

import com.example.servletsvireya.model.Admin;

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
    public AdminDTO(int id, String nome, String email, String senha, int idEta, String nomeEta) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.idEta = idEta;
        this.nomeEta = nomeEta;
    }

    //Getters
    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }
    public String getSenha() { return senha; }
    public int getIdEta() { return idEta; }
    public String getNomeEta() { return nomeEta; }

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setSenha(String senha) { this.senha = senha; }
    public void setIdEta(int idEta) { this.idEta = idEta; }
    public void setNomeEta(String nomeEta) { this.nomeEta = nomeEta; }

    public String toString(){
        return "Id: "+this.getId()+" Nome: "+this.getNome()+" Senha: "+this.getSenha()+" Email: "+this.getEmail()
                +" Id da eta: "+this.getIdEta()+" Nome eta: "+this.getNomeEta();
    }
}
