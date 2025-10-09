package com.example.servletsvireya.dto;

public class EtaDTO{
    private int id; // Primary Key
    private String nome;
    private int capacidade;
    private String telefone;
    private int idEndereco;
    private String cnpj;
    private String bairro;
    private String cep;
    private String rua;
    private String cidade;
    private String estado;
    private int numero;



    // Contrutores
    public EtaDTO(){}
    public EtaDTO(int id, String nome, int capacidade, String telefone, int idEndereco, String cnpj) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.telefone = telefone;
        this.idEndereco = idEndereco;
        this.cnpj=cnpj;
    }

    public EtaDTO(int id, String nome, int capacidade, String telefone, int idEndereco, String cnpj, String bairro, String cep, String rua, String cidade, String estado, int numero) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.telefone = telefone;
        this.idEndereco = idEndereco;
        this.cnpj = cnpj;
        this.bairro = bairro;
        this.cep = cep;
        this.rua = rua;
        this.cidade = cidade;
        this.estado = estado;
        this.numero = numero;
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}