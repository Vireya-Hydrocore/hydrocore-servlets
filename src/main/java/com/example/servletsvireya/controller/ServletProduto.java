package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dao.ProdutoDAO;
import com.example.servletsvireya.dto.ProdutoDTO;
import com.example.servletsvireya.util.Validador;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletProduto", "/mainProduto", "/createProduto", "/selectProduto", "/updateProduto", "/deleteProduto", "/filtroProduto"}, name = "ServletProduto")
public class ServletProduto extends HttpServlet {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final EtaDAO etaDAO = new EtaDAO();


    // ===============================================================
    //            Método doGet (variáveis passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action; //Vem com a ação do usuário

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
                    filtroProduto(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/produto/produtoIndex.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace(); //Mostra a exceção possível
            req.setAttribute("erro", "Ocorreu um erro interno ao processar a requisição.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //            Método doPost (passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action;

        action = req.getParameter("action");

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
            e.printStackTrace();
            req.setAttribute("erro", "Ocorreu um erro interno ao processar a requisição.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ================== MÉTODOS AUXILIARES =========================


    // ===============================================================
    //                Método para LISTAR os produtos
    // ===============================================================

    protected void listarProdutos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<ProdutoDTO> lista; //List de objetos retornados na query
        RequestDispatcher rd; //Envia para a página principal

        lista = produtoDAO.listarProdutos(); //List de objetos retornados na query

        req.setAttribute("produtos", lista); //Devolve a lista de produtos encontrados em um novo atributo

        rd = req.getRequestDispatcher("/paginasCrud/produto/produtoIndex.jsp"); //Envia para a página principal
        rd.forward(req, resp);
    }


    // ===============================================================
    //                Método para INSERIR um produto
    // ===============================================================

    protected void inserirProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ProdutoDTO produtoDTO; //Criado um DTO para armazenar os valores inseridos
        String nome;
        String tipo;
        String unidadeMedida;
        String concentracaoParam;
        String nomeEta; //Para realizar a busca do id da ETA
        Double concentracao;
        List<String> erros; // ===== VALIDAÇÃO PRODUTO =====
        int resultado;
        RequestDispatcher rd;

        // Parâmetros do formulário
        nome = req.getParameter("nome");
        tipo = req.getParameter("tipo");
        unidadeMedida = req.getParameter("unidadeMedida");
        concentracaoParam = req.getParameter("concentracao");
        nomeEta = req.getParameter("nomeEta");

        try {
            concentracao = Double.parseDouble(concentracaoParam);
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "A concentração deve ser um valor numérico válido.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
            return;
        }

        //Criado um DTO para armazenar os valores inseridos
        produtoDTO = new ProdutoDTO();
        produtoDTO.setNome(nome);
        produtoDTO.setTipo(tipo);
        produtoDTO.setUnidadeMedida(unidadeMedida);
        produtoDTO.setConcentracao(concentracao);

        //Para realizar a busca do id da ETA
        produtoDTO.setIdEta(etaDAO.buscarIdPorNome(nomeEta));
        produtoDTO.setNomeEta(nomeEta);

        // ===== VALIDAÇÃO PRODUTO =====
        erros = Validador.validarProduto(produtoDTO.getNome(), produtoDTO.getTipo(), produtoDTO.getConcentracao());
        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
            return; // Interrompe execução se houver erros
        }

        resultado = produtoDAO.cadastrarProduto(produtoDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto"); //Lista novamente os produtos se der certo
        } else { //Deu erro na inserção
            req.setAttribute("erro", "Não foi possível inserir o produto. Verifique os campos e tente novamente!"); //Setta um atributo com o erro
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp"); //Vai para a página de erro
            rd.forward(req, resp); //Vai para a página de erro
        }
    }


    // ===============================================================
    //      Método para ALTERAR o produto (com os VALORES NOVOS)
    // ================================================================

    protected void alterarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ProdutoDTO produtoDTO; //Settando os novos valores do produto
        String idParam;
        String nome;
        String tipo;
        String unidadeMedida;
        String concentracaoParam;
        int id;
        double concentracao;
        List<String> erros; // ===== VALIDAÇÃO PRODUTO =====
        int resultado; //Chamando o produtoDAO
        RequestDispatcher rd;

        // Parâmetros do formulário
        idParam = req.getParameter("id");
        nome = req.getParameter("nome");
        tipo = req.getParameter("tipo");
        unidadeMedida = req.getParameter("unidadeMedida");
        concentracaoParam = req.getParameter("concentracao");

        try {
            if (idParam == null || idParam.isEmpty()) {
                throw new NumberFormatException("ID do produto não fornecido.");
            }
            id = Integer.parseInt(idParam);
            concentracao = Double.parseDouble(concentracaoParam);
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID ou Concentração devem ser valores numéricos válidos.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
            return;
        }

        //Settando os novos valores do produto
        produtoDTO = new ProdutoDTO();
        produtoDTO.setId(id);
        produtoDTO.setNome(nome);
        produtoDTO.setTipo(tipo);
        produtoDTO.setUnidadeMedida(unidadeMedida);
        produtoDTO.setConcentracao(concentracao);

        // ===== VALIDAÇÃO PRODUTO =====
        erros = Validador.validarProduto(produtoDTO.getNome(), produtoDTO.getTipo(), produtoDTO.getConcentracao());
        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
            return; // Interrompe execução se houver erros
        }

        //Chamando o produtoDAO
        resultado = produtoDAO.alterarProduto(produtoDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto");
        } else {
            req.setAttribute("erro", "Não foi possível alterar o produto! Verifique os campos e tente novamente.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
        }
    }


    // ================================================================================
    //    Método para BUSCAR o produto (mostra os VALORES ANTIGOS na tela de edição)
    // ================================================================================

    protected void buscarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ProdutoDTO produtoDTO; //Setta o id no ProdutoDTO
        String idParam;
        int id;
        RequestDispatcher rd; //Envia para a página de alterar

        idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("erro", "ID do produto para busca não fornecido.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
            return;
        }

        try {
            id = Integer.parseInt(idParam);

            //Setta o id no ProdutoDTO
            produtoDTO = new ProdutoDTO();
            produtoDTO.setId(id);
            produtoDAO.buscarPorId(produtoDTO); //Busca os dados correspondente a esse id e setta no msm DTO

            if (produtoDTO.getNome() != null && !produtoDTO.getNome().isEmpty()) {
                req.setAttribute("produto", produtoDTO); //Setta em um novo atributo para o JSP pegar os valores

                //Envia para a página de alterar
                rd = req.getRequestDispatcher("/paginasCrud/produto/produtoAlterar.jsp");
                rd.forward(req, resp);
            } else {
                req.setAttribute("erro", "Produto com ID " + id + " não encontrado.");
                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
                rd.forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID inválido fornecido. Deve ser um número inteiro.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
        }
    }


    // ===============================================================
    //         Método para REMOVER o produto (pelo ID pego)
    // ===============================================================

    protected void removerProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String idParam; //Pega o id para remoção
        int id; //Pega o id para remoção
        int resultado;
        RequestDispatcher rd;

        idParam = req.getParameter("id"); //Pega o id para remoção

        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("erro", "ID do produto para remoção não fornecido.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
            return;
        }

        try {
            id = Integer.parseInt(idParam); //Pega o id para remoção

            resultado = produtoDAO.removerProduto(id);

            if (resultado == 1) {
                resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto");
            } else {
                // Página de erro
                req.setAttribute("erro", "Não foi possível remover, tente novamente mais tarde. Pode haver registros vinculados.");
                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
                rd.forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID inválido fornecido para remoção.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
        }
    }


    // ===============================================================
    //        Método para FILTRAR o produto (por coluna e valor)
    // ===============================================================

    protected void filtroProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<ProdutoDTO> lista; //Armazena a query filtrada em um novo List
        String coluna;
        String pesquisa;
        RequestDispatcher rd;

        coluna = req.getParameter("nome_coluna");
        pesquisa = req.getParameter("pesquisa");

        if (coluna == null || pesquisa == null || coluna.trim().isEmpty() || pesquisa.trim().isEmpty()) {
            listarProdutos(req, resp);
            return;
        }

        //Armazena a query filtrada em um novo List
        lista = produtoDAO.filtroBuscaPorColuna(coluna, pesquisa);

        req.setAttribute("produtos", lista);
        rd = req.getRequestDispatcher("/paginasCrud/produto/produtoIndex.jsp");
        rd.forward(req, resp);
    }
}