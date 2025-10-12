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

@WebServlet(urlPatterns = {"/ServletEstoque", "/mainEstoque", "/createEstoque", "/selectEstoque", "/updateEstoque", "/deleteEstoque"}, name = "ServletEstoque")
public class ServletEstoque extends HttpServlet {

    private EstoqueDAO estoqueDAO = new EstoqueDAO();


    // ===============================================================
    //                       Método doGet
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        System.out.println(action);

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar estoques (ou redirecionar)
            listarEstoquePorEta(req, resp); //mudar para listarEstoque Por Eta
            return;
        }

        try {
            switch (action) {
                case "mainEstoque":
                    listarEstoquePorEta(req, resp);
                    break;
                case "selectEstoque":
                    buscarEstoque(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/estoque/estoqueIndex.jsp");
            }
        } catch (Exception e) {
            System.out.println("EXCEÇÃO");
            e.printStackTrace();
        }
    }


    // ===============================================================
    //                       Método doPost
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

    protected void listarEstoquePorEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<EstoqueDTO> lista = estoqueDAO.listarEstoque();

        req.setAttribute("estoques", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/estoque/estoqueIndex.jsp");
        rd.forward(req, resp);
    }


    // ===============================================================
    //               Método para INSERIR o estoque
    // ===============================================================

    protected void inserirEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //Instanciando objeto DTO
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        EtaDAO etaDao = new EtaDAO();

        estoqueDTO.setQuantidade(Integer.parseInt(req.getParameter("quantidade")));
        String dataStr = req.getParameter("dataValidade"); //Convertendo de String para Date
        estoqueDTO.setDataValidade(java.sql.Date.valueOf(dataStr));
        estoqueDTO.setMinPossivelEstocado(Integer.parseInt(req.getParameter("minPossivelEstocado")));
        String nomeProduto=req.getParameter("nomeProduto");
        estoqueDTO.setNomeProduto(nomeProduto);
        ProdutoDAO produtdao= new ProdutoDAO();
        int idProduto= produtdao.buscarIdPorNome(nomeProduto);

        String nomeEta= req.getParameter("nomeEta");
        estoqueDTO.setIdEta(etaDao.buscarIdPorNome(nomeEta));
        estoqueDTO.setNomeEta(nomeEta);


        estoqueDTO.setIdProduto(idProduto); //para teste

        int resultado = estoqueDAO.inserirEstoque(estoqueDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
        } else {
            // Página de erro
            req.setAttribute("erro", "Não foi possível inserir esse estoque");
            req.getRequestDispatcher("/paginasCrud/erro.jsp");
        }
    }


    // ================================================================================
    //   Método para BUSCAR o estoque (mostra os VALORES ANTIGOS na tela de edição)
    // ================================================================================

    protected void buscarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Recebimento do id do estoque que será editado
        int id = Integer.parseInt(req.getParameter("id"));

        //Setar a variavel Estoque
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setId(id);

        //executar o metodo buscarPorId
        estoqueDAO.buscarPorId(estoqueDTO); //No mesmo objeto, setta os valores encontrados

        //Setar os atributos do formulário no estoqueDTO
        req.setAttribute("id", estoqueDTO.getId());
        req.setAttribute("quantidade", estoqueDTO.getQuantidade());
        req.setAttribute("dataValidade", estoqueDTO.getDataValidade());
        req.setAttribute("minPossivelEstocado", estoqueDTO.getMinPossivelEstocado());

        //Encaminhar ao documento alterarEstoque.jsp
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/estoque/alterarEstoque.jsp");
        rd.forward(req, resp);
    }


    // ===============================================================
    //     Método para ALTERAR o estoque (com os VALORES NOVOS)
    // ===============================================================

    protected void alterarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Settando os valores no estoqueDTO ---> ---> --> usuário não pode mudar a eta
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setId(Integer.parseInt(req.getParameter("id")));
        estoqueDTO.setQuantidade(Integer.parseInt(req.getParameter("quantidade")));
        String dataStr = req.getParameter("dataValidade"); //Convertendo de String para Date
        estoqueDTO.setDataValidade(java.sql.Date.valueOf(dataStr));
        estoqueDTO.setMinPossivelEstocado(Integer.parseInt(req.getParameter("minPossivelEstocado")));

        // Chamar o DAO com o id já settado no DTO
        int resultado = estoqueDAO.alterarEstoque(estoqueDTO); //Dentro do método, é settado os atributos

        // Tratar o resultado
        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
        }
        else { // Página de erro
            //Settando atributo que será pego no JSP
            req.setAttribute("erro", "Não foi possível alterar o estoque! Verifique os campos e tente novamente.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //       Método para REMOVER o estoque (pelo ID pego)
    // ===============================================================

    protected void removerEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //Instanciando objeto DTO
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setId(Integer.parseInt(req.getParameter("id")));

        int resultado = estoqueDAO.removerEstoque(estoqueDTO);

        if (resultado == 1) {
            // Atualiza a lista de produtos na mesma página
            listarEstoquePorEta(req, resp);
        } else {
            // Página de erro
            //Settando atributo que será pego no JSP
            req.setAttribute("erro", "Não foi possível remover o estoque! Tente novamente.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }
}