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
    public String getUnidadeMedida() {
        return unidadeMedida;
    }
    public double getConcentracao() {
        return concentracao;
    }
    public int getIdEta() {return idEta;}
    public String getNomeEta() {return nomeEta;}

    //Setters
    public void setId(int id) {this.id = id;}
    public void setNome(String nome) {this.nome = nome;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public void setUnidadeMedida(String unidadeMedida) {this.unidadeMedida = unidadeMedida;}
    public void setConcentracao(double concentracao) {this.concentracao = concentracao;}
    public void setIdEta(int idEta) {this.idEta = idEta;}
    public void setNomeEta(String nomeEta) {this.nomeEta = nomeEta;}

    //toString
    public String toString(){
        return "Id: "+this.getId()+" Nome: "+this.getNome()+" Tipo: "+this.getTipo()+" Concentração: "+this.getConcentracao()
                +" Unidade de medida: "+this.getUnidadeMedida()+" Id eta: "+this.getIdEta()+" Nome da eta: "+this.getNomeEta();
    }
}