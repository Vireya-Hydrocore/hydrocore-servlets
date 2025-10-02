package com.example.servletsvireya.controller.admin;

import com.example.servletsvireya.dao.AdminDAO;
import com.example.servletsvireya.model.Admin;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletInserirAdmin", value = "/servlet-inserir-admin")
public class ServletInserirAdmin extends HttpServlet {
    AdminDAO adminDAO = new AdminDAO();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Pegando valores dos inputs
        String idStr = req.getParameter("id");
	String nome = req.getParameter("nome");
	String email = req.getParameter("email");
	String idEtaStr = req.getParameter("idEta");

        //Convertendo para os valores adequados
        int id = Integer.parseInt(idStr);
        int idEta = Integer.parseInt(idEtaStr);

        //Instanciando objeto model Admin
        Admin admin = new Admin();
        admin.setId(id);
        admin.setNome(nome);
        admin.setEmail(email);
        admin.setIdEta(idEta);

        //Inserindo no estoque do banco de dados
        adminDAO.inserirAdmin(admin);

    }
}
