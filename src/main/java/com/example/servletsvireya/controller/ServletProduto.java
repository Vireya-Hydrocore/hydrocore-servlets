package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dao.ProdutoDAO;
import com.example.servletsvireya.dto.ProdutoDTO;
import com.example.servletsvireya.model.Produto;
import com.example.servletsvireya.util.Validador;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletProduto", "/mainProduto", "/createProduto", "/selectProduto", "/updateProduto", "/deleteProduto", "/filtroProduto"}, name = "ServletProduto")
public class ServletProduto extends HttpServlet {

    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private EtaDAO etaDAO = new EtaDAO(); // Instanciado no topo
    List<String> erros = new ArrayList<>();

    // Variáveis de escopo de método movidas para o topo (declaradas)
    private String action;
    private List<ProdutoDTO> lista;
    private ProdutoDTO produtoDTO;
    private RequestDispatcher rd;
    private int resultado;
    private String nomeEta;
    private int id;


    // ===============================================================
    //            Método doGet (variáveis passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        action = req.getParameter("action"); //Vem com a ação do usuário

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
                    filtrarProduto(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/assets/pages/produto/produtoIndex.jsp");
            }
        } catch (Exception e) {
            erros.add("Ocorreu um erro ao processar sua solicitação de Produto.");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //            Método doPost (passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        action = req.getParameter("action");

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar produtos (ou redirecionar)
            listarProdutos(req, resp);
            return;
        }

        try {
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
        } catch (Exception e) {
            erros.add("Erro inesperado ao processar a ação de Produto.");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ================== MÉTODOS AUXILIARES =========================


    // ===============================================================
    //                Método para LISTAR os produtos
    // ===============================================================

    protected void listarProdutos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        lista = produtoDAO.listarProdutos(); //List de objetos retornados na query

        req.setAttribute("produtos", lista); //Devolve a lista de produtos encontrados em um novo atributo

        rd = req.getRequestDispatcher("/assets/pages/produto/produtoIndex.jsp"); //Envia para a página principal
        rd.forward(req, resp);
    }


    // ===============================================================
    //                Método para INSERIR um produto
    // ===============================================================

    protected void inserirProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Criado um DTO para armazenar os valores inseridos
        produtoDTO = new ProdutoDTO();
        produtoDTO.setNome(req.getParameter("nome"));
        produtoDTO.setTipo(req.getParameter("tipo"));
        produtoDTO.setUnidadeMedida(req.getParameter("unidadeMedida"));
        produtoDTO.setConcentracao(Double.parseDouble(req.getParameter("concentracao")));

        EtaDAO etaDAO = new EtaDAO(); //Para realizar a busca do id da ETA
        int idEta= Integer.parseInt(req.getParameter("nomeEta"));
        produtoDTO.setIdEta(idEta);

        // ===== VALIDAÇÃO PRODUTO =====
        erros = Validador.validarProduto(
                produtoDTO.getNome(),
                produtoDTO.getTipo(),
                produtoDTO.getConcentracao()
        );

        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
            return; // Interrompe execução se houver erros
        }

        resultado = produtoDAO.cadastrarProduto(produtoDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto"); //Lista novamente os produtos se der certo
        } else { //Deu erro na inserção
            erros.add("ETA inexistente, verifique os campos e tente novamente!");
            req.setAttribute("erros", erros); //Setta um atributo com o erro
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp); //Vai para a página de erro
        }
    }


    // ===============================================================
    //      Método para ALTERAR o produto (com os VALORES NOVOS)
    // ================================================================

    protected void alterarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando os novos valores do produto
        produtoDTO = new ProdutoDTO();
        produtoDTO.setId(Integer.parseInt(req.getParameter("id")));
        produtoDTO.setNome(req.getParameter("nome"));
        produtoDTO.setTipo(req.getParameter("tipo"));
        produtoDTO.setUnidadeMedida(req.getParameter("unidadeMedida"));
        produtoDTO.setConcentracao(Double.parseDouble(req.getParameter("concentracao")));

        // ===== VALIDAÇÃO PRODUTO =====
        erros = Validador.validarProduto(produtoDTO.getNome(), produtoDTO.getTipo(), produtoDTO.getConcentracao());
        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
            return; // Interrompe execução se houver erros
        }

        //Chamando o produtoDAO
        resultado = produtoDAO.alterarProduto(produtoDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto");
        } else {
            erros.add("ETA inexistente, verifique os campos e tente novamente!");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ================================================================================
    //    Método para BUSCAR o produto (mostra os VALORES ANTIGOS na tela de edição)
    // ================================================================================

    protected void buscarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Setta o id no ProdutoDTO
        produtoDTO = new ProdutoDTO();
        produtoDTO.setId(Integer.parseInt(req.getParameter("id")));
        produtoDAO.buscarPorId(produtoDTO); //Busca os dados correspondente a esse id e setta no msm DTO

        req.setAttribute("produto", produtoDTO); //Setta em um novo atributo para o JSP pegar os valores

        //Envia para a página de alterar
        rd = req.getRequestDispatcher("/assets/pages/produto/produtoAlterar.jsp");
        rd.forward(req, resp);
    }


    // ===============================================================
    //         Método para REMOVER o produto (pelo ID pego)
    // ===============================================================

    protected void removerProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Pega o id para remoção
        id = Integer.parseInt(req.getParameter("id"));

        resultado = produtoDAO.removerProduto(id);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto");
        } else {
            // Página de erro
            erros.add("Não foi possível remover o produto, tente novamente.");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //        Método para FILTRAR o produto (por coluna e valor)
    // ===============================================================

    protected void filtrarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Armazena a query filtrada em um novo List
        lista = produtoDAO.filtroBuscaPorColuna(req.getParameter("nome_coluna"), req.getParameter("pesquisa"));

        req.setAttribute("produtos", lista);
        rd = req.getRequestDispatcher("/assets/pages/produto/produtoIndex.jsp");
        rd.forward(req, resp);
    }
}