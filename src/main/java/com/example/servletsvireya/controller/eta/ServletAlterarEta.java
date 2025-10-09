//package com.example.servletsvireya.controller.eta;
//
//import com.example.servletsvireya.dao.EtaDAO;
//import com.example.servletsvireya.model.Eta;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//
//@WebServlet (name = "ServletAlterarEta",value = "/servlet-alterar-eta")
//
//public class ServletAlterarEta extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        int id = Integer.parseInt(req.getParameter("id"));
//        String nome = req.getParameter("nome");
//        int capacidade = Integer.parseInt(req.getParameter("capacidade"));
//
//        Eta eta = new Eta();
//
//        eta.setId(id);
//        eta.setNome(nome);
//        eta.setCapacidade(capacidade);
//
//        EtaDAO etaDAO = new EtaDAO();
//        int resultado = etaDAO.alterarETA(eta);
//
//        if (resultado > 0){
//            resp.sendRedirect("lista-eta.jsp");
//        }
//        else {
//            resp.sendRedirect("erro.jsp");
//        }
//    }
//}