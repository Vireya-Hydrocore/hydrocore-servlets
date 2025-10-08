package com.example.servletsvireya.controller.eta;

import com.example.servletsvireya.dao.AdminDAO;
import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dto.AdminDTO;
import com.example.servletsvireya.model.Admin;
import com.example.servletsvireya.model.Eta;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet(name = "ServletCadastrarEta", value = "/servlet-cadastrar-eta")
public class ServletCadastrarEta extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Pegando dados do formulário

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String cnpj = request.getParameter("cnpj");
        String senha = request.getParameter("senha");
        int capacidade = Integer.parseInt(request.getParameter("capacidade"));
        String telefone = request.getParameter("telefone");
        // Criando objeto Admin

        cnpj = cnpj.replaceAll("\\D","");
        telefone = telefone.replaceAll("\\D","");

        Admin admin = new Admin(nome,email,senha);
        Eta eta = new Eta(nome,capacidade,telefone,cnpj);
//        admin.setIdEta(idEta);

        // Inserindo no banco via DAO

        EtaDAO etaDao = new EtaDAO();
        AdminDAO adminDAO = new AdminDAO();
        int resultado = adminDAO.inserirAdmin(admin);
        int resultadoEta = etaDao.inserirEta(eta);

        // Checando resultado
        if (resultado > 0) {
            response.sendRedirect("sucesso.jsp"); //sucesso
        } else {
            response.sendRedirect("erro.jsp"); //erro
        }
    }
}
