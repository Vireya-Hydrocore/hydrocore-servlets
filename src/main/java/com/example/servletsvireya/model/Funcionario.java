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

    // Getters
    public int getId() { return id; }
    public String getNome() { return this.nome; }
    public String getEmail() { return this.email; }
    public Date getDataAdmissao() {
        return this.dataAdmissao;
    }
    public Date getDataNascimento() {
        return this.dataNascimento;
    }
    public int getIdEta() { return this.idEta; }
    public void setIdEta(int idEta){ this.idEta = idEta; }
    public int getIdCargo() { return this.idCargo; }

    //Setters
    public void setId (int id) { this.id = id; }
    public void setNome(String nome){ this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    public void setIdCargo(int idCargo){ this.idCargo = idCargo; }
    //toString
    public String toString(){
        return "Id: "+this.getId()+" Nome: "+this.getNome()+" Email: "+this.getEmail()+" Data de admissão: "+this.getDataAdmissao()+
                " Data de nascimento: "+this.getDataNascimento()+" ID da eta: "+this.getIdEta()+" ID do Cargo: "+this.getIdCargo();
    }
}