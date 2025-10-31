package com.example.servletsvireya.dto;

import java.time.LocalDate;
import java.util.Date;

public class FuncionarioDTO {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private java.sql.Date dataAdmissao;
    private java.sql.Date dataNascimento;
    private int idEta; // Foreign Key
    private int idCargo; // Foreign Key
    private String nomeCargo;
    private String nomeEta;

    //Construtores
    public FuncionarioDTO() {
    }
    public FuncionarioDTO(int id, String nome, String email, String senha, java.sql.Date dataAdmissao,
                          java.sql.Date dataNascimento, int idEta, int idCargo, String nomeCargo, String nomeEta) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataAdmissao = dataAdmissao;
        this.dataNascimento = dataNascimento;
        this.idEta = idEta;
        this.idCargo = idCargo;
        this.nomeCargo = nomeCargo;
        this.nomeEta = nomeEta;
    }

    //Getters
    public int getId() {
        return this.id;
    }
    public String getNome() {
        return this.nome;
    }
    public String getEmail() {
        return this.email;
    }
    public Date getDataAdmissao() {
        return dataAdmissao;
    }
    public Date getDataNascimento() {
        return dataNascimento;
    }
    public String getSenha() {
        return senha;
    }
    public int getIdCargo() {
        return idCargo;
    }
    public String getNomeCargo() {return this.nomeCargo;}
    public int getIdEta() {
        return idEta;
    }
    public String getNomeEta() {
        return this.nomeEta;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setDataAdmissao(java.sql.Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }
    public void setDataNascimento(java.sql.Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public void setIdCargo(int idCargo) {this.idCargo = idCargo;}
    public void setNomeCargo(String nomeCargo) {
        this.nomeCargo = nomeCargo;
    }
    public void setIdEta(int idEta) {
        this.idEta = idEta;
    }
    public void setNomeEta(String nomeEta) {
        this.nomeEta = nomeEta;
    }

    //toString
    public String toString(){
        return "Id: "+this.getId()+" Nome: "+this.getNome()+" Email: "+this.getEmail()+" Data de admissão: "+this.getDataAdmissao()+
                " Data de nascimento: "+this.getDataNascimento()+" Senha: "+this.senha+
                " ID do Cargo: "+this.getIdCargo()+" Nome do cargo: "+this.nomeCargo+
                " ID da eta: "+this.getIdEta()+"Nome da eta: "+this.getNomeEta();
    }
}
