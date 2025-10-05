package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.EstoqueDAO;
import com.example.servletsvireya.dao.ProdutoDAO;
import com.example.servletsvireya.dto.EstoqueDTO;
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

    // GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        System.out.println(action);

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar produtos (ou redirecionar)
//            estoques(req, resp);
            return;
        }

        try {
            switch (action) {
                case "mainEstoque":
                    estoques(req, resp);
                    break;
                case "selectEstoque":
                    listarEstoque(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/estoque/index.jsp");
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

    // MÉTODOS AUXILIARES
    //LISTA POR ETA
    protected void estoques(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ⚠ Por enquanto, fixo para testes
        int idEta = 1;

        // ✅ No futuro, isso aqui pega da sessão:
        // HttpSession session = req.getSession();
        // AdminDTO adminLogado = (AdminDTO) session.getAttribute("adminLogado");
        // int idEta = adminLogado.getIdEta();

        List<EstoqueDTO> lista = estoqueDAO.listarEstoquePorEta(idEta);
        System.out.println(lista);

        req.setAttribute("estoques", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/estoque/index.jsp");
        rd.forward(req, resp);
    }

    protected void inserirEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EstoqueDTO estoqueDTO = new EstoqueDTO();

        estoqueDTO.setQuantidade(Integer.parseInt(req.getParameter("quantidade")));
        //Convertendo de String para Date
        String dataStr = req.getParameter("dataValidade");
        estoqueDTO.setDataValidade(java.sql.Date.valueOf(dataStr));
        estoqueDTO.setMinPossivelEstocado(Integer.parseInt(req.getParameter("minPossivelEstocado")));
        estoqueDTO.setNomeProduto("nomeProduto");
        estoqueDTO.setIdEta(1); //para teste
        estoqueDTO.setIdProduto(34); //para teste

        int resultado = estoqueDAO.inserirEstoque(estoqueDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
        } else {
            // Página de erro
        }
    }


    protected void listarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Recebimento do id do estoque que será editado
        int id = Integer.parseInt(req.getParameter("id"));
        //Setar a variavel Estoque
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setId(id);
        //executar o metodo buscarPorId
        estoqueDAO.buscarPorId(estoqueDTO);
        //Setar os atributos do formulário no estoqueDTO
        req.setAttribute("id", estoqueDTO.getId());
        req.setAttribute("quantidade", estoqueDTO.getQuantidade());
        req.setAttribute("dataValidade", estoqueDTO.getDataValidade());
        req.setAttribute("minPossivelEstocado", estoqueDTO.getMinPossivelEstocado());
        //Encaminhar ao documento alterarEstoque.jsp
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/estoque/alterarEstoque.jsp");
        rd.forward(req, resp);
    }

    protected void alterarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1️⃣ Pegar o ID do produto que está sendo alterado
        int id = Integer.parseInt(req.getParameter("id"));
        System.out.println(id);

        // 2️⃣ Buscar o produto no banco
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setId(id);
        estoqueDTO.setQuantidade(Integer.parseInt(req.getParameter("quantidade")));
        //Convertendo de String para Date
        String dataStr = req.getParameter("dataValidade");
        estoqueDTO.setDataValidade(java.sql.Date.valueOf(dataStr));
        estoqueDTO.setMinPossivelEstocado(Integer.parseInt(req.getParameter("minPossivelEstocado")));

        // Chamar o DAO
        int resultado = estoqueDAO.alterarEstoque(estoqueDTO);

        // Tratar o resultado
        if (resultado == 1) {
//            req.setAttribute("alteradoSucesso", true);
            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainEstoque");
        } else {
            // Página de erro
//            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=main");
        }
    }

    protected void removerEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setId(Integer.parseInt(req.getParameter("id")));

        int resultado = estoqueDAO.removerEstoque(estoqueDTO);

        if (resultado == 1) {
            // Atualiza a lista de produtos na mesma página
            estoques(req, resp);
        } else {
            // Página de erro
//            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }
}