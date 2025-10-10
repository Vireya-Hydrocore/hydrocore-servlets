package com.example.servletsvireya.dto;

public class ProdutoDTO {
    private int id;
    private String nome;
    private String tipo;              // Coagulante | Floculante | Outro
    private String unidadeMedida;
    private double concentracao;
    private int idEta; //Não tem relação com table Eta
    private String nomeEta;

    //Construtores
    public ProdutoDTO(){
    }
    public ProdutoDTO(int id, String nome, String tipo, String unidadeMedida, double concentracao){
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.unidadeMedida = unidadeMedida;
        this.concentracao = concentracao;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }
    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public double getConcentracao() {
        return concentracao;
    }
    public void setConcentracao(double concentracao) {
        this.concentracao = concentracao;
    }

    public int getIdEta() {
        return idEta;
    }
    public void setIdEta(int idEta) {
        this.idEta = idEta;
    }

    public String getNomeEta() {
        return nomeEta;
    }
    public void setNomeEta(String nomeEta) {
        this.nomeEta = nomeEta;
    }
}