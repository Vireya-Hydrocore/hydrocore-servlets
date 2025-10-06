package com.example.servletsvireya.dto;

public class CargoDTO {
    private int id;
    private String nome;
    private Integer acesso;
    private Integer idEta;

    public Integer getIdEta() {
        return idEta;
    }

    public void setIdEta(Integer idEta) {
        this.idEta = idEta;
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

    public Integer getAcesso() {
        return acesso;
    }

    public void setAcesso(Integer acesso) {
        this.acesso = acesso;
    }
}
