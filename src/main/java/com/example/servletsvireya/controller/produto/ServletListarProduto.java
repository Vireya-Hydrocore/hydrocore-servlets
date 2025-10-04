//package com.example.servletsvireya.controller.produto;
//
//import com.example.servletsvireya.dao.ProdutoDAO;
//import com.example.servletsvireya.model.Produto;
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//
//import java.io.IOException;
//import java.util.List;
//
//@WebServlet(name = "ServletListarProduto", value = "/servlet-listar-produto")
//public class ServletListarProduto extends HttpServlet {
//    ProdutoDAO produtoDAO = new ProdutoDAO();
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        //Criando um objeto que irá receber os dados do produto
//        List<Produto> produtoList = produtoDAO.listarProduto();
//
//        //Encaminhar lista ao documento index.jsp
//        req.setAttribute("produtos", produtoList);
//        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/produto/listarProduto/index.jsp"); //
//        rd.forward(req, resp);
//    }
//}

