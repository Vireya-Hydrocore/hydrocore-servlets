package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.CargoDAO;
import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dao.FuncionarioDAO;

import com.example.servletsvireya.dto.CargoDTO;
import com.example.servletsvireya.dto.EstoqueDTO;
import com.example.servletsvireya.dto.FuncionarioDTO;
import com.example.servletsvireya.model.Funcionario;
import com.example.servletsvireya.util.SenhaHash;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletFuncionario", "/mainFuncionario", "/createFuncionario", "/selectFuncionario", "/updateFuncionario", "/deleteFuncionario"}, name = "ServletFuncionario")
public class ServletFuncionario extends HttpServlet {

    private FuncionarioDAO funcionarioDAO = new FuncionarioDAO();


    // ===============================================================
    //            Método doGet (variáveis passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action"); //Vem com a ação do usuário

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // Comportamento padrão: listar funcionario (ou redirecionar)
            listarFuncionarios(req, resp);
            return;
        }

        try {
            switch (action) {
                case "mainFuncionario":
                    listarFuncionarios(req, resp);
                case "selectFuncionario":
                    buscarFuncionario(req, resp);
                case "filtroFuncionario":
                    filtroFuncionario(req, resp);
                default:
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/funcionario/funcionarioIndex.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace(); //Mostra a exceção possível
        }
    }


    // ===============================================================
    //            Método doPost (passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action) {
            case "createFuncionario":
                inserirFuncionario(req, resp);
            case "updateFuncionario":
                alterarFuncionario(req,resp);
            case "deleteFuncionario":
                removerFuncionario(req, resp);
            default:
                resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
        }
    }


    // ================== MÉTODOS AUXILIARES =========================


    // ===============================================================
    //              Método para LISTAR os funcionários
    // ===============================================================

    protected void listarFuncionarios(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<FuncionarioDTO> lista = funcionarioDAO.listarFuncionarios(); //Objetos retornados na query

        req.setAttribute("funcionarios", lista); //Devolve a lista de produtos encontrados em um novo atributo

        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/funcionario/funcionarioIndex.jsp"); //Envia para a página principal
        rd.forward(req, resp);
    }


    // ===============================================================
    //               Método para INSERIR o funcionário
    // ================================================================

    protected void inserirFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Criado um DTO para armazenar os valores inseridos
        FuncionarioDTO funcionarioDTO = new FuncionarioDTO();

        CargoDAO cargoDAO = new CargoDAO(); //Para realizar a busca do id do cargo
        String cargoNome = req.getParameter("cargo");
        funcionarioDTO.setNomeCargo(cargoNome);
        funcionarioDTO.setIdCargo(cargoDAO.buscarIdPorNome(cargoNome));

        EtaDAO etaDAO = new EtaDAO(); //Para realizar a busca do id da ETA
        String nomeEta= req.getParameter("nomeEta");
        funcionarioDTO.setNomeEta(nomeEta);
        funcionarioDTO.setIdEta(etaDAO.buscarIdPorNome(nomeEta));

        funcionarioDTO.setNome(req.getParameter("nome"));
        funcionarioDTO.setEmail(req.getParameter("email"));
        String dataStr = req.getParameter("dataNascimento");

        if (dataStr != null && !dataStr.isEmpty()) {
            funcionarioDTO.setDataNascimento(java.sql.Date.valueOf(dataStr));
        }
        String dataStr2 = req.getParameter("dataAdmissao");
        if (dataStr2 != null && !dataStr2.isEmpty()) {
            funcionarioDTO.setDataAdmissao(java.sql.Date.valueOf(dataStr));
        }

        //Criptografia de senha
        String senhaDigitada= req.getParameter("senha");
        String senhaCrip = SenhaHash.hashSenha(senhaDigitada);
        funcionarioDTO.setSenha(senhaCrip);

        int resultado = funcionarioDAO.inserirFuncionario(funcionarioDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario"); //Lista novamente os produtos se der certo
        } else {
            req.setAttribute("erro", "Não foi possível inserir esse funcionário, Verifique os campos e tente novamente!"); //Setta um atributo com o erro
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp); //Vai para a página de erro
        }
    }


    // ===================================================================================
    //   Método para BUSCAR o funcionário (mostra os VALORES ANTIGOS na tela de edição)
    // ===================================================================================

    protected void buscarFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando o id no FuncionarioDTO
        FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
        funcionarioDTO.setId(Integer.parseInt(req.getParameter("id")));

        funcionarioDAO.buscarPorId(funcionarioDTO); //No mesmo objeto, setta os valores encontrados

        req.setAttribute("funcionario", funcionarioDTO); //Setta em um novo atributo para o JSP pegar os valores

        //Encaminhar ao documento alterarFuncionario.jsp
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/funcionario/funcionarioAlterar.jsp");
        rd.forward(req, resp);
    }


    // ===============================================================
    //     Método para ALTERAR o funcionário (com os VALORES NOVOS)
    // ===============================================================

    protected void alterarFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Settando os valores no funcionarioDTO ---> ---> --> usuário não pode mudar a eta
        FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
        funcionarioDTO.setId(Integer.parseInt(req.getParameter("id")));
        funcionarioDTO.setNome(req.getParameter("nome"));
        funcionarioDTO.setEmail(req.getParameter("email"));
        funcionarioDTO.setSenha(req.getParameter("senha"));
        String dataAdmissao = req.getParameter("dataAdmissao"); //Convertendo de String para Date
        funcionarioDTO.setDataAdmissao(java.sql.Date.valueOf(dataAdmissao));
        String dataNascimento = req.getParameter("dataNascimento"); //Convertendo novamente
        funcionarioDTO.setDataNascimento(java.sql.Date.valueOf(dataNascimento));
        String nomeCargo = req.getParameter("nomeCargo");

        // Buscar o id do cargo pelo nome
        CargoDAO cargoDAO = new CargoDAO();
        funcionarioDTO.setIdCargo(cargoDAO.buscarIdPorNome(nomeCargo)); //Nesse método é retornado o id do cargo

        // Chamar o DAO com o id já settado no DTO
        int resultado = funcionarioDAO.alterarFuncionario(funcionarioDTO); //Dentro do objeto, é settado os atributos

        // Tratar o resultado
        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
        } else {
            req.setAttribute("erro", "Não foi possível alterar o funcionário! Verifique os campos e tente novamente.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //       Método para REMOVER o funcionário (pelo ID pego)
    // ===============================================================

    protected void removerFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
        int id = Integer.parseInt(req.getParameter("id"));
        int resultado = funcionarioDAO.removerFuncionario(id);

        if (resultado == 1) {
            // Atualiza a lista de produtos na mesma página
            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
        } else {
            // Página de erro
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }



    protected void filtroFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<FuncionarioDTO> lista = funcionarioDAO.filtroBuscaPorColuna(req.getParameter("nome_coluna"),req.getParameter("pesquisa"));

        req.setAttribute("funcionarios", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/funcionario/funcionarioIndex.jsp");
        rd.forward(req, resp);
    }
}
