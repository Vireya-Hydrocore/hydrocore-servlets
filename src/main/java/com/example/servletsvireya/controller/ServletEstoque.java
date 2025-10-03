package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.EstoqueDAO;
import com.example.servletsvireya.dto.EstoqueDTO;
import com.example.servletsvireya.model.Estoque;
import com.example.servletsvireya.model.Produto;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletEstoque", "/estoque"}, name = "ServletEstoque")
public class ServletEstoque extends HttpServlet {

    EstoqueDAO estoqueDAO = new EstoqueDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            switch (action) {
                case "main": //Tela principal
                    estoques(req, resp);
//                case "create":
//                    inserirEstoque(req, resp);
//                    break;
                default:
                    //volta para a página inicial
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/estoque/index.jsp");
            }
        } catch (Exception e){
            System.out.println("Exceção");
        }
    }

    //doPost redireciona para o doGet (para não ficar visível na URL)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action) {
            case "create":
                inserirEstoque(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=main");
        }
    }

    //Listar estoque
    protected void estoques(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.sendRedirect("/paginasCrud/produto/produto.jsp"); //atualiza a pagina
        //Criando um objeto que irá receber os dados do estoque
        List<EstoqueDTO> lista = estoqueDAO.listarEstoque();

        //Encaminhar lista ao documento index.jsp
        req.setAttribute("produtos", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/estoque/index.jsp"); //
        rd.forward(req, resp);
    }

    protected void inserirEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Instanciando objeto estoqueDTO
        EstoqueDTO estoqueDTO = new EstoqueDTO();

        //Settar os valores já convertidos no DTO Estoque
        estoqueDTO.setQuantidade(Integer.parseInt(req.getParameter("quantidade")));
        estoqueDTO.setDataValidade(LocalDate.parse(req.getParameter("dataValidade"))); //Convertendo para LocalDate
        estoqueDTO.setMinPossivelEstocado(Integer.parseInt(req.getParameter("MinPossivEstocado")));
        estoqueDTO.setIdProduto(Integer.parseInt(req.getParameter("idProduto"))); //////////
        estoqueDTO.setNomeProduto(req.getParameter("nomeProduto"));
        estoqueDTO.setIdEta(Integer.parseInt(req.getParameter("idEta"))); //////////
        estoqueDTO.setNomeEta(req.getParameter("nomeEta"));

        //Inserindo no banco de dados
        int resultado = estoqueDAO.inserirEstoque(estoqueDTO);

        if (resultado == 1){
            //Redireciona para o GET listar estoque
            resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=main");
        } else{
            //manda pra pagina de erro
        }
    }
}
