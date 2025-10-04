//package com.example.servletsvireya.controller.produto;
//
//import com.example.servletsvireya.dao.ProdutoDAO;
//import com.example.servletsvireya.model.Produto;
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//
//import java.io.IOException;
//
//@WebServlet(name = "ServletRemoverProduto", value = "/servlet-remover-produto")
//public class ServletRemoverProduto extends HttpServlet {
//    ProdutoDAO produtoDAO = new ProdutoDAO();
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        //Pegar o valor
//        String idStr = req.getParameter("id");
//
//        //Converter para inteiro
//        int id = Integer.parseInt(idStr);
//
//        //Instanciando objeto da classe model Produto
//        Produto produto = new Produto();
//        produto.setId(id);
//
//        //Removendo produto do sistema
//        int resultado = produtoDAO.removerProduto(produto);
//
//        if (resultado == 1){
//            //volta pra pagina atualizada da lista
//        } else{
//            //manda pra pagina de erro
//        }
//    }
//}