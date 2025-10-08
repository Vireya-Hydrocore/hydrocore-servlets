package com.example.servletsvireya.dto;
public class EtaDTO{
    private int id; // Primary Key
    private String nome;
    private int capacidade;
    private String telefone;
    private String cnpj;
    private int idEndereco; // Foreign Key
    private String bairroEndereco;
    private String cepEndereco;
    private String ruaEndereco;
    private String cidadeEndereco;
    private String estadoEndereco;
    private int numeroEndereco;


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
    public EtaDTO(int id, String nome, int capacidade, String telefone, String cnpj, int idEndereco, String bairroEndereco, String cepEndereco, String ruaEndereco, String cidadeEndereco, String estadoEndereco, int numeroEndereco) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.telefone = telefone;
        this.cnpj = cnpj;
        this.idEndereco = idEndereco;
        this.bairroEndereco = bairroEndereco;
        this.cepEndereco = cepEndereco;
        this.ruaEndereco = ruaEndereco;
        this.cidadeEndereco = cidadeEndereco;
        this.estadoEndereco = estadoEndereco;
        this.numeroEndereco = numeroEndereco;
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

    public String getBairroEndereco() {
        return bairroEndereco;
    }

    public void setBairroEndereco(String bairroEndereco) {
        this.bairroEndereco = bairroEndereco;
    }

    public String getCepEndereco() {
        return cepEndereco;
    }

    public void setCepEndereco(String cepEndereco) {
        this.cepEndereco = cepEndereco;
    }

    public String getRuaEndereco() {
        return ruaEndereco;
    }

    public void setRuaEndereco(String ruaEndereco) {
        this.ruaEndereco = ruaEndereco;
    }

    public String getCidadeEndereco() {
        return cidadeEndereco;
    }

    public void setCidadeEndereco(String cidadeEndereco) {
        this.cidadeEndereco = cidadeEndereco;
    }

    public String getEstadoEndereco() {
        return estadoEndereco;
    }

    public void setEstadoEndereco(String estadoEndereco) {
        this.estadoEndereco = estadoEndereco;
    }

    public int getNumeroEndereco() {
        return numeroEndereco;
    }

    public void setNumeroEndereco(int numeroEndereco) {
        this.numeroEndereco = numeroEndereco;
    }
}