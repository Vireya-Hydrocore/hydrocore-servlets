package com.example.servletsvireya.dto;

import java.util.Date;

public class EstoqueDTO {
    private int id;
    private int quantidade;
    private java.sql.Date dataValidade;
    private Integer minPossivelEstocado;
    private int idProduto;
    private int idEta;
    private String nomeEta;
    private String nomeProduto;

    //Construtores
    public EstoqueDTO() {}
    public EstoqueDTO(int id, int quantidade, java.sql.Date dataValidade, Integer minPossivelEstocado,
                      int idProduto, int idEta, String nomeProduto) {
        this.id = id;
        this.quantidade = quantidade;
        this.dataValidade = dataValidade;
        this.minPossivelEstocado = minPossivelEstocado;
        this.idProduto = idProduto;
        this.idEta = idEta;
        this.nomeProduto = nomeProduto;
    }

    //Getters
    public int getId() {
        return this.id;
    }
    public int getQuantidade() {
        return this.quantidade;
    }
    public Date getDataValidade() {
        return dataValidade;
    }
    public Integer getMinPossivelEstocado() {
        return this.minPossivelEstocado;
    }
    public int getIdProduto() {
        return this.idProduto;
    }
    public int getIdEta() { return this.idEta; }
    public String getNomeEta() { return nomeEta; }
    public String getNomeProduto() {
        return this.nomeProduto;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    public void setDataValidade(java.sql.Date dataValidade) {
        this.dataValidade = dataValidade;
    }
    public void setMinPossivelEstocado(Integer minPossivelEstocado) {
        this.minPossivelEstocado = minPossivelEstocado;
    }
    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }
    public void setNomeEta(String nomeEta) { this.nomeEta = nomeEta; }
    public void setIdEta(int idEta) { this.idEta = idEta; }
    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    //toString
    public String toString(){
        return "Id: "+this.getId()+" Quantidade: "+this.getQuantidade()+" Data de validade: "+this.getDataValidade()+" Mínimo possível estocado: "
                +this.getMinPossivelEstocado()+" Id da eta: "+this.getIdEta()+" Id do produto: "+ this.getIdProduto()+ " Nome da eta: "+this.nomeEta
                +" Nome do produto: "+this.getNomeProduto();
    }
}