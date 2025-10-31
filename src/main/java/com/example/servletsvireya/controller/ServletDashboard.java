package com.example.servletsvireya.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.servletsvireya.dao.DashboardDAO;


import java.io.IOException;
import java.util.Map;

@WebServlet(name = "/ServletDashboard", value = "/ServletDashboard")
public class ServletDashboard extends HttpServlet {

    private DashboardDAO dashDAO = new DashboardDAO();

    // Variáveis de escopo de método movidas para o topo (declaradas)
    private Map<String, Integer> lista;
    private RequestDispatcher rd;


    // ===============================================================
    //          Método doGet (Mostra os dados do dashboard)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        lista = dashDAO.contagemGeral(); //List de objetos retornados na query

        req.setAttribute("dashboard", lista); //Devolve a lista dos resultados encontrados em um novo atributo

        rd = req.getRequestDispatcher("/assets/pages/dashboard.jsp"); //Envia para a página principal
        rd.forward(req, resp);
    }
}