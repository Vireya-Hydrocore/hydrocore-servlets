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

    // Getters
    public int getId(){ return this.id; }
    public String getNome(){ return this.nome; }
    public int getCapacidade(){ return this.capacidade; }
    public String getTelefone(){ return this.telefone; }
    public int getIdEndereco() { return idEndereco; }
    public String getCnpj() {
        return cnpj;
    }
    public String getBairro() {
        return bairro;
    }
    public String getCep() {
        return cep;
    }
    public String getRua() {return rua;}
    public String getCidade() {
        return cidade;
    }
    public String getEstado() {return estado;}
    public int getNumero() {
        return numero;
    }

    //Setters
    public void setId(int id){ this.id = id; }
    public void setNome(String nome){ this.nome = nome; }
    public void setCapacidade(int capacidade){ this.capacidade = capacidade; }
    public void setTelefone(String telefone){ this.telefone = telefone; }
    public void setIdEndereco(int idEndereco) { this.idEndereco = idEndereco; }
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    public void setCep(String cep) {
        this.cep = cep;
    }
    public void setRua(String rua) {
        this.rua = rua;
    }
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public void setNumero(int numero) {
        this.numero = numero;
    }

    //toString
    public String toString(){
        return "Id: "+this.getId()+" Nome: "+this.getNome()+" Capacidade: "+this.getCapacidade()+
                " Id do endereço: "+this.idEndereco+" Telefone: "+this.getTelefone()+" Cnpj: "+this.getCnpj()+
                " Bairro: "+this.getBairro()+" Cep: "+this.getCep()+
                " Rua: "+this.getRua()+" Cidade: "+this.getCidade()+" Estado: "+this.getEstado()+" Número: "+this.getNumero();
    }
}