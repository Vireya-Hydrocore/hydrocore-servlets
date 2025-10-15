package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dao.ProdutoDAO;
import com.example.servletsvireya.dto.ProdutoDTO;
import com.example.servletsvireya.model.Produto;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletProduto", "/mainProduto", "/createProduto", "/selectProduto", "/updateProduto", "/deleteProduto", "/filtroProduto"}, name = "ServletProduto")
public class ServletProduto extends HttpServlet {

    private ProdutoDAO produtoDAO = new ProdutoDAO();


    // ===============================================================
    //            Método doGet (variáveis passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action"); //Vem com a ação do usuário

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar produtos (ou redirecionar)
            listarProdutos(req, resp);
            return;
        }

        try {
            switch (action) {
                case "mainProduto":
                    listarProdutos(req, resp);
                    break;
                case "selectProduto":
                    buscarProduto(req, resp);
                    break;
                case "filtroProduto":
                    filtroProduto(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/produto/produtoIndex.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace(); //Mostra a exceção possível
        }
    }


    // ===============================================================
    //            Método doPost (passam pelo servidor)
    // ===============================================================

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


    // ================== MÉTODOS AUXILIARES =========================


    // ===============================================================
    //                Método para LISTAR os produtos
    // ===============================================================

    protected void listarProdutos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<ProdutoDTO> lista = produtoDAO.listarProdutos(); //List de objetos retornados na query

        req.setAttribute("produtos", lista); //Devolve a lista de produtos encontrados em um novo atributo

        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/produto/produtoIndex.jsp"); //Envia para a página principal
        rd.forward(req, resp);
    }


    // ===============================================================
    //                Método para INSERIR um produto
    // ===============================================================

    protected void inserirProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Criado um DTO para armazenar os valores inseridos
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome(req.getParameter("nome"));
        produtoDTO.setTipo(req.getParameter("tipo"));
        produtoDTO.setUnidadeMedida(req.getParameter("unidadeMedida"));
        produtoDTO.setConcentracao(Double.parseDouble(req.getParameter("concentracao")));

        EtaDAO etaDAO= new EtaDAO(); //Para realizar a busca do id da ETA
        String nomeEta = req.getParameter("nomeEta");
        produtoDTO.setIdEta(etaDAO.buscarIdPorNome(nomeEta));
        produtoDTO.setNomeEta(nomeEta);

        int resultado = produtoDAO.cadastrarProduto(produtoDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto"); //Lista novamente os produtos se der certo
        } else { //Deu erro na inserção
            req.setAttribute("erro", "Não foi possível inserir o produto. Verifique os campos e tente novamente!"); //Setta um atributo com o erro
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp); //Vai para a página de erro
        }
    }


    // ================================================================================
    //    Método para BUSCAR o produto (mostra os VALORES ANTIGOS na tela de edição)
    // ================================================================================

    protected void buscarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Setta o id no ProdutoDTO
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(Integer.parseInt(req.getParameter("id")));
        produtoDAO.buscarPorId(produtoDTO); //Busca os dados correspondente a esse id e setta no msm DTO

        req.setAttribute("produto", produtoDTO); //Setta em um novo atributo para o JSP pegar os valores

        //Envia para a página de alterar
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/produto/produtoAlterar.jsp");
        rd.forward(req, resp);
    }


    // ===============================================================
    //      Método para ALTERAR o produto (com os VALORES NOVOS)
    // ================================================================

    protected void alterarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando os novos valores do produto
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(Integer.parseInt(req.getParameter("id")));
        produtoDTO.setNome(req.getParameter("nome"));
        produtoDTO.setTipo(req.getParameter("tipo"));
        produtoDTO.setUnidadeMedida(req.getParameter("unidadeMedida"));
        produtoDTO.setConcentracao(Double.parseDouble(req.getParameter("concentracao")));

        //Chamando o produtoDAO
        int resultado = produtoDAO.alterarProduto(produtoDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto");
        } else {
            req.setAttribute("erro", "Não foi possível alterar o produto! Verifique os campos e tente novamente.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //         Método para REMOVER o produto (pelo ID pego)
    // ===============================================================

    protected void removerProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Pega o id para remoção
        int id = Integer.parseInt(req.getParameter("id"));

        int resultado = produtoDAO.removerProduto(id);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto");
        } else {
            // Página de erro
            req.setAttribute("erro", "Não foi possível remover, tente novamente mais tarde");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //        Método para FILTRAR o produto (por coluna e valor)
    // ===============================================================

    protected void filtroProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Armazena a query filtrada em um novo List
        List<ProdutoDTO> lista = produtoDAO.filtroBuscaPorColuna(req.getParameter("nome_coluna"), req.getParameter("pesquisa"));

        req.setAttribute("produtos", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/produto/produtoIndex.jsp");
        rd.forward(req, resp);
    }
}