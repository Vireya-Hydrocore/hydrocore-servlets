package com.example.servletsvireya.model;

public class Admin {
    //Variáveis
    private int id;
    private String nome;
    private String senha;
    private String email;
    private int idEta;
    //Construtores
    public Admin(){}
    public Admin(String nome, String email, String senha){
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
    //Getters
    public  int getId() {return this.id;}
    public String getNome(){
        return this.nome;
    }
    public String getSenha(){
        return this.senha;
    }
    public String getEmail(){
        return this.email;
    }
    public int getIdEta(){
        return this.idEta;
    }

    //Setters
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setSenha(String senha){
        this.senha = senha;
    }

    //toString
    public String toString(){
        return "Id: "+this.getId()+" Nome: "+this.getNome()+" Senha: "+this.getSenha()+" Email: "+this.getEmail()
                +" Id da eta: "+this.getIdEta();
    }
}