package com.example.servletsvireya.dto;

public class ProdutoDTO {
    private int id;
    private String nome;
    private String tipo;              // Coagulante | Floculante | Outro
    private String unidadeMedida;

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

    private double concentracao;
}