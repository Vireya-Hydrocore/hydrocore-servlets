//package com.example.servletsvireya.controller.produto;
//
//import com.example.servletsvireya.dao.ProdutoDAO;
//import com.example.servletsvireya.dto.ProdutoDTO;
//import com.example.servletsvireya.model.Produto;
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//
//import java.io.IOException;
//import java.util.List;
//
//@WebServlet(urlPatterns = {"/ServletProduto"}, name = "ServletProduto") SERVLET DO IAGO
//public class ServletProduto extends HttpServlet {
//
//    private final ProdutoDAO produtoDAO = new ProdutoDAO();
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action = req.getParameter("action");
//        if (action == null) action = "mainProduto";
//
//        switch (action) {
//            case "mainProduto":
//                listarProdutosPorEta(req, resp);
//                break;
//            case "editarProduto":
//                abrirTelaEdicao(req, resp); // abre a JSP de edição com os dados do produto
//                break;
//            case "deleteProduto":
//                removerProduto(req, resp); // aceita via GET também
//                break;
//            default:
//                resp.sendRedirect(req.getContextPath() + "/paginasCrud/produto/produtoIndex.jsp");
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action = req.getParameter("action");
//        if (action == null) action = "mainProduto";
//
//        switch (action) {
//            case "createProduto":
//                inserirProduto(req, resp);
//                break;
//            case "updateProduto":
//                alterarProduto(req, resp);
//                break;
//            case "deleteProduto":
//                removerProduto(req, resp);
//                break;
//            default:
//                resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto");
//        }
//    }
//
//    protected void listarProdutosPorEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        int idEta = 1; // temporário
//        List<ProdutoDTO> lista = produtoDAO.listarProdutoPorEta(idEta);
//        req.setAttribute("produtos", lista);
//        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/produto/produtoIndex.jsp");
//        rd.forward(req, resp);
//    }
//
//    protected void abrirTelaEdicao(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        int id = Integer.parseInt(req.getParameter("id"));
//        Produto produto = new Produto();
//        produto.setId(id);
//        produtoDAO.selecionarProduto(produto); // preenche o produto
//        req.setAttribute("produtoSelecionado", produto);
//        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/produto/produtoAlterar.jsp");
//        rd.forward(req, resp);
//    }
//
//    protected void inserirProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Produto produto = new Produto();
//        produto.setNome(req.getParameter("nome"));
//        produto.setTipo(req.getParameter("tipo"));
//        produto.setUnidadeMedida(req.getParameter("unidadeMedida"));
//        try {
//            produto.setConcentracao(Double.parseDouble(req.getParameter("concentracao")));
//        } catch (Exception e) {
//            produto.setConcentracao(0.0);
//        }
//
//        int resultado = produtoDAO.cadastrarProduto(produto);
//        if (resultado == 1) {
//            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto");
//        } else {
//            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
//        }
//    }
//
//    protected void alterarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        int id = Integer.parseInt(req.getParameter("id"));
//
//        Produto produtoAntigo = new Produto();
//        produtoAntigo.setId(id);
//        produtoDAO.selecionarProduto(produtoAntigo);
//
//        Produto produtoNovo = new Produto();
//        produtoNovo.setId(id);
//        produtoNovo.setNome(req.getParameter("nome"));
//        produtoNovo.setTipo(req.getParameter("tipo"));
//        produtoNovo.setUnidadeMedida(req.getParameter("unidadeMedida"));
//        try {
//            produtoNovo.setConcentracao(Double.parseDouble(req.getParameter("concentracao")));
//        } catch (Exception e) {
//            produtoNovo.setConcentracao(produtoAntigo.getConcentracao());
//        }
//
//        int resultado = produtoDAO.alterar(produtoAntigo, produtoNovo);
//        if (resultado == 1) {
//            // mostra alerta na próxima tela via session (ou request)
//            HttpSession session = req.getSession();
//            session.setAttribute("alteradoSucesso", true);
//        }
//        resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto");
//    }
//
//    protected void removerProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        int id = Integer.parseInt(req.getParameter("id"));
//        ProdutoDTO produto = new ProdutoDTO();
//        produto.setId(id);
//        int resultado = produtoDAO.removerProduto(produto);
//        if (resultado == 1) {
//            resp.sendRedirect(req.getContextPath() + "/ServletProduto?action=mainProduto");
//        } else {
//            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
//        }
//    }
//}

