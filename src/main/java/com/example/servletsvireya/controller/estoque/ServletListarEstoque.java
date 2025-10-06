//package com.example.servletsvireya.controller.estoque;
//
//import com.example.servletsvireya.dao.EstoqueDAO;
//import com.example.servletsvireya.model.Estoque;
//import com.example.servletsvireya.model.Produto;
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//
//import java.io.IOException;
//import java.util.List;
//
//@WebServlet(name = "ServletListarEstoque", value = "/ServletListarEstoque")
//public class ServletListarEstoque extends HttpServlet {
//    EstoqueDAO estoqueDAO = new EstoqueDAO();
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        //Criando um objeto que irá receber os dados do estoque
//        List<Estoque> estoqueList = estoqueDAO.listarEstoque();
//
//        //Encaminhar lista ao documento produtoAlterar.jsp
//        req.setAttribute("estoque", estoqueList);
//        RequestDispatcher rd = req.getRequestDispatcher("produtoAlterar.jsp"); //
//        rd.forward(req, resp);
//    }
//}
