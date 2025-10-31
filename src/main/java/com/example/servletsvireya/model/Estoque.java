package com.example.servletsvireya.model;
import java.time.LocalDate;

public class Estoque {
    //Variáveis
    private int id;                  // Identificador único
    private int quantidade;          // Quantidade estocada de tal produto
    private LocalDate dataValidade; // Data de validade de tal produto
    private int minPossivEstocado; // Mínimo possível que tem que estar estocado desse produto (??)
    private int idEta;              //FK. ETA que esse estoque desse produto pertence
    private int idProduto;          //FK. Produto já cadastrado no sistema, mas não no estoque

    //Métodos Construtores
    public Estoque(int id, int quantidade, LocalDate dataValidade, int minPossivEstocado, int idEta, int idProduto) {
        this.id = id;
        this.quantidade = quantidade;
        this.dataValidade = dataValidade;
        this.minPossivEstocado = minPossivEstocado;
        this.idEta = idEta;
        this.idProduto = idProduto;
    }
    public Estoque(){
    }

    //Getters
    public int getId() {
        return id;
    }
    public int getQuantidade() {
        return this.quantidade;
    }
    public LocalDate getDataValidade() {
        return this.dataValidade;
    }
    public int getMinPossivEstocado() {
        return this.minPossivEstocado;
    }
    public int getIdEta() {
        return this.idEta;
    }
    public int getIdProduto() {
        return this.idProduto;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }
    public void setMinPossivEstocado(int minPossivEstocado) {
        this.minPossivEstocado = minPossivEstocado;
    }
    public void setIdEta(int idEta) {
        this.idEta = idEta;
    }
    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    //toString
    public String toString(){
        return "Id: "+this.getId()+" Quantidade: "+this.getQuantidade()+" Data de validade: "+this.getDataValidade()+" Mínimo possível estocado: "
                +this.getMinPossivEstocado()+" Id da eta: "+this.getIdEta()+" Id do produto: "+this.getIdProduto();
    }
}
