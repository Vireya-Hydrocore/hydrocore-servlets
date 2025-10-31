package com.example.servletsvireya.dto;

public class CargoDTO {
    private int id;
    private String nome;
    private Integer acesso;
    private Integer idEta;
    private String nomeEta;
    //Contrutores
    public CargoDTO() {}
    public CargoDTO(int id, String nome, Integer acesso, Integer idEta, String nomeEta) {
        this.id = id;
        this.nome = nome;
        this.acesso = acesso;
        this.idEta = idEta;
        this.nomeEta = nomeEta;
    }

    //Gettters
    public Integer getIdEta() {
        return idEta;
    }
    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public Integer getAcesso() {
        return acesso;
    }
    public String getNomeEta() {
        return nomeEta;
    }

    //Setters
    public void setIdEta(Integer idEta) {
        this.idEta = idEta;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setAcesso(Integer acesso) {
        this.acesso = acesso;
    }
    public void setNomeEta(String nomeEta) {
        this.nomeEta = nomeEta;
    }

    //toString
    public String toString(){
        return "Id: "+this.getId()+" Nome: "+this.getNome()+" Acesso: "+this.getAcesso()+
                " IdEta: "+this.getIdEta()+" NomeEta: "+this.getNomeEta();
    }
}
