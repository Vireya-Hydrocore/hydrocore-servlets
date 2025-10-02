package com.example.servletsvireya.controller.estoque;

import com.example.servletsvireya.dao.EstoqueDAO;
import com.example.servletsvireya.model.Estoque;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet(name = "ServletInserirEstoque", value = "/servlet-inserir-estoque")
public class ServletInserirEstoque extends HttpServlet {
    EstoqueDAO estoqueDAO = new EstoqueDAO();
    Estoque estoque = new Estoque();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Pegando valores dos inputs
        String quantidadeStr = req.getParameter("quantidade");
        String dataValidadeStr = req.getParameter("dataValidade");
        String minPossivEstocadoStr = req.getParameter("minPossivEstocado");
        String idEtaStr = req.getParameter("idEta");
        String idProdutoStr = req.getParameter("idProduto");

        //Convertendo para os valores adequados
        int quantidade = Integer.parseInt(quantidadeStr);
        LocalDate dataValidade = LocalDate.parse(dataValidadeStr); //ta certo??
        int minPossivEstocado = Integer.parseInt(minPossivEstocadoStr);
        int idEta = Integer.parseInt(idEtaStr);
        int idProduto = Integer.parseInt(idProdutoStr);

        //Instanciando objeto model Estoque
        estoque.setQuantidade(quantidade);
        estoque.setData_validade(dataValidade);
        estoque.setMin_possiv_estocado(minPossivEstocado);
        estoque.setId_eta(idEta);
        estoque.setId_produto(idProduto);

        //Inserindo no estoque do banco de dados
        estoqueDAO.inserirEmEstoque(estoque);

        //Não precisa responder nada
    }
}
