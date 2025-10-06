package com.example.servletsvireya.dto;

import java.time.LocalDate;
import java.util.Date;

public class FuncionarioDTO {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private Date dataAdmissao;
    private Date dataNascimento;
    private int idEta; // Foreign Key
    private int idCargo; // Foreign Key
    private String nomeCargo;

    //Construtores
    public FuncionarioDTO() {
    }

    public FuncionarioDTO(int id, String nome, String email, String senha, Date dataAdmissao, Date dataNascimento, int idEta, int idCargo, String nomeCargo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataAdmissao = dataAdmissao;
        this.dataNascimento = dataNascimento;
        this.idEta = idEta;
        this.idCargo = idCargo;
        this.nomeCargo = nomeCargo;
    }

    //Getters e Setters
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataAdmissao() {
        return dataAdmissao;
    }
    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public String getNomeCargo() {
        return this.nomeCargo;
    }

    public void setNomeCargo(String nomeCargo) {
        this.nomeCargo = nomeCargo;
    }

    public int getIdEta() {
        return idEta;
    }

    public void setIdEta(int idEta) {
        this.idEta = idEta;
    }
}
