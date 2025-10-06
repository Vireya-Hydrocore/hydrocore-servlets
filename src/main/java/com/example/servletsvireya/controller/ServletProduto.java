package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.ProdutoDAO;
import com.example.servletsvireya.model.Produto;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletProduto", "/mainProduto", "/selectProduto", "/updateProduto", "/deleteProduto"}, name = "ServletProduto")
public class ServletProduto extends HttpServlet {

    private ProdutoDAO produtoDAO = new ProdutoDAO();

    // GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar produtos (ou redirecionar)
            listarProdutosPorEta(req, resp);
            return;
        }

        try {
            switch (action) {
                case "main":
                    listarProdutosPorEta(req, resp);
                    break;
                case "select":
                    listarProduto(req, resp);
                    break;
                case "delete":
                    removerProduto(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/produto/produtoIndex.jsp");
            }
        } catch (Exception e) {
            System.out.println("EXCEÇÃO");
            e.printStackTrace();
        }
    }

    // POST
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action) {
            case "create":
                inserirProduto(req, resp);
                break;
            case "update":
                alterarProduto(req, resp);
                break;
            case "delete":
                removerProduto(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=main");
        }
    }

    // MÉTODOS AUXILIARES
    protected void listarProdutosPorEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Produto> lista = produtoDAO.listarProduto();
        req.setAttribute("produtos", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/produto/produtoIndex.jsp");
        rd.forward(req, resp);
    }

    protected void inserirProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Produto produto = new Produto();
        produto.setNome(req.getParameter("nome"));
        produto.setTipo(req.getParameter("tipo"));
        produto.setUnidadeMedida(req.getParameter("unidadeMedida"));
        produto.setConcentracao(Double.parseDouble(req.getParameter("concentracao")));

        int resultado = produtoDAO.cadastrarProduto(produto);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=main");
        } else {
            // Página de erro
        }
    }

    protected void listarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Produto produto = new Produto();
        produto.setId(id);
        produtoDAO.selecionarProduto(produto);

        req.setAttribute("produto", produto);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/produto/produtoAlterar.jsp");
        rd.forward(req, resp);
    }

    protected void alterarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1️⃣ Pegar o ID do produto que está sendo alterado
        int id = Integer.parseInt(req.getParameter("id"));

        // 2️⃣ Buscar o produto antigo no banco
        Produto produtoAntigo = new Produto();
        produtoAntigo.setId(id);
        produtoDAO.selecionarProduto(produtoAntigo);
        // ← aqui o DAO preenche os campos do produto antigo com os dados do banco

        // 3️⃣ Criar objeto produto novo com dados do formulário
        Produto produtoNovo = new Produto();
        produtoNovo.setId(id);
        produtoNovo.setNome(req.getParameter("nome"));
        produtoNovo.setTipo(req.getParameter("tipo"));
        produtoNovo.setUnidadeMedida(req.getParameter("unidadeMedida"));
        produtoNovo.setConcentracao(Double.parseDouble(req.getParameter("concentracao")));

        // Chamar o DAO passando os dois objetos
        int resultado = produtoDAO.alterar(produtoAntigo, produtoNovo);

        // Tratar o resultado
        if (resultado == 1) {
            req.setAttribute("alteradoSucesso", true);
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=main");
        } else {
            // Página de erro
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=main");
        }
    }

    protected void removerProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Produto produto = new Produto();
        produto.setId(id);
        System.out.println(id);

        int resultado = produtoDAO.removerProduto(produto);

        if (resultado == 1) {
            // Atualiza a lista de produtos na mesma página
            listarProdutosPorEta(req, resp);
        } else {
            // Página de erro
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }
}
