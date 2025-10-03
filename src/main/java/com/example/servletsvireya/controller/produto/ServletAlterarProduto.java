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
//@WebServlet(name = "ServletAlterarProduto", value = "/servlet-alterar-produto")
//public class ServletAlterarProduto extends HttpServlet {
//    ProdutoDAO produtoDAO = new ProdutoDAO();
//
//    // GET -> mostra o formulário preenchido
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//
//        int id = Integer.parseInt(req.getParameter("id"));
//
//        Produto produto = produtoDAO.buscarPorId(id);
//
//        if (produto != null) {
//            req.setAttribute("produto", produto); //setta o atributo la no jsp
//            RequestDispatcher dispatcher = req.getRequestDispatcher("/paginasCrud/produto/alterarProduto/index.jsp");
//            dispatcher.forward(req, resp);
//        } else {
//            resp.getWriter().println("Produto não encontrado.");
//        }
//    }
//
//
//    // POST -> pega as informações depois de mudar
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        //Pegar os valores
//        String idStr = req.getParameter("id");
//        String concentracaoStr = req.getParameter("concentracao");
//
//        //Converter para inteiro e double
//        int id = Integer.parseInt(idStr);
//        double concentracao = Double.parseDouble(concentracaoStr);
//
//        //Instanciando objetos da classe model Produto
//        Produto original = produtoDAO.buscarPorId(id);
//
//        Produto modificado = new Produto();
//        modificado.setId(id);
//        modificado.setNome(req.getParameter("nome"));
//        modificado.setTipo(req.getParameter("tipo"));
//        modificado.setUnidadeMedida(req.getParameter("unidMedida"));
//        modificado.setConcentracao(concentracao);
//
//
//        //Alterando produto do sistema
//        int resultado = produtoDAO.alterar(original, modificado);
//
//        if (resultado == 1){
//            //volta pra lista
//        } else if (resultado == 0){
//            //nada foi alterado
//        } else{
//            //tela de erro
//        }
//    }
//}
