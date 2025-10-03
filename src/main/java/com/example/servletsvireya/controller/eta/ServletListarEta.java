package com.example.servletsvireya.controller.eta;

import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.model.Eta;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ServletListarEta", value = "/servlet-listar-eta")

public class ServletListarEta extends HttpServlet {

    private EtaDAO etaDAO = new EtaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        List<Eta> listaEtas = etaDAO.buscarETA();
        req.setAttribute("listaEtas", listaEtas);

        req.getRequestDispatcher("lista-etas.jsp").forward(req, resp);
    }
}