package com.example.servletsvireya.model;

public class Cargo {
    //Variaveis
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

    //Getters
    public int getId(){
        return this.id;
    }
    public String getNome(){
        return this.nome;
    }
    public int getAcesso() {
        return acesso;
    }

    //Setters
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setAcesso(int acesso) {
        this.acesso = acesso;
    }

    //toString
    public String toString(){
        return "Id: "+this.getId()+" Nome: "+this.getNome()+" Acesso: "+this.getAcesso();
    }
}