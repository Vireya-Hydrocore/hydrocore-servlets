package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.EstoqueDAO;
import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dao.ProdutoDAO;
import com.example.servletsvireya.dto.EstoqueDTO;
import com.example.servletsvireya.dto.ProdutoDTO;
import com.example.servletsvireya.model.Estoque;
import com.example.servletsvireya.model.Produto;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletEstoque", "/mainEstoque", "/createEstoque", "/selectEstoque", "/updateEstoque", "/deleteEstoque", "/filtroEstoque"}, name = "ServletEstoque")
public class ServletEstoque extends HttpServlet {

    private EstoqueDAO estoqueDAO = new EstoqueDAO();


    // ===============================================================
    //             Método doGet (atributos passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action"); //Vem com a ação do usuário

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar estoques (ou redirecionar)
            listarEstoque(req, resp);
            return;
        }

        try {
            switch (action) {
                case "mainEstoque":
                    listarEstoque(req, resp);
                    break;
                case "selectEstoque":
                    buscarEstoque(req, resp);
                    break;
                case "filtroEstoque":
                    filtrarEstoque(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/estoque/estoqueIndex.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace(); //Mostra a exceção possível
        }
    }


    // ===============================================================
    //            Método doPost (atributos passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action) {
            case "createEstoque":
                inserirEstoque(req, resp);
                break;
            case "updateEstoque":
                alterarEstoque(req, resp);
                break;
            case "deleteEstoque":
                removerEstoque(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
        }
    }


    // ================== MÉTODOS AUXILIARES =========================


    // ===============================================================
    //                Método para LISTAR o estoque
    // ===============================================================

    protected void listarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<EstoqueDTO> lista = estoqueDAO.listarEstoque(); //List de objetos retornados na query

        req.setAttribute("estoques", lista); //Devolve a lista de estoques encontrados em um novo atributo

        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/estoque/estoqueIndex.jsp"); //Envia para a página principal
        rd.forward(req, resp);
    }


    // ================================================================================
    //   Método para BUSCAR um estoque (mostra os VALORES ANTIGOS na tela de edição)
    // ================================================================================

    protected void buscarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando o id no EstoqueDTO
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setId(Integer.parseInt(req.getParameter("id")));

        estoqueDAO.buscarPorId(estoqueDTO); //No mesmo objeto, setta os valores encontrados

        req.setAttribute("estoque", estoqueDTO); //Setta em um novo atributo para o JSP pegar os valores

        //Encaminhar ao documento estoqueAlterar.jsp
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/estoque/alterarEstoque.jsp");
        rd.forward(req, resp);
    }


    // ===============================================================
    //               Método para INSERIR um estoque
    // ===============================================================

    protected void inserirEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Criado um DTO para armazenar os valores inseridos
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setQuantidade(Integer.parseInt(req.getParameter("quantidade")));
        String dataStr = req.getParameter("dataValidade"); //Convertendo de String para Date
        estoqueDTO.setDataValidade(java.sql.Date.valueOf(dataStr));
        estoqueDTO.setMinPossivelEstocado(Integer.parseInt(req.getParameter("minPossivelEstocado")));

        ProdutoDAO produtoDAO = new ProdutoDAO(); //Para realizar a busca do nome do produto
        String nomeProduto = req.getParameter("nomeProduto");
        int idProduto = produtoDAO.buscarIdPorNome(nomeProduto);
        estoqueDTO.setIdProduto(idProduto);
        estoqueDTO.setNomeProduto(nomeProduto);

        EtaDAO etaDAO = new EtaDAO(); //Realizar a busca do id da ETA
        String nomeEta= req.getParameter("nomeEta");
        estoqueDTO.setIdEta(etaDAO.buscarIdPorNome(nomeEta));
        estoqueDTO.setNomeEta(nomeEta);

        int resultado = estoqueDAO.inserirEstoque(estoqueDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque"); //Lista novamente os produtos no estoque se der certo
        } else {
            req.setAttribute("erro", "Não foi possível inserir esse estoque, Verifique os campos e tente novamente!"); //Setta um atributo com o erro
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp); //Vai para a página de erro
        }
    }


    // ===============================================================
    //     Método para ALTERAR o estoque (com os VALORES NOVOS)
    // ===============================================================

    protected void alterarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando os valores no estoqueDTO ---> ---> --> usuário não pode mudar a eta
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setId(Integer.parseInt(req.getParameter("id")));
        estoqueDTO.setQuantidade(Integer.parseInt(req.getParameter("quantidade")));
        String dataStr = req.getParameter("dataValidade"); //Convertendo de String para Date
        estoqueDTO.setDataValidade(java.sql.Date.valueOf(dataStr));
        estoqueDTO.setMinPossivelEstocado(Integer.parseInt(req.getParameter("minPossivelEstocado")));

        //Chamar o DAO com o id já settado no DTO
        int resultado = estoqueDAO.alterarEstoque(estoqueDTO); //Dentro do método, é settado os atributos

        //Tratando o resultado
        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
        } else {
            req.setAttribute("erro", "Não foi possível alterar o estoque! Verifique os campos e tente novamente.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //       Método para REMOVER o estoque (pelo ID pego)
    // ===============================================================

    protected void removerEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Instanciando objeto DTO e settando o id para remoção
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setId(Integer.parseInt(req.getParameter("id")));

        int resultado = estoqueDAO.removerEstoque(estoqueDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
        } else {
            req.setAttribute("erro", "Não foi possível remover o estoque, tente novamente mais tarde.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //        Método para FILTRAR o estoque (por coluna e valor)
    // ===============================================================

    protected void filtrarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Armazena a query filtrada em um novo List
        List<EstoqueDTO> lista = estoqueDAO.filtroBuscaPorColuna(req.getParameter("nome_coluna"), req.getParameter("pesquisa"));

        req.setAttribute("estoques", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/estoque/estoqueIndex.jsp");
        rd.forward(req, resp);
    }
}