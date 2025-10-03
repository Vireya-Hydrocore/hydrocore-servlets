package com.example.servletsvireya.dto;

import java.time.LocalDate;

public class FuncionarioDTO {
    private int id; // Primary Key
    private String nome;
    private String email;
    private LocalDate dataAdmissao;
    private LocalDate dataNascimento;
    private int idEta; // Foreign Key
    private int idCargo; // Foreign Key

    // Construtores
    public FuncionarioDTO(){}
    public FuncionarioDTO(int id, String nome, String email, LocalDate dataAdmissao,
                       LocalDate dataNascimento, int idEta, int idCargo){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataAdmissao = dataAdmissao;
        this.dataNascimento = dataNascimento;
        this.idEta = idEta;
        this.idCargo = idCargo;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId (int id) { this.id = id; }

    public String getNome() { return this.nome; }
    public void setNome(String nome){ this.nome = nome; }

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDataAdmissao() { return this.dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao){ this.dataAdmissao = dataAdmissao; }

    public LocalDate getDataNascimento() { return this.dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento){ this.dataNascimento = dataNascimento; }

    public int getIdEta() { return this.idEta; }
    public void setIdEta(int idEta){ this.idEta = idEta; }

    public int getIdCargo() { return this.idCargo; }
    public void setIdCargo(int idCargo){ this.idCargo = idCargo; }
}