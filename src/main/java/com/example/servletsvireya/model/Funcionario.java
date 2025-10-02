package com.example.servletsvireya.model;
import java.time.LocalDate;

public class Funcionario {
    private int id; // Primary Key
    private String nome;
    private String email;
    private LocalDate dataAdmissao;
    private LocalDate dataNascimento;
    private int idETA; // Foreign Key
    private int idCargo; // Foreign Key

    // Construtores
    public Funcionario(){}
    public Funcionario(Funcionario funcionario){
        this.id = funcionario.getId();
        this.nome = funcionario.getNome();
        this.email = funcionario.getEmail();
        this.dataAdmissao = funcionario.getDataAdmissao();
        this.dataNascimento = funcionario.getDataNascimento();
        this.idETA = funcionario.getIdETA();
        this.idCargo = funcionario.getIdCargo();
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return this.nome; }
    public String getEmail() { return this.email; }
    public LocalDate getDataAdmissao() { return this.dataAdmissao; }
    public LocalDate getDataNascimento() { return this.dataNascimento; }
    public int getIdETA() { return this.idETA; }
    public int getIdCargo() { return this.idCargo; }

    public void setId (int id) { this.id = id; }
    public void setNome(String nome){ this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setDataAdmissao(LocalDate dataAdmissao){ this.dataAdmissao = dataAdmissao; }
    public void setDataNascimento(LocalDate dataNascimento){ this.dataNascimento = dataNascimento; }
    public void setIdEta(int idETA){ this.idETA = idETA; }
    public void setIdCargo(int idCargo){ this.idCargo = idCargo; }

}