package com.example.servletsvireya.controller.estoque;

import com.example.servletsvireya.dao.EstoqueDAO;
import com.example.servletsvireya.model.Estoque;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ServletRemoverEstoque", value = "/servlet-remover-estoque")
public class ServletRemoverEstoque extends HttpServlet {
    EstoqueDAO estoqueDAO = new EstoqueDAO();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Pegando valor do input
        String idStr = req.getParameter("id");

        //Convertendo id de String para int
        int id = Integer.parseInt(idStr);

        //Instanciando objeto model Estoque
        Estoque estoque = new Estoque();
        estoque.setId(id);

        //Removendo produto do estoque
        estoqueDAO.removerEmEstoque(estoque);
    }
}