package com.example.servletsvireya.controller.admin;

import com.example.servletsvireya.dao.AdminDAO;
import com.example.servletsvireya.model.Admin;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ServletListarAdmin", value = "/ServletListarAdmin")
public class ServletListarAdmin extends HttpServlet {
    AdminDAO estoqueDAO = new AdminDAO();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Criando um objeto que irá receber os dados
        List<Admin> adminList = adminDAO.buscarAdmin();
        //Encaminhar lista ao documento index.jsp
        req.setAttribute("admin", adminList);
        RequestDispatcher rd = req.getRequestDispatcher("index.jsp"); //
        rd.forward(req, resp);
    }
}
