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

    private AdminDAO adminDAO = new AdminDAO();
    private List<String> erros = new ArrayList<>();

    // Variáveis de escopo de método movidas para o topo (declaradas)
    private String action;
    private String email;
    private String senha;
    private boolean resultado;
    private Integer idAdmin;
    private RequestDispatcher rd;


    // ===============================================================
    //          Método doPost (atributos passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
            erros.clear();
            erros.add("Ocorreu um erro inesperado ao processar a solicitação de login");
            req.setAttribute("erros", erros);
            // Redireciona para página de erro, usando RequestDispatcher
            req.getRequestDispatcher("/assets/pages/erros/erroLogin.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //          Método para LOGAR ADMIN (Desenvolvedores)
    // ===============================================================

    protected void logarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Pegando atributos para realização do login na Área Restrita
        email = req.getParameter("email");
        senha = req.getParameter("senha");

        resultado = adminDAO.seLogarAreaRestrita(email, senha);

        if (resultado) {
            // Redireciona para página inicial do admin
            resp.sendRedirect(req.getContextPath() + "/ServletDashboard");
        } else {
            erros.clear();
            erros.add("E-mail ou senha incorretos.");
            req.setAttribute("erros", erros); //Setta um atributo erro para o JSP tratar
            rd = req.getRequestDispatcher("/assets/pages/erros/erroLogin.jsp"); //Vai para a pagina de erro
            rd.forward(req, resp);
        }
    }


    // ===============================================================
    //                 Método para LOGAR USUÁRIO
    // ===============================================================

    protected void logar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        email = req.getParameter("email");
        senha = req.getParameter("senha");

        idAdmin = adminDAO.seLogar(email, senha);

        if (idAdmin != null) {
            // Redireciona para página principal do dashboard
            // constrói a URL de destino
            String url = "https://hydrocore-gerente.onrender.com/"
                    + "?funcionarioId=" + idAdmin
                    + "&token=" + URLEncoder.encode(TOKEN, "UTF-8");

            // redireciona o navegador
            resp.sendRedirect(url);
            return;
        } else {
            erros.clear();
            erros.add("E-mail ou senha incorretos.");
            req.setAttribute("erros", erros);
            rd = req.getRequestDispatcher("/assets/pages/erros/erroLogin.jsp");
            rd.forward(req, resp);
        }
    }
}