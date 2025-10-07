package com.example.servletsvireya.dto;

public class EtaDTO {
    private int id; // Primary Key
    private String nome;
    private int capacidade;
    private String telefone;
    private int idEndereco;


    // Contrutores
    public EtaDTO(){}
    public EtaDTO(int id, String nome, int capacidade, String telefone, int idEndereco) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.telefone = telefone;
        this.idEndereco = idEndereco;
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

    public int getIdEndereco() { return idEndereco; }
    public void setIdEndereco(int idEndereco) { this.idEndereco = idEndereco; }
}