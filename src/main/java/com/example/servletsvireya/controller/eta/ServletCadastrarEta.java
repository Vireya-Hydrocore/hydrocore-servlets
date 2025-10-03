package com.example.servletsvireya.controller.eta;

import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.model.Eta;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ServletCadastrarEta", value = "/servlet-cadastrar-eta")

public class ServletCadastrarEta extends HttpServlet {

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Pegar os parâmetros que vieram do formulário JSP
        int id = Integer.parseInt(req.getParameter("id"));
        String nome = req.getParameter("nome");
        int capacidade = Integer.parseInt(req.getParameter("capacidade"));

        // Criar o objeto Eta
        Eta eta = new Eta();

        eta.setNome(nome);
        eta.setCapacidade(capacidade);
        eta.setId(id);

        // Salvar no banco via DAO
        EtaDAO etaDAO = new EtaDAO();
        int resultado = etaDAO.inserirETA((eta));

        // Redirecionar ou mostrar mensagem
        if (resultado > 0) {
            // deu certo -> volta pra lista
            resp.sendRedirect("lista-eta.jsp");
        } else {
            // deu errado -> manda pra uma página de erro
            resp.sendRedirect("erro.jsp");
        }
    }
}