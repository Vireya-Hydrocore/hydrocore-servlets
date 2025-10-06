<<<<<<< HEAD
package com.example.servletsvireya.controller.funcionario;

import com.example.servletsvireya.dao.CargoDAO;
import com.example.servletsvireya.dao.FuncionarioDAO;

import com.example.servletsvireya.dto.FuncionarioDTO;
import com.example.servletsvireya.model.Funcionario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletFuncionario", "/mainFuncionario", "/selectFuncionario", "/updateFuncionario", "/deleteFuncionario", "/editarFuncionario"}, name = "ServletFuncionario")
public class ServletFuncionario extends HttpServlet {
    private FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    // GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar produtos (ou redirecionar)
            listarFuncionarioPorEta(req, resp);
            return;
        }

        try {
            switch (action) {
                case "mainFuncionario":
                    listarFuncionarioPorEta(req, resp);
                    break;
                case "deleteFuncionario":
                    removerFuncionario(req, resp);
                    break;
                case "editarFuncionario":
                    int id = Integer.parseInt(req.getParameter("id"));
                    FuncionarioDTO funcionario = new FuncionarioDTO();
                    funcionario.setId(id);
                    funcionario = funcionarioDAO.selecionarFuncionario(funcionario);

                    req.setAttribute("funcionario", funcionario);
                    RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/funcionario/funcionarioAlterar.jsp");
                    rd.forward(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/funcionario/funcionarioIndex.jsp");
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
            case "createFuncionario":
                inserirFuncionario(req, resp);
                break;
            case "updateFuncionario":
                alterarFuncionario(req,resp);
                break;

            case "deleteFuncionario":
                removerFuncionario(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
        }
    }

    // MÉTODOS AUXILIARES


    protected void inserirFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FuncionarioDTO funcionario = new FuncionarioDTO();
        CargoDAO cargo = new CargoDAO();
        String cargoNome = req.getParameter("cargo");
        Integer cargoId = cargo.buscarIdPorNome(cargoNome);

        funcionario.setNome(req.getParameter("nome"));
        funcionario.setEmail(req.getParameter("email"));
        String dataStr = req.getParameter("dataNascimento");
        if (dataStr != null && !dataStr.isEmpty()) {
            funcionario.setDataNascimento(java.sql.Date.valueOf(dataStr));
        }
        String dataStr2 = req.getParameter("dataAdmissao");
        if (dataStr2 != null && !dataStr2.isEmpty()) {
            funcionario.setDataAdmissao(java.sql.Date.valueOf(dataStr));
        }
        funcionario.setSenha(req.getParameter("senha"));
        funcionario.setIdEta(1);//FAZER LOGIN
        funcionario.setIdCargo(cargoId);

        int resultado = funcionarioDAO.inserirFuncionario(funcionario);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
        } else {
            // Página de erro
        }
    }

    protected void listarFuncionarioPorEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ⚠️ Por enquanto, fixo para testes
        int idEta = 1;

        // ✅ No futuro, isso aqui pega da sessão:
        // HttpSession session = req.getSession();
        // AdminDTO adminLogado = (AdminDTO) session.getAttribute("adminLogado");
        // int idEta = adminLogado.getIdEta();

        List<com.example.servletsvireya.dto.FuncionarioDTO> lista = funcionarioDAO.listarFuncionariosPorEta(idEta);

        req.setAttribute("funcionarios", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/funcionario/funcionarioIndex.jsp");
        rd.forward(req, resp);
    }


    protected void alterarFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        FuncionarioDTO antigo = funcionarioDAO.buscarPorId(id); // 🔹 busca o registro atual

        CargoDAO cargoDAO = new CargoDAO();
        String cargoNome = req.getParameter("nomeCargo");
        Integer cargoId = cargoDAO.buscarIdPorNome(cargoNome);

// Atualiza só o que foi alterado:
        antigo.setNome(req.getParameter("nome"));
        antigo.setEmail(req.getParameter("email"));
        antigo.setSenha(req.getParameter("senha"));
        antigo.setIdCargo(cargoId);

// Chama o DAO pra atualizar:
        int resultado = funcionarioDAO.alterarFuncionario(antigo);


        if (resultado == 1) {
            req.setAttribute("alteradoSucesso", true);
            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao alterar funcionário");
        }
    }



    protected void removerFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        int resultado = funcionarioDAO.removerFuncionario(id);

        if (resultado == 1) {
            // Atualiza a lista de produtos na mesma página
            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
        } else {
            // Página de erro
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }

}
=======
//package com.example.servletsvireya.controller.funcionario;
//
//import com.example.servletsvireya.dao.CargoDAO;
//import com.example.servletsvireya.dao.FuncionarioDAO;
//
//import com.example.servletsvireya.dto.FuncionarioDTO;
//import com.example.servletsvireya.model.Funcionario;
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//import java.sql.Date;
//import java.sql.SQLException;
//import java.util.List;
//
//@WebServlet(urlPatterns = {"/ServletFuncionario", "/mainFuncionario", "/selectFuncionario", "/updateFuncionario", "/deleteFuncionario", "/editarFuncionario"}, name = "ServletFuncionario")
//public class ServletFuncionario extends HttpServlet {
//
//    private FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
//
//    // GET
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action = req.getParameter("action");
//
//        // Proteção contra NullPointerException em switch de String
//        if (action == null) {
//            // comportamento padrão: listar produtos (ou redirecionar)
//            listarFuncionarioPorEta(req, resp);
//            return;
//        }
//
//        try {
//            switch (action) {
//                case "mainFuncionario":
//                    listarFuncionarioPorEta(req, resp);
//                    break;
//                case "deleteFuncionario":
//                    removerFuncionario(req, resp);
//                    break;
//                case "editarFuncionario":
//                    int id = Integer.parseInt(req.getParameter("id"));
//                    FuncionarioDTO funcionario = new FuncionarioDTO();
//                    funcionario.setId(id);
//                    funcionario = funcionarioDAO.selecionarFuncionario(funcionario);
//
//                    req.setAttribute("funcionario", funcionario);
//                    RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/funcionario/funcionarioAlterar.jsp");
//                    rd.forward(req, resp);
//                    break;
//                default:
//                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/funcionario/funcionarioIndex.jsp");
//            }
//        } catch (Exception e) {
//            System.out.println("EXCEÇÃO");
//            e.printStackTrace();
//        }
//    }
//
//    // POST
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action = req.getParameter("action");
//
//        switch (action) {
//            case "createFuncionario":
//                inserirFuncionario(req, resp);
//                break;
//            case "updateFuncionario":
//                alterarFuncionario(req,resp);
//                break;
//
//            case "deleteFuncionario":
//                removerFuncionario(req, resp);
//                break;
//            default:
//                resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
//        }
//    }
//
//    // MÉTODOS AUXILIARES
//
//
//    protected void inserirFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        FuncionarioDTO funcionario = new FuncionarioDTO();
//        CargoDAO cargo = new CargoDAO();
//        String cargoNome = req.getParameter("cargo");
//        Integer cargoId = cargo.buscarIdPorNome(cargoNome);
//
//        funcionario.setNome(req.getParameter("nome"));
//        funcionario.setEmail(req.getParameter("email"));
//        String dataStr = req.getParameter("dataNascimento");
//        if (dataStr != null && !dataStr.isEmpty()) {
//            funcionario.setDataNascimento(java.sql.Date.valueOf(dataStr));
//        }
//        String dataStr2 = req.getParameter("dataAdmissao");
//        if (dataStr2 != null && !dataStr2.isEmpty()) {
//            funcionario.setDataAdmissao(java.sql.Date.valueOf(dataStr));
//        }
//        funcionario.setSenha(req.getParameter("senha"));
//        funcionario.setIdEta(1);//FAZER LOGIN
//        funcionario.setIdCargo(cargoId);
//
//        int resultado = funcionarioDAO.inserirFuncionario(funcionario);
//
//        if (resultado == 1) {
//            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
//        } else {
//            // Página de erro
//        }
//    }
//
//    protected void listarFuncionarioPorEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        // ⚠️ Por enquanto, fixo para testes
//        int idEta = 1;
//
//        // ✅ No futuro, isso aqui pega da sessão:
//        // HttpSession session = req.getSession();
//        // AdminDTO adminLogado = (AdminDTO) session.getAttribute("adminLogado");
//        // int idEta = adminLogado.getIdEta();
//
//        List<com.example.servletsvireya.dto.FuncionarioDTO> lista = funcionarioDAO.listarFuncionariosPorEta(idEta);
//
//        req.setAttribute("funcionarios", lista);
//        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/funcionario/funcionarioIndex.jsp");
//        rd.forward(req, resp);
//    }
//
//
//    protected void alterarFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        int id = Integer.parseInt(req.getParameter("id"));
//        FuncionarioDTO antigo = funcionarioDAO.buscarPorId(id); // 🔹 busca o registro atual
//
//        CargoDAO cargoDAO = new CargoDAO();
//        String cargoNome = req.getParameter("nomeCargo");
//        Integer cargoId = cargoDAO.buscarIdPorNome(cargoNome);
//
//// Atualiza só o que foi alterado:
//        antigo.setNome(req.getParameter("nome"));
//        antigo.setEmail(req.getParameter("email"));
//        antigo.setSenha(req.getParameter("senha"));
//        antigo.setIdCargo(cargoId);
//
//// Chama o DAO pra atualizar:
//        int resultado = funcionarioDAO.alterarFuncionario(antigo);
//
//
//        if (resultado == 1) {
//            req.setAttribute("alteradoSucesso", true);
//            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
//        } else {
//            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao alterar funcionário");
//        }
//    }
//
//
//
//    protected void removerFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        int id = Integer.parseInt(req.getParameter("id"));
//        int resultado = funcionarioDAO.removerFuncionario(id);
//
//        if (resultado == 1) {
//            // Atualiza a lista de produtos na mesma página
//            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
//        } else {
//            // Página de erro
//            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
//        }
//    }
//
//}
>>>>>>> c47397cb1c3deaddd58fd219de45fc910e1f01dc
