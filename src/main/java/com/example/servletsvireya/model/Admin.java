package com.example.servletsvireya.model;

public class Admin {
    private int id;
    private String nome;
    private String email;
    private int idEta;
    public Admin(){};
    public Admin(int id, String nome, String email, int idEta){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.idEta = idEta;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setIdEta(int idEta){
        this.idEta = idEta;
    }
    public int getId(){
        return this.id;
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
}