package com.example.servletsvireya.model;

public class Admin {
    private int id;
    private String nome;
    private String email;
    private int idEta;
    private String senha;
    public Admin(){}
    public Admin(String nome, String email, String senha,int idEta){
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.idEta = idEta;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setSenha(String senha){
        this.senha = senha;
    }
    public String getSenha(){
        return this.senha;
    }
    public String getNome(){
        return this.nome;
    }
    public String getEmail(){
        return this.email;
    }
    public int getIdEta(){
        return this.idEta;
    }
    public  int getId() {return this.id;}
}