package com.example.servletsvireya.dto;

import java.time.LocalDate;
import java.util.Date;

public class EstoqueDTO {
    private int id;
    private int quantidade;
    private Date dataValidade;
    private Integer minPossivelEstocado;
    private int idProduto;
    private String nomeProduto;
    private int idEta;
    private String nomeEta;

    //Construtores
    public EstoqueDTO() {}
    public EstoqueDTO(int id, int quantidade, Date dataValidade, Integer minPossivelEstocado,
                      int idProduto, int idEta, String nomeProduto) {
        this.id = id;
        this.quantidade = quantidade;
        this.dataValidade = dataValidade;
        this.minPossivelEstocado = minPossivelEstocado;
        this.idProduto = idProduto;
        this.idEta = idEta;
        this.nomeProduto = nomeProduto;
    }

    //Getters e Setters
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidade() {
        return this.quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }


    public Integer getMinPossivelEstocado() {
        return this.minPossivelEstocado;
    }
    public void setMinPossivelEstocado(Integer minPossivelEstocado) {
        this.minPossivelEstocado = minPossivelEstocado;
    }

    public int getIdProduto() {
        return this.idProduto;
    }
    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getIdEta() { return this.idEta; }
    public void setIdEta(int idEta) { this.idEta = idEta; }

    public String getNomeEta() { return nomeEta; }
    public void setNomeEta(String nomeEta) { this.nomeEta = nomeEta; }

    public String getNomeProduto() {
        return this.nomeProduto;
    }
    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }
}