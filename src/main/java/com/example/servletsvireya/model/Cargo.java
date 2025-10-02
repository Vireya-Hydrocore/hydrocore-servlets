package com.example.servletsvireya.model;

public class Cargo {
    private int id;
    private String nome;
    private String acesso;
    public Cargo(){};
    public Cargo(int id, String nome, String acesso){
        this.id = id;
        this.nome = nome;
        this.acesso = acesso;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setAcesso(String acesso){
        this.acesso = acesso;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public String getNome(){
        return this.nome;
    }
    public String getAcesso(){
        return this.acesso;
    }
}