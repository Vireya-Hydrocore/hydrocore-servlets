package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.AdminDAO;
import com.example.servletsvireya.model.Admin;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"/ServletLogin", "/logar", "/logarAdmin"}, name = "ServletLogin")
public class ServletLogin extends HttpServlet {
        AdminDAO adminDAO = new AdminDAO();


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
                e.printStackTrace();
            }
        }


        // ===============================================================
        //          Método para LOGAR ADMIN (Desenvolvedores)
        // ===============================================================

        protected void logarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            //Pegando atributos para realização do login na Área Restrita
            String email = req.getParameter("email");
            String senha = req.getParameter("senha");

            Boolean resultado = adminDAO.seLogarAreaRestrita(email, senha);
            System.out.println(resultado);

            if (resultado) {
                // Redireciona para página inicial do admin
                resp.sendRedirect(req.getContextPath() + "/ServletEta?action=mainEta");
            } else {
                req.setAttribute("erroLogin", "E-mail ou senha incorretos."); //Setta um atributo erro para o JSP tratar
                RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/erro/"); //Vai para a pagina de erro
                rd.forward(req, resp);
            }
        }


        // ===============================================================
        //          Método para LOGAR USUÁRIO (pagina do vitor???)
        // ===============================================================

        protected void logar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String email = req.getParameter("email");
            String senha = req.getParameter("senha");

            Integer idAdmin = adminDAO.seLogar(email, senha);

            if (idAdmin != null) {
                // Redireciona para página principal do admin
                System.out.println(idAdmin);
            } else {
                req.setAttribute("erroLogin", "E-mail ou senha incorretos.");
                RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/erroSenha.jsp");
                rd.forward(req, resp);
            }
        }
    }
