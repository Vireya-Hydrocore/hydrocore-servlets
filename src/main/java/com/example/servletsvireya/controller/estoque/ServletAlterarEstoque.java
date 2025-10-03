//package com.example.servletsvireya.controller.estoque;
//
//import com.example.servletsvireya.dao.EstoqueDAO;
//import com.example.servletsvireya.model.Estoque;
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//
//import java.io.IOException;
//import java.time.LocalDate;
//
//@WebServlet(name = "ServletAlterarEstoque", value = "/ServletAlterarEstoque")
//public class ServletAlterarEstoque extends HttpServlet {
//    EstoqueDAO estoqueDAO = new EstoqueDAO();
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        //Pegar os valores do original
//        String idStr = req.getParameter("id");
//        String quantidadeStr = req.getParameter("quantidad");
//        String dataValidadeStr = req.getParameter("dataValidade");
//        String minPossivEstocadoStr = req.getParameter("minPossivEstocado");
//        String idEtaStr = req.getParameter("idEta");
//        String idProdutoStr = req. getParameter("idProduto");
//
//        //Convertendo os valores necessários
//        int id = Integer.parseInt(idStr);
//        int quantidade = Integer.parseInt(quantidadeStr);
//        LocalDate dataValidade = LocalDate.parse(dataValidadeStr);
//        int minPossivEstocado = Integer.parseInt(minPossivEstocadoStr);
//        int idEta = Integer.parseInt(idEtaStr);
//        int idProduto = Integer.parseInt(idProdutoStr);
//
//        Estoque modificado = new Estoque(id,quantidade, dataValidade, minPossivEstocado, idEta, idProduto);
//
//        //Pegando valores antes da alteração
//        Estoque original = estoqueDAO.buscarPorId(id);
//
//        //Alterando produto do sistema
//        estoqueDAO.alterarEstoque(original, modificado);
//    }
//}