package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.CargoDAO;
import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dao.FuncionarioDAO;

import com.example.servletsvireya.dto.FuncionarioDTO;
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

    // Mantido o estilo de inicialização do original, mas usando final para consistência com o restante
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private final CargoDAO cargoDAO = new CargoDAO();
    private final EtaDAO etaDAO = new EtaDAO();


    // ===============================================================
    //            Método doGet (atributos passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action; //Vem com a ação do usuário

        action = req.getParameter("action");

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
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/funcionario/funcionarioIndex.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace(); //Mostra a exceção possível
            req.setAttribute("erro", "Ocorreu um erro interno ao processar a requisição.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //          Método doPost (atributos passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action;

        action = req.getParameter("action");

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erro", "Ocorreu um erro interno ao processar a requisição.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ================== MÉTODOS AUXILIARES =========================


    // ===============================================================
    //              Método para LISTAR os funcionários
    // ===============================================================

    protected void listarFuncionarios(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<FuncionarioDTO> lista; //List de objetos retornados na query
        RequestDispatcher rd; //Envia para a página principal

        lista = funcionarioDAO.listarFuncionarios(); //List de objetos retornados na query

        req.setAttribute("funcionarios", lista); //Devolve a lista de produtos encontrados em um novo atributo, para a pagina JSP

        rd = req.getRequestDispatcher("/paginasCrud/funcionario/funcionarioIndex.jsp"); //Envia para a página principal
        rd.forward(req, resp);
    }


    // ===============================================================
    //               Método para INSERIR o funcionário
    // ===============================================================

    protected void inserirFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FuncionarioDTO funcionarioDTO; //Criando um DTO para armazenar os valores inseridos

        String cargoNome; //Para realizar a busca do id do cargo
        String nomeEta; //Para realizar a busca do id da ETA
        String nome;
        String email;
        String dataStrNasc;
        String dataStrAdm;
        String senhaDigitada;
        List<String> erros; // ===== Validação com classe Validador =====
        List<String> errosSenha;
        String senhaCrip; //Criptografia de senha
        int resultado;
        RequestDispatcher rd;

        // Inicialização de DTO
        funcionarioDTO = new FuncionarioDTO(); //Criando um DTO para armazenar os valores inseridos
        erros = new ArrayList<>(); // ===== Validação com classe Validador =====

        // Parâmetros do formulário
        cargoNome = req.getParameter("cargo");
        nomeEta = req.getParameter("nomeEta");
        nome = req.getParameter("nome");
        email = req.getParameter("email");
        dataStrNasc = req.getParameter("dataNascimento");
        dataStrAdm = req.getParameter("dataAdmissao");
        senhaDigitada = req.getParameter("senha");

        funcionarioDTO.setNomeCargo(cargoNome);
        funcionarioDTO.setIdCargo(cargoDAO.buscarIdPorNome(cargoNome));

        funcionarioDTO.setNomeEta(nomeEta);
        funcionarioDTO.setIdEta(etaDAO.buscarIdPorNome(nomeEta));

        funcionarioDTO.setNome(nome);
        funcionarioDTO.setEmail(email);

        // Set de Datas com tratamento
        try {
            if (dataStrNasc != null && !dataStrNasc.isEmpty()) {
                funcionarioDTO.setDataNascimento(java.sql.Date.valueOf(dataStrNasc));
            }
            String dataStr2 = dataStrAdm; // corrigido dataStr2
            if (dataStr2 != null && !dataStr2.isEmpty()) {
                funcionarioDTO.setDataAdmissao(java.sql.Date.valueOf(dataStr2)); // corrigido dataStr2
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", "Formato de data inválido. Use YYYY-MM-DD.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
            return;
        }


        // ===== Validação com classe Validador =====

        if (!Validador.naoVazio(funcionarioDTO.getNome())) {
            erros.add("O nome do funcionário não pode estar vazio.");
        }

        if (!Validador.validarEmail(funcionarioDTO.getEmail())) {
            erros.add("O e-mail informado é inválido.");
        }

//        if (funcionarioDTO.getDataNascimento() == null || !Validador.validarData(funcionarioDTO.getDataNascimento().toLocalDate())) {
//            erros.add("A data de nascimento é inválida.");
//        }
//
//        if (funcionarioDTO.getDataAdmissao() == null || !Validador.validarData(funcionarioDTO.getDataAdmissao().toLocalDate())) {
//            erros.add("A data de admissão é inválida.");
//        }

        errosSenha = Validador.validarSenha(senhaDigitada);
        erros.addAll(errosSenha);

        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
            return;
        }

        //Criptografia de senha
        senhaCrip = SenhaHash.hashSenha(senhaDigitada);
        funcionarioDTO.setSenha(senhaCrip);

        resultado = funcionarioDAO.inserirFuncionario(funcionarioDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario"); //Lista novamente os funcionarios se der certo
        } else {
            req.setAttribute("erro", "Não foi possível inserir esse funcionário, Verifique os campos e tente novamente!"); //Setta um atributo com o erro
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp"); //Vai para a página de erro
            rd.forward(req, resp); //Vai para a página de erro
        }
    }


    // ===================================================================================
    //   Método para BUSCAR o funcionário (mostra os VALORES ANTIGOS na tela de edição)
    // ===================================================================================

    protected void buscarFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FuncionarioDTO funcionarioDTO; //Settando o id no FuncionarioDTO
        String idParam;
        int funcionarioId;
        RequestDispatcher rd; //Encaminhar ao documento alterarFuncionario.jsp

        idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("erro", "ID do funcionário para busca não fornecido.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
            return;
        }

        try {
            funcionarioId = Integer.parseInt(idParam);

            funcionarioDTO = new FuncionarioDTO(); //Settando o id no FuncionarioDTO
            funcionarioDTO.setId(funcionarioId);

            funcionarioDAO.buscarPorId(funcionarioDTO); //No mesmo objeto, setta os valores encontrados

            if (funcionarioDTO.getNome() != null && !funcionarioDTO.getNome().isEmpty()) {
                req.setAttribute("funcionario", funcionarioDTO); //Setta em um novo atributo para o JSP pegar os valores

                //Encaminhar ao documento alterarFuncionario.jsp
                rd = req.getRequestDispatcher("/paginasCrud/funcionario/funcionarioAlterar.jsp");
                rd.forward(req, resp);
            } else {
                req.setAttribute("erro", "Funcionário com ID " + funcionarioId + " não encontrado.");
                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
                rd.forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID inválido fornecido. Deve ser um número inteiro.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
        }
    }


    // ===============================================================
    //     Método para ALTERAR o funcionário (com os VALORES NOVOS)
    // ===============================================================

    protected void alterarFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        FuncionarioDTO funcionarioDTO; // Settando os valores no funcionarioDTO ---> ---> --> usuário não pode mudar a eta
        String idParam;
        String nome;
        String email;
        String senha;
        String dataStrAdm; //Convertendo de String para Date
        String dataStrNasc; //Convertendo novamente
        String nomeCargo;
        int funcionarioId;
        List<String> erros; // ===== Validação com classe Validador =====
        List<String> errosSenha;
        String senhaCrip; // Criptografar novamente antes de salvar
        int resultado; // Chamar o DAO com o id já settado no DTO
        RequestDispatcher rd;

        // Inicializações
        erros = new ArrayList<>(); // ===== Validação com classe Validador =====

        // Parâmetros do formulário
        idParam = req.getParameter("id");
        nome = req.getParameter("nome");
        email = req.getParameter("email");
        senha = req.getParameter("senha");
        dataStrAdm = req.getParameter("dataAdmissao"); //Convertendo de String para Date
        dataStrNasc = req.getParameter("dataNascimento"); //Convertendo novamente
        nomeCargo = req.getParameter("nomeCargo");

        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("erro", "ID do funcionário para alteração não fornecido.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
            return;
        }

        try {
            // Conversão de ID e set de campos
            funcionarioId = Integer.parseInt(idParam);

            funcionarioDTO = new FuncionarioDTO(); // Settando os valores no funcionarioDTO ---> ---> --> usuário não pode mudar a eta
            funcionarioDTO.setId(funcionarioId);
            funcionarioDTO.setNome(nome);
            funcionarioDTO.setEmail(email);
            funcionarioDTO.setSenha(senha);

            // Conversão de Datas
            funcionarioDTO.setDataAdmissao(java.sql.Date.valueOf(dataStrAdm)); //Convertendo de String para Date
            funcionarioDTO.setDataNascimento(java.sql.Date.valueOf(dataStrNasc)); //Convertendo novamente

            // Buscar o id do cargo pelo nome
            funcionarioDTO.setIdCargo(cargoDAO.buscarIdPorNome(nomeCargo)); //Nesse método é retornado o id do cargo
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID inválido fornecido. Deve ser um número inteiro.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
            return;
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", "Formato de data inválido. Use YYYY-MM-DD.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
            return;
        }

        // ===== Validação com classe Validador =====

        if (!Validador.naoVazio(funcionarioDTO.getNome())) {
            erros.add("O nome do funcionário não pode estar vazio.");
        }

        if (!Validador.validarEmail(funcionarioDTO.getEmail())) {
            erros.add("O e-mail informado é inválido.");
        }

//        if (funcionarioDTO.getDataNascimento() == null || !Validador.validarData(funcionarioDTO.getDataNascimento())) {
//            erros.add("A data de nascimento é inválida.");
//        }
//
//        if (funcionarioDTO.getDataAdmissao() == null || !Validador.validarData(funcionarioDTO.getDataAdmissao())) {
//            erros.add("A data de admissão é inválida.");
//        }

        errosSenha = Validador.validarSenha(funcionarioDTO.getSenha());
        erros.addAll(errosSenha);

        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
            return;
        }
        // ===== Fim da validação =====

        // Criptografar novamente antes de salvar
        senhaCrip = SenhaHash.hashSenha(funcionarioDTO.getSenha());
        funcionarioDTO.setSenha(senhaCrip);

        // Chamar o DAO com o id já settado no DTO
        resultado = funcionarioDAO.alterarFuncionario(funcionarioDTO); //Dentro do objeto, é settado os atributos

        // Tratar o resultado
        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
        } else {
            req.setAttribute("erro", "Não foi possível alterar o funcionário! Verifique os campos e tente novamente.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
        }
    }


    // ===============================================================
    //       Método para REMOVER o funcionário (pelo ID pego)
    // ===============================================================

    protected void removerFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
        String idParam;
        int id;
        int resultado;
        RequestDispatcher rd;

        idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("erro", "ID do funcionário para remoção não fornecido.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
            return;
        }

        try {
            id = Integer.parseInt(idParam);
            resultado = funcionarioDAO.removerFuncionario(id);

            if (resultado == 1) {
                // Atualiza a lista de produtos na mesma página
                resp.sendRedirect(req.getContextPath() + "/ServletFuncionario?action=mainFuncionario");
            } else {
                // Página de erro
                req.setAttribute("erro", "Não foi possível remover o funcionário. ID pode não existir.");
                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
                rd.forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID inválido fornecido para remoção.");
            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(req, resp);
        }
    }


    // ===============================================================
    //     Método para FILTRAR o funcionário (por coluna e valor)
    // ===============================================================

    protected void filtrarFuncionario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<FuncionarioDTO> lista;
        String coluna;
        String pesquisa;
        RequestDispatcher rd;

        coluna = req.getParameter("nome_coluna");
        pesquisa = req.getParameter("pesquisa");

        if (coluna == null || pesquisa == null || coluna.trim().isEmpty() || pesquisa.trim().isEmpty()) {
            listarFuncionarios(req, resp);
            return;
        }

        lista = funcionarioDAO.filtroBuscaPorColuna(coluna, pesquisa);

        req.setAttribute("funcionarios", lista);
        rd = req.getRequestDispatcher("/paginasCrud/funcionario/funcionarioIndex.jsp");
        rd.forward(req, resp);
    }
}