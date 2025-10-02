package com.example.servletsvireya.controller.admin;

import com.example.servletsvireya.dao.AdminDAO;
import com.example.servletsvireya.model.Admin;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletAlterarAdmin", value = "/ServletAlterarAdmin")

public class ServletAlterarAdmin extends HttpServlet {
	AdminDAO adminDAO = new AdminDAO();
	
	@Override
	protect void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//Pegar os valores do original
		String idStr = req.getParameter("id");
		String nome = req.getParameter("nome");
		String email = req.getParameter("email");
		String idEtaStr = req.getParameter("idEta");
		
		//Convertendo os valores necessários
		int id = Integer.parseInt(idStr);
		int idEta = Integer.parseInt(idEtaStr);

		Admin modificado = new Admin(id, nome, email, idEta);

		//Pegando os valores antes da alteração
		Admin original = adminDAO.buscarAdmin(id);

		//Alterando admin do sistema
		adminDAO.alterar(original, modificado);

	}
}