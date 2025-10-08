package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.ProdutoDAO;
import com.example.servletsvireya.dto.ProdutoDTO;
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
            listarPorEta(req, resp);
            return;
        }

        try {
            switch (action) {
                case "mainProduto":
                    listarPorEta(req, resp);
                    break;
                case "selectProduto":
                    buscarProduto(req, resp);
                    break;
                case "deleteProduto":
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
            case "createProduto":
                inserirProduto(req, resp);
                break;
            case "updateProduto":
                alterarProduto(req, resp);
                break;
            case "deleteProduto":
                removerProduto(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto");
        }
    }

    // MÉTODOS AUXILIARES
    protected void listarPorEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ProdutoDTO> lista = produtoDAO.listarProdutoPorEta(1); //idEta provisório

        //Devolve a lista de produtos encontrados
        req.setAttribute("produtos", lista);

        //Envia para a página principal
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/produto/produtoIndex.jsp");
        rd.forward(req, resp);
    }


    protected void inserirProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome(req.getParameter("nome"));
        produtoDTO.setTipo(req.getParameter("tipo"));
        produtoDTO.setUnidadeMedida(req.getParameter("unidadeMedida"));
        produtoDTO.setConcentracao(Double.parseDouble(req.getParameter("concentracao")));

        int resultado = produtoDAO.cadastrarProduto(produtoDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto");
        } else {
            // Página de erro
            req.setAttribute("erro", "Não foi possível inserir o produto. Verifique os campos e tente novamente!");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    protected void buscarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Setta o id no ProdutoDTO
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(Integer.parseInt(req.getParameter("id")));
        produtoDAO.buscarPorId(produtoDTO);

        req.setAttribute("produto", produtoDTO); //Para o JSP pegar os valores

        //Envia para a página de alterar
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/produto/produtoAlterar.jsp");
        rd.forward(req, resp);
    }


    protected void alterarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando os valores do produto
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(Integer.parseInt(req.getParameter("id")));
        produtoDTO.setNome(req.getParameter("nome"));
        produtoDTO.setTipo(req.getParameter("tipo"));
        produtoDTO.setUnidadeMedida(req.getParameter("unidadeMedida"));
        produtoDTO.setConcentracao(Double.parseDouble(req.getParameter("concentracao")));

        //Chamando o produtoDAO
        int resultado = produtoDAO.alterarProduto(produtoDTO);

        if (resultado == 1) {
            //Redireciona para a listagem por ETA
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto");
        } else {
            // Página de erro
            req.setAttribute("erro", "Não foi possível alterar o produto! Verifique os campos e tente novamente.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    //REMOVER UM PRODUTO E SEU ESTOQUE
    protected void removerProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Pega o id para remoção
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(Integer.parseInt(req.getParameter("id")));

        int resultado = produtoDAO.removerProduto(produtoDTO);

        if (resultado == 1) {
            // Atualiza a lista de produtos na mesma página
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto");
        } else {
            // Página de erro
            req.setAttribute("erro", "Não foi possível remover, tente novamente mais tarde");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }
}
