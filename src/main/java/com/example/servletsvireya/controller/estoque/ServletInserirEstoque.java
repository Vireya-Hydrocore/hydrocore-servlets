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
//@WebServlet(name = "ServletInserirEstoque", value = "/servlet-inserir-estoque")
//public class ServletInserirEstoque extends HttpServlet {
//    EstoqueDAO estoqueDAO = new EstoqueDAO();
//    Estoque estoque = new Estoque();
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        //Pegando valores dos inputs
//        String quantidadeStr = req.getParameter("quantidade");
//        String dataValidadeStr = req.getParameter("dataValidade");
//        String minPossivEstocadoStr = req.getParameter("minPossivEstocado");
//        String idEtaStr = req.getParameter("idEta");
//        String idProdutoStr = req.getParameter("idProduto");
//
//        //Convertendo para os valores adequados
//        int quantidade = Integer.parseInt(quantidadeStr);
//        LocalDate dataValidade = LocalDate.parse(dataValidadeStr);
//        int minPossivEstocado = Integer.parseInt(minPossivEstocadoStr);
//        int idEta = Integer.parseInt(idEtaStr);
//        int idProduto = Integer.parseInt(idProdutoStr);
//
//        //Settando valores no objeto Estoque - tem que ser DTO???????
//        estoque.setQuantidade(quantidade);
//        estoque.setDataValidade(dataValidade);
//        estoque.setMinPossivEstocado(minPossivEstocado);
//        estoque.setIdEta(idEta);
//        estoque.setIdProduto(idProduto);
//
//        //Inserindo no estoque do banco de dados
////        int retorno = estoqueDAO.inserirEstoque(estoque);
//
////        if (retorno == 0){
////            //encaminha para pagina de erro
////        } else if (retorno == -1){
////            //é porque deu exceção
//        }
//    }
//}
