package com.example.servletsvireya.model;

public class Eta {
    private int id; // Primary Key
    private String nome;
    private int capacidade;
    private String telefone;

    private String cnpj;


    // Contrutores

    public Eta(){}
    public Eta(String nome, int capacidade, String telefone, String cnpj){
        this.nome = nome;
        this.capacidade = capacidade;
        this.telefone = telefone;
        this.cnpj = cnpj;
    }

    // Getters e Setters
    public int getId(){ return this.id; }
    public String getNome(){ return this.nome; }
    public int getCapacidade(){ return this.capacidade; }
    public String getTelefone(){ return this.telefone; }
    public String getCnpj(){return  this.cnpj;}
    public int getCapacidade(){ return this.capacidade; }
    public String getTelefone(){ return this.telefone; }
    
  
    public void setId(int id){ this.id = id; }
    public void setNome(String nome){ this.nome = nome; }
    public void setCnpj(String cnpj){this.cnpj = cnpj; }
    public void setCapacidade(int capacidade){ this.capacidade = capacidade; }
    public void setTelefone(String telefone){ this.telefone = telefone; }
}