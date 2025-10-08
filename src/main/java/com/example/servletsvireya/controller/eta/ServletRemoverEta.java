package com.example.servletsvireya.controller.eta;

import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dto.EtaDTO;
import com.example.servletsvireya.model.Eta;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ServletRemoverEta", value = "/servlet-remover-eta")

public class ServletRemoverEta extends HttpServlet {

    private EtaDAO etaDAO = new EtaDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Pega o id da URL
        int id = Integer.parseInt(req.getParameter("id"));

        // Cria um objeto Eta só com o id
        EtaDTO etaDTO = new EtaDTO();
        etaDTO.setId(id);

        // Remove do BD
        int removido = etaDAO.removerEta(etaDTO);

        // Mensagem baseada no número de linhas removidas
        req.setAttribute("mensagem", removido > 0 ? "ETA removida com sucesso!" : "Erro ao remover ETA.");

        // Encaminha pro JSP
        req.getRequestDispatcher("lista-etas.jsp").forward(req, resp);
    }
}