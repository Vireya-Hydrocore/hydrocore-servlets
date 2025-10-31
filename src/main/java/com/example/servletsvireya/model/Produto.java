package com.example.servletsvireya.model;
public class Produto {
    private int id;                // Identificador único
    private String nome;           // ex: Sulfato de Alumínio
    private String tipo;           // ex: Coagulante, Desinfetante
    private String unidade_medida; // ex: mg/L, %, etc.
    private double concentracao;   // ex: 12.5, 3.7, etc.

    //Construtores
    public Produto(int id, String nome, String tipo, String unidade_medida, double concentracao) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.unidade_medida = unidade_medida;
        this.concentracao = concentracao;
    }
    public Produto(){}

    //Getters
    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getTipo() {
        return tipo;
    }
    public double getConcentracao(){
        return concentracao;
    }
    public String getUnidadeMedida(){
        return unidade_medida;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public void setUnidadeMedida(String unidade_medida) {
        this.unidade_medida = unidade_medida;
    }
    public void setConcentracao(double concentracao) {
        this.concentracao = concentracao;
    }

    //toString
    public String toString(){
        return "Id: "+this.getId()+" Nome: "+this.getNome()+" Tipo: "+this.getTipo()+" Concentração: "+this.getConcentracao()
                +" Unidade de medida: "+this.getUnidadeMedida();
    }
}
