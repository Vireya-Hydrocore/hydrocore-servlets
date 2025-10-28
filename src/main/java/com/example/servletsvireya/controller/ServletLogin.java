package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.AdminDAO;
import com.example.servletsvireya.model.Admin;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletLogin", "/logar", "/logarAdmin"}, name = "ServletLogin")
public class ServletLogin extends HttpServlet {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String TOKEN = dotenv.get("TOKEN");

    AdminDAO adminDAO = new AdminDAO();
    List<String> erros = new ArrayList<>();


    // ===============================================================
    //          Método doPost (atributos passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");

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
            erros.add("Ocorreu um erro inesperado ao processar a solicitação de login");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("assets/pages/erroLogin.jsp");
        }
    }


    // ===============================================================
    //          Método para LOGAR ADMIN (Desenvolvedores)
    // ===============================================================

    protected void logarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Pegando atributos para realização do login na Área Restrita
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        boolean resultado = adminDAO.seLogarAreaRestrita(email, senha);

        if (resultado) {
            // Redireciona para página inicial do admin
            resp.sendRedirect(req.getContextPath() + "/ServletDashboard");
        } else {
            erros.add("E-mail ou senha incorretos.");
            req.setAttribute("erros", erros); //Setta um atributo erro para o JSP tratar
            RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/erroLogin.jsp"); //Vai para a pagina de erro
            rd.forward(req, resp);
        }
    }


    // ===============================================================
    //                 Método para LOGAR USUÁRIO
    // ===============================================================

    protected void logar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        Integer idAdmin = adminDAO.seLogar(email, senha);

        if (idAdmin != null) {
            // Redireciona para página principal do dashboard
            System.out.println("conectiouyy");
            System.out.println(idAdmin);

            // constrói a URL de destino
            String url = "https://rendervireyaweb/login-externo"
                    + "?funcionarioId=" + idAdmin
                    + "&token=" + URLEncoder.encode(TOKEN, "UTF-8");

            // redireciona o navegador
            resp.sendRedirect(url);
            return;
        } else {
            erros.add("E-mail ou senha incorretos.");
            System.out.println("erro");
            req.setAttribute("erros", erros);
            RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/erroLogin.jsp");
            rd.forward(req, resp);
        }
    }
}