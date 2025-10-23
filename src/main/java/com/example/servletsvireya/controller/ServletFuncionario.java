package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.CargoDAO;
import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dao.FuncionarioDAO;

import com.example.servletsvireya.dto.CargoDTO;
import com.example.servletsvireya.dto.EstoqueDTO;
import com.example.servletsvireya.dto.FuncionarioDTO;
import com.example.servletsvireya.model.Funcionario;
import com.example.servletsvireya.util.SenhaHash;
import com.example.servletsvireya.util.Validador; // ===== IMPORTAÇÃO DO VALIDADOR =====
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletFuncionario", "/mainFuncionario", "/createFuncionario", "/selectFuncionario", "/updateFuncionario", "/deleteFuncionario", "/filtroFuncionario"}, name = "ServletFuncionario")
public class ServletFuncionario extends HttpServlet {

    private FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    List<String> erros = new ArrayList<>();


    // ===============================================================
    //            Método doGet (atributos passam pela URL)
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
                    break;
                case "selectFuncionario":
                    buscarFuncionario(req, resp);
                    break;
                case "filtroFuncionario":
                    filtrarFuncionario(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/assets/pages/funcionario/funcionarioIndex.jsp");
            }
        } catch (Exception e) {
            erros.add("Erro ao processar a solicitação de funcionário.");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //          Método doPost (atributos passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // Comportamento padrão: listar funcionario (ou redirecionar)
            listarFuncionarios(req, resp);
            return;
        }

        try {
            switch (action) {
                case "createFuncionario":
                    inserirFuncionario(req, resp);
                    break;
                case "updateFuncionario":
                    alterarFuncionario(req, resp);
                    break;
                case "deleteFuncionario":
                    removerFuncionario(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
            }
        } catch (Exception e) {
            erros.add("Erro inesperado ao processar a ação do funcionário.");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ================== MÉTODOS AUXILIARES =========================


    // ===============================================================
    //              Método para LISTAR os funcionários
    // ===============================================================

    protected void listarFuncionarios(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<FuncionarioDTO> lista = funcionarioDAO.listarFuncionarios(); //List de objetos retornados na query

        req.setAttribute("funcionarios", lista); //Devolve a lista de funcionarios encontrados em um novo atributo, para a pagina JSP

        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/funcionario/funcionarioIndex.jsp"); //Envia para a página principal
        rd.forward(req, resp);
    }


    // ===============================================================
    //               Método para INSERIR o funcionário
    // ===============================================================

    protected void inserirFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Criando um DTO para armazenar os valores inseridos
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
            funcionarioDTO.setDataAdmissao(java.sql.Date.valueOf(dataStr2)); // Corrigido dataStr2
        }

        // ===== Validação com classe Validador =====
        erros = new ArrayList<>();

        if (!Validador.naoVazio(funcionarioDTO.getNome())) {
            erros.add("O nome do funcionário não pode estar vazio.");
        }

        if (!Validador.validarEmail(funcionarioDTO.getEmail())) {
            erros.add("O e-mail informado é inválido.");
        }

        if (!Validador.validarDataNascimento((Date) funcionarioDTO.getDataNascimento())) {
            erros.add("O funcionário deve ter pelo menos 16 anos.");
        }

        if (!Validador.validarDataAdmissao((Date) funcionarioDTO.getDataAdmissao())) {
            erros.add("A data de admissão deve ser hoje ou anterior.");
        }

        String senhaDigitada = req.getParameter("senha");
        List<String> errosSenha = Validador.validarSenha(senhaDigitada);
        erros.addAll(errosSenha);

        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
            return;
        }

        //Criptografia de senha
        String senhaCrip = SenhaHash.hashSenha(senhaDigitada);
        funcionarioDTO.setSenha(senhaCrip);

        int resultado = funcionarioDAO.inserirFuncionario(funcionarioDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario"); //Lista novamente os funcionarios se der certo
        } else {
            erros.add("Cargo ou ETA inexistente, verifique os campos e tente novamente!");
            req.setAttribute("erros", erros); //Setta um atributo com o erro
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp); //Vai para a página de erro
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
        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/funcionario/funcionarioAlterar.jsp");
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

        // ===== Validação com classe Validador =====
        List<String> erros = new ArrayList<>();

        if (!Validador.naoVazio(funcionarioDTO.getNome())) {
            erros.add("O nome do funcionário não pode estar vazio.");
        }

        if (!Validador.validarEmail(funcionarioDTO.getEmail())) {
            erros.add("O e-mail informado é inválido.");
        }

        if (!Validador.validarDataNascimento((java.sql.Date) funcionarioDTO.getDataNascimento())) {
            erros.add("O funcionário deve ter pelo menos 16 anos");
        }

        if (!Validador.validarDataAdmissao((java.sql.Date) funcionarioDTO.getDataAdmissao())) {
            erros.add("A data de admissão deve ser hoje ou anterior.");
        }

        List<String> errosSenha = Validador.validarSenha(funcionarioDTO.getSenha());
        erros.addAll(errosSenha);

        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
            return;
        }
        // ===== Fim da validação =====

        // Criptografar novamente antes de salvar
        funcionarioDTO.setSenha(SenhaHash.hashSenha(funcionarioDTO.getSenha()));

        // Chamar o DAO para alterar
        int resultado = funcionarioDAO.alterarFuncionario(funcionarioDTO);

        // Tratar o resultado
        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
        } else {
            erros.add("Cargo ou ETA inexistente, verifique os campos e tente novamente");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //       Método para REMOVER o funcionário (pelo ID pego)
    // ===============================================================

    protected void removerFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        int resultado = funcionarioDAO.removerFuncionario(id);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
        } else {
            erros.add("Não foi possível remover o funcionário, tente novamente.");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //     Método para FILTRAR o funcionário (por coluna e valor)
    // ===============================================================

    protected void filtrarFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<FuncionarioDTO> lista = funcionarioDAO.filtroBuscaPorColuna(req.getParameter("nome_coluna"), req.getParameter("pesquisa"));

        req.setAttribute("funcionarios", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/funcionario/funcionarioIndex.jsp");
        rd.forward(req, resp);
    }
}
