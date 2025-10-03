package com.example.servletsvireya.dto;

import java.time.LocalDate;
import java.util.Date;

public class EstoqueDTO {
    private int id;
    private int quantidade;
    private LocalDate dataValidade;
    private Integer minPossivelEstocado;
    private int idProduto;
    private int idEta;
    private String nomeProduto;
    private String nomeEta;

    //Construtores
    public EstoqueDTO() {}
    public EstoqueDTO(int id, int quantidade, LocalDate dataValidade, Integer minPossivelEstocado,
                      int idProduto, int idEta, String nomeProduto, String nomeEta) {
        this.id = id;
        this.quantidade = quantidade;
        this.dataValidade = dataValidade;
        this.minPossivelEstocado = minPossivelEstocado;
        this.idProduto = idProduto;
        this.idEta = idEta;
        this.nomeProduto = nomeProduto;
        this.nomeEta = nomeEta;
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

    public LocalDate getDataValidade() {
        return this.dataValidade;
    }
    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public Integer getMinPossivEstocado() {
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

    public String getNomeProduto() {
        return this.nomeProduto;
    }
    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getNomeEta() { return this.nomeEta; }
    public void setNomeEta(String nomeEta) { this.nomeEta = nomeEta; }
}