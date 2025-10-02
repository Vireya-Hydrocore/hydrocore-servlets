package com.example.servletsvireya.model;

public class Eta {
    private int id; // Primary Key
    private String nome;
    private int capacidade;
    private String telefone;


    // Contrutores

    public Eta(){}
    public Eta(int id, String nome, int capacidade, String telefone) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.telefone = telefone;
    }

    // Getters e Setters
    public int getId(){ return this.id; }
    public void setId(int id){ this.id = id; }

    public String getNome(){ return this.nome; }
    public void setNome(String nome){ this.nome = nome; }

    public int getCapacidade(){ return this.capacidade; }
    public void setCapacidade(int capacidade){ this.capacidade = capacidade; }

    public String getTelefone(){ return this.telefone; }
    public void setTelefone(String telefone){ this.telefone = telefone; }
}