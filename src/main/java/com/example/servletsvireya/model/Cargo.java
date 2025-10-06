package com.example.servletsvireya.model;

public class Cargo {
    private int id;
    private String nome;
    private int acesso;

    //Construtores
    public Cargo(){};
    public Cargo(int id, String nome, int acesso){
        this.id = id;
        this.nome = nome;
        this.acesso = acesso;
    }

    //Getters e setters
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getNome(){
        return this.nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }

    public int getAcesso() {
        return acesso;
    }

    public void setAcesso(int acesso) {
        this.acesso = acesso;
    }
}