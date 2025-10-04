//package com.example.servletsvireya.controller.produto;
//
//import com.example.servletsvireya.dao.ProdutoDAO;
//import com.example.servletsvireya.model.Produto;
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//@WebServlet(name = "ServletCadastrarProduto", value = "/servlet-cadastrar-produto")
//public class ServletCadastrarProduto extends HttpServlet {
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        ProdutoDAO produtoDAO = new ProdutoDAO();
//
//        //Pegar os valores
//        String nome = req.getParameter("nome").toUpperCase(); // Dá para simplificar !!!
//        String tipo = req.getParameter("tipo").toUpperCase();
//        String unidMedida = req.getParameter("unidMedida").toUpperCase();
//        double concentracao = Double.parseDouble(req.getParameter("concentracao")); //Já convertido de String para double
//
//        //Instanciando objeto da classe model Produto
//        Produto produto = new Produto();
//        produto.setNome(nome);
//        produto.setTipo(tipo);
//        produto.setUnidadeMedida(unidMedida);
//        produto.setConcentracao(concentracao);
//
//        //Inserindo no banco de dados
//        int resultado = produtoDAO.cadastrarProduto(produto);
//
//        if (resultado == 1){
//            //atualiza a pagina com o novo produto inserido
//        } else{
//            //manda pra pagina de erro
//        }
//    }
//}
