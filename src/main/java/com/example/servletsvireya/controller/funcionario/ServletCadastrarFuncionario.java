package com.example.servletsvireya.controller.funcionario;

import com.example.servletsvireya.dao.FuncionarioDAO;
import com.example.servletsvireya.model.Funcionario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet(name = "ServletCadastrarFuncionario", value = "/servlet-cadastrar-funcionario")

public class ServletCadastrarFuncionario extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        String nome = req.getParameter("nome");
        String email = req.getParameter("email");
        LocalDate dataAdmissao = LocalDate.parse(req.getParameter("data_admissao"));
        LocalDate dataNascimento = LocalDate.parse(req.getParameter("data_nascimento"));
        int idEta = Integer.parseInt(req.getParameter("id_eta"));
        int idCargo = Integer.parseInt(req.getParameter("id_cargo"));

        Funcionario funcionario = new Funcionario();

        funcionario.setId(id);
        funcionario.setNome(nome);
        funcionario.setEmail(email);
        funcionario.setDataAdmissao(dataAdmissao);
        funcionario.setDataNascimento(dataNascimento);
        funcionario.setIdEta(idEta);
        funcionario.setIdCargo(idCargo);

        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        int resultado = funcionarioDAO.inserirFuncionario(funcionario);

//        if (resultado > 0){
//            resp.sendRedirect("lista-funcionario.jsp");
//        }
//        else {
//            resp.sendRedirect("erro.jsp");
//        }
    }
}