package com.example.servletsvireya.dto;

import java.time.LocalDate;
import java.util.Date;

public class FuncionarioDTO {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataAdmissao;
    private LocalDate dataNascimento;
    private int idEta; // Foreign Key
    private int idCargo; // Foreign Key
    private String nomeCargo;
    private String nomeEta;

    //Construtores
    public FuncionarioDTO() {
    }
    public FuncionarioDTO(int id, String nome, String email, LocalDate dataAdmissao,
                          LocalDate dataNascimento, String nomeCargo, String nomeEta) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataAdmissao = dataAdmissao;
        this.dataNascimento = dataNascimento;
        this.nomeCargo = nomeCargo;
        this.nomeEta = nomeEta;
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

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getDataAdmissao() {
        return this.dataAdmissao;
    }
    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNomeCargo() {
        return this.nomeCargo;
    }
    public void setNomeCargo(String nomeCargo) {
        this.nomeCargo = nomeCargo;
    }

    public String getNomeEta() {
        return this.nomeEta;
    }
    public void setNomeEta(String nomeEta) {
        this.nomeEta = nomeEta;
    }
}
