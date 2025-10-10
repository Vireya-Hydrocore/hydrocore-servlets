package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.CargoDAO;
import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dao.FuncionarioDAO;

import com.example.servletsvireya.dto.CargoDTO;
import com.example.servletsvireya.dto.EstoqueDTO;
import com.example.servletsvireya.dto.FuncionarioDTO;
import com.example.servletsvireya.model.Funcionario;
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

@WebServlet(urlPatterns = {"/ServletFuncionario", "/mainFuncionario", "/selectFuncionario", "/updateFuncionario", "/deleteFuncionario", "/editarFuncionario"}, name = "ServletFuncionario")
public class ServletFuncionario extends HttpServlet {

    private FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    // ================================================================================
    //         Método doGet (analisa o atributo action sem passar pela URL)
    // ================================================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

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
                    break;
                case "selectFuncionario":
                    buscarFuncionario(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/funcionario/funcionarioIndex.jsp");
            }
        } catch (Exception e) {
            System.out.println("EXCEÇÃO");
            e.printStackTrace();
        }
    }

    // POST
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action) {
            case "createFuncionario":
                inserirFuncionario(req, resp);
                break;
            case "updateFuncionario":
                alterarFuncionario(req,resp);
                break;
            case "deleteFuncionario":
                removerFuncionario(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
        }
    }

    // MÉTODOS AUXILIARES


    // ===============================================================
    //               Método para INSERIR o funcionário
    // ================================================================

    protected void inserirFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FuncionarioDTO funcionario = new FuncionarioDTO();
        CargoDAO cargo = new CargoDAO();
        EtaDAO etadao= new EtaDAO();
        String cargoNome = req.getParameter("cargo");
        Integer cargoId = cargo.buscarIdPorNome(cargoNome);
        String nomeEta= req.getParameter("nomeEta");
        funcionario.setNomeEta(nomeEta);
        funcionario.setIdEta(etadao.buscarIdPorNome(nomeEta));
        funcionario.setNome(req.getParameter("nome"));
        funcionario.setEmail(req.getParameter("email"));
        funcionario.setEmail(req.getParameter("email"));
        String dataStr = req.getParameter("dataNascimento");

        if (dataStr != null && !dataStr.isEmpty()) {
            funcionario.setDataNascimento(java.sql.Date.valueOf(dataStr));
        }
        String dataStr2 = req.getParameter("dataAdmissao");
        if (dataStr2 != null && !dataStr2.isEmpty()) {
            funcionario.setDataAdmissao(java.sql.Date.valueOf(dataStr));
        }
        funcionario.setSenha(req.getParameter("senha"));
        funcionario.setIdCargo(cargoId);

        int resultado = funcionarioDAO.inserirFuncionario(funcionario);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
        } else {
            // Página de erro
        }
    }

    protected void listarFuncionarios(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<FuncionarioDTO> lista = funcionarioDAO.listarFuncionarios();

        req.setAttribute("funcionarios", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/funcionario/funcionarioIndex.jsp");
        rd.forward(req, resp);
    }


    // ===================================================================================
    //   Método para BUSCAR o funcionário (mostra os VALORES ANTIGOS na tela de edição)
    // ===================================================================================

    protected void buscarFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Recebimento do id do funcionario que será editado
        int id = Integer.parseInt(req.getParameter("id"));

        //Setar no objeto tipo DTO
        FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
        funcionarioDTO.setId(id);

        //executar o metodo buscarPorId
        funcionarioDAO.buscarPorId(funcionarioDTO); //No mesmo objeto, setta os valores encontrados

        //Setar os atributos do funcionarioDTO no formulário
        req.setAttribute("funcionario", funcionarioDTO);

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
        String dataAdmissao = req.getParameter("dataAdmissao"); //Conversão de String para Date
        funcionarioDTO.setDataAdmissao(java.sql.Date.valueOf(dataAdmissao));
        String dataNascimento = req.getParameter("dataNascimento"); //Aqui também
        funcionarioDTO.setDataNascimento(java.sql.Date.valueOf(dataNascimento));
        String nomeCargo = req.getParameter("nomeCargo");
//        funcionarioDTO.setNomeEta(req.getParameter("nomeEta")); //Não muda

        // Buscar o id do cargo pelo nome
        CargoDAO cargoDAO = new CargoDAO();
        funcionarioDTO.setIdCargo(cargoDAO.buscarIdPorNome(nomeCargo)); //Nesse método é retornado o id do cargo

        // Chamar o DAO com o id já settado no DTO
        int resultado = funcionarioDAO.alterarFuncionario(funcionarioDTO); //Dentro do objeto, é settado os atributos

        // Tratar o resultado
        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
        }
        else { // Página de erro
            //Settando atributo que será pego no JSP
            req.setAttribute("erro", "Não foi possível alterar o funcionário! Verifique os campos e tente novamente.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //       Método para REMOVER o funcionário (pelo ID pego)
    // ===============================================================

    protected void removerFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

}
