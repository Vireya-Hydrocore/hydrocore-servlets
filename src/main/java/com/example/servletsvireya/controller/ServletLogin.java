package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.AdminDAO;
import com.example.servletsvireya.model.Admin;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"/ServletLogin", "/logar", "/logarAdmin"}, name = "ServletLogin")
public class ServletLogin extends HttpServlet {

    private final AdminDAO adminDAO = new AdminDAO();


    // ===============================================================
    //          Método doPost (atributos passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action; // Proteção contra NullPointerException em switch de String

        action = req.getParameter("action");

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar admins (ou redirecionar)
            logar(req, resp);
            return;
        }

        try {
            switch (action) {
                case "logarAdmin":
                    logarAdmin(req, resp); //Login dos desenvolvedores
                    break;
                case "logar":
                    logar(req, resp); //Gerente da ETA
                    break;
                default:
                    logar(req, resp);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erro", "Ocorreu um erro interno ao processar o login.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //          Método para LOGAR ADMIN (......)
    // ===============================================================

    protected void logarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email; //Pegando atributos para realização do login na Área Restrita
        String senha; //Pegando atributos para realização do login na Área Restrita
        Boolean resultado;
        RequestDispatcher rd;

        //Pegando atributos para realização do login na Área Restrita
        email = req.getParameter("email");
        senha = req.getParameter("senha");

        resultado = adminDAO.seLogarAreaRestrita(email, senha);
        System.out.println(resultado);

        if (resultado) {
            // Redireciona para página inicial do admin
            resp.sendRedirect(req.getContextPath() + "/ServletEta?action=mainEta");
        } else {
            req.setAttribute("erroLogin", "E-mail ou senha incorretos."); //Setta um atributo erro para o JSP tratar
            rd = req.getRequestDispatcher("/paginasCrud/menu/index.jsp"); //Volta para a página de login
            rd.forward(req, resp);
        }
    }


    // ===============================================================
    //          Método para LOGAR USUÁRIO (pagina do vitor???)
    // ===============================================================

    protected void logar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email;
        String senha;
        Integer idAdmin;
        RequestDispatcher rd;

        email = req.getParameter("email");
        senha = req.getParameter("senha");

        idAdmin = adminDAO.seLogar(email, senha);

        if (idAdmin != null) {
            // Redireciona para página principal do admin
            resp.sendRedirect(req.getContextPath() + "/ServletEta?action=mainEta");
        } else {
            req.setAttribute("erroLogin", "E-mail ou senha incorretos.");
            rd = req.getRequestDispatcher("/paginasCrud/menu/index.jsp");
            rd.forward(req, resp);
        }
    }
}