package com.example.servletsvireya.controller.admin;

import com.example.servletsvireya.dao.AdminDAO;
import com.example.servletsvireya.model.Admin;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ServletRemoverAdmin", value = "/servlet-remover-admin")
public class ServletRemoverAdmin extends HttpServlet {
    AdminDAO adminDAO = new AdminDAO();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Pegando valor do input
        String idStr = req.getParameter("id");

        //Convertendo id de String para int
        int id = Integer.parseInt(idStr);

        //Instanciando objeto model Estoque
        Admin admin = new Admin();
        admin.setId(id);

        //Removendo
        adminDAO.removerAdmin(admin);

        //Não precisa responder nada
    }
}