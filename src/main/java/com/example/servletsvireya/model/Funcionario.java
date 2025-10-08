package com.example.servletsvireya.model;
import java.util.Date;

public class Funcionario {
    private int id; // Primary Key
    private String nome;
    private String email;
    private Date dataAdmissao;
    private Date dataNascimento;
    private int idEta; // Foreign Key
    private int idCargo; // Foreign Key

    // Construtores

    public Funcionario(){}
    public Funcionario(int id, String nome, String email, Date dataAdmissao,
                       Date dataNascimento, int idEta, int idCargo){
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

    public Date getDataAdmissao() {
        return this.dataAdmissao;
    }

    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Date getDataNascimento() {
        return this.dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public int getIdEta() { return this.idEta; }
    public void setIdEta(int idEta){ this.idEta = idEta; }

    public int getIdCargo() { return this.idCargo; }
    public void setIdCargo(int idCargo){ this.idCargo = idCargo; }
}