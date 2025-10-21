package com.example.servletsvireya.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.servletsvireya.dao.DashBoardDAO;


import java.io.IOException;
import java.util.Map;

@WebServlet(value = "/dashAnalise", name = "/ServletDashBoard")
public class ServletdashBoard extends HttpServlet {

    private DashBoardDAO dash = new DashBoardDAO();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String, Integer> lista = dash.contagemGeral(); //List de objetos retornados na query

        req.setAttribute("dashboard", lista); //Devolve a lista dos resultados encontrados em um novo atributo

        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/dashBoard.jsp"); //Envia para a página principal
        rd.forward(req, resp);
    }
}