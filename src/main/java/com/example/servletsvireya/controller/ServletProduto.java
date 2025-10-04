package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.ProdutoDAO;
import com.example.servletsvireya.model.Produto;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletProduto", "/main", "/select", "/update", "/delete"}, name = "ServletProduto")
public class ServletProduto extends HttpServlet {

    ProdutoDAO produtoDAO = new ProdutoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action"); //

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar produtos (ou redirecionar)
            produtos(req, resp);
            return;
        }

        try {
            switch (action) {
                case "main": //Tela principal
                    produtos(req, resp);
                    break;
                case "select":
                    listarProduto(req, resp);
                    break;
                default:
                    //volta para a página de cadastro
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/produto/index.jsp");
            }
        } catch (Exception e) {
            System.out.println("EXCEÇÃO");
            e.printStackTrace();
        }
    }

    //doPost redireciona para o doGet (para não ficar visível na URL)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action) {
            case "create":
                inserirProduto(req, resp);
                break;
//            case "select":
//                listarProduto(req, resp);
//                break;
            case "delete":
                removerProduto(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=main");
        }
    }

    //Listar produtos
    protected void produtos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.sendRedirect("/paginasCrud/produto/produto.jsp"); //atualiza a pagina
        //Criando um objeto que irá receber os dados do produto
        List<Produto> lista = produtoDAO.listarProduto();

        //Encaminhar lista ao documento index.jsp
        req.setAttribute("produtos", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/produto/index.jsp"); //
        rd.forward(req, resp);
    }

    //Inserir Produto
    protected void inserirProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Pegar os valores
        String nome = req.getParameter("nome"); // Dá para simplificar !!!
        String tipo = req.getParameter("tipo");
        String unidadeMedida = req.getParameter("unidadeMedida");
        double concentracao = Double.parseDouble(req.getParameter("concentracao")); //Já convertido de String para double

        //Instanciando objeto da classe model Produto
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setTipo(tipo);
        produto.setUnidadeMedida(unidadeMedida);
        produto.setConcentracao(concentracao);

        //Inserindo no banco de dados
        int resultado = produtoDAO.cadastrarProduto(produto);

        if (resultado == 1) {
            //Redireciona para o GET listar produtos
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=main");
        } else {
            //manda pra pagina de erro
        }
    }

    //Editar produtos
    protected void listarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Produto produto = new Produto();
        //Recebimento do id do produto que será editado
        int id = Integer.parseInt(req.getParameter("id"));
        //Settar variavel Produto
        produto.setId(id);
        //Executar o metodo selecionarProduto()
        produtoDAO.selecionarProduto(produto);
    }


    //Remover um produto
    protected void removerProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Produto produto = new Produto();

        //Recebimento do id do produto que será removido (validador.js)
        int id = Integer.parseInt(req.getParameter("id"));
        System.out.println("delete id=" + id);
        //Settar variavel Produto
        produto.setId(id);
        //Executar o metodo removerProduto()
        int resultado = produtoDAO.removerProduto(produto);

        if(resultado == 1){
            //Redireciona para o GET listar produtos
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=main");
        } else {
            //pagina de erro
        }
    }
}