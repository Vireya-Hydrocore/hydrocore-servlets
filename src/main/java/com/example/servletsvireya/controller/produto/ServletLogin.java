//package com.example.servletsvireya.controller.produto;
//
//import com.example.servletsvireya.dao.AdminDAO;
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//
//import java.io.IOException;
//
//@WebServlet(name = "ServletLogin", value = "/ServletLogin")
//public class ServletLogin extends HttpServlet {
//
//    AdminDAO adminDAO = new AdminDAO();
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }
//
//    protected void logarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String email = req.getParameter("email");
//        String senha = req.getParameter("senha");
//
//        Integer idEta = adminDAO.seLogar(email, senha);
//
//        if (idEta != null) {
//            HttpSession session = req.getSession();
//            session.setAttribute("idEta", idEta);
//            session.setAttribute("emailAdmin", email);
//
//            // Redireciona para página principal do admin
//            resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin"); //??????????
//        } else {
//            req.setAttribute("erroLogin", "E-mail ou senha incorretos.");
//            RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/menu/index.jsp");
//            rd.forward(req, resp);
//        }
//    }
//}
