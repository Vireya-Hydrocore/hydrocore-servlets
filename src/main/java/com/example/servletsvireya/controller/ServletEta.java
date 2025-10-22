package com.example.servletsvireya.controller;

import com.example.servletsvireya.dto.ProdutoDTO;
import com.example.servletsvireya.model.Eta;
import com.example.servletsvireya.util.SenhaHash;


import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dto.AdminDTO;
import com.example.servletsvireya.dto.EtaDTO;
import com.example.servletsvireya.util.Validador;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletEta", "/mainEta", "/createEta", "/selectEta", "/updateEta", "/deleteEta", "/filtroEta"}, name = "ServletEta")
public class ServletEta extends HttpServlet {

    private EtaDAO etaDAO = new EtaDAO();


    // ===============================================================
    //             Método doGet (atributos passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action"); //Vem com a ação do usuário

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar etas (ou redirecionar)
            listarEtas(req, resp);
            return;
        }

        try {
            switch (action) {
                case "mainEta":
                    listarEtas(req, resp);
                    break;
                case "selectEta":
                    buscarEta(req, resp);
                    break;
                case "filtroEta":
                    filtrarEta(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/assets/pages/eta/etaIndex.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erro", "Ocorreu um erro ao processar sua solicitação de ETA.");
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //          Método doPost (atributos passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");


        try {
            switch (action) {
                case "createEta":
                    cadastrarEta(req, resp);
                    break;
                case "updateEta":
                    alterarEta(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/ServletEta?action=mainEta");
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro inesperado ao processar a ação de ETA.");
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // =================== MÉTODOS AUXILIARES ========================


    // ===============================================================
    //                  Método para LISTAR as ETAs
    // ===============================================================

    protected void listarEtas(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<EtaDTO> listEtas = etaDAO.listarEtas(); //List de objetos retornados na query

        req.setAttribute("etas", listEtas); //Devolve a lista de ETAs encontradas em um novo atributo, para a pagina JSP

        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/eta/etaIndex.jsp"); //Envia para a página principal
        rd.forward(req, resp);
    }


    // ===============================================================
    //                Método para CADASTRAR uma ETA //////////////////
    // ===============================================================

    private void cadastrarEta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String nome = (String) session.getAttribute("nome");
        String email = (String) session.getAttribute("email");
        String senhaDigitada = (String) session.getAttribute("senha");
        String cnpj= (String) session.getAttribute("cnpj");
        String telefone = (String) session.getAttribute("telefone");
        int capacidade = (Integer) session.getAttribute("capacidade");

        //Criando um DTO de ETA para armazenar os valores inseridos
        EtaDTO etaDTO = new EtaDTO();
        etaDTO.setNome(nome); //Nome da ETA
        etaDTO.setCapacidade(capacidade);
        etaDTO.setRua(request.getParameter("rua"));
        etaDTO.setNumero(Integer.parseInt(request.getParameter("numero")));
        etaDTO.setBairro(request.getParameter("bairro"));
        etaDTO.setCidade(request.getParameter("cidade"));
        etaDTO.setEstado(request.getParameter("estado"));
        etaDTO.setCep(request.getParameter("cep"));

        //Aplicação do REGEX em CNPJ e telefone
        String regex = "\\D";
        String cnpjFormatado = cnpj.replaceAll(regex,"");
        String telefoneFormatado = telefone.replaceAll(regex,"");
        etaDTO.setCnpj(cnpjFormatado);
        etaDTO.setTelefone(telefoneFormatado);

        // ===== VALIDAÇÃO ETA =====
        List<String> erros = Validador.validarEta(nome, capacidade, telefoneFormatado, cnpjFormatado);
        if (!erros.isEmpty()) {
            request.setAttribute("erros", erros);
            RequestDispatcher rd = request.getRequestDispatcher("/assets/pages/erroLogin.jsp");
            rd.forward(request, response);
            return; // Interrompe execução se houver erros
        }

        //Criando DTO do Admin vinculado à ETA
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setNome(nome);
        adminDTO.setEmail(email);

        // ===== VALIDAÇÃO ADMIN (senha) =====
        var errosSenha = Validador.validarSenha(senhaDigitada);
        if (!errosSenha.isEmpty()) {
            request.setAttribute("erros", errosSenha);
            RequestDispatcher rd = request.getRequestDispatcher("/assets/pages/erroLogin.jsp");
            rd.forward(request, response);
            return;
        }

        //Se estiver tudo certo, criptografa a senha
        String senhaCrip = SenhaHash.hashSenha(senhaDigitada);
        adminDTO.setSenha(senhaCrip);

        //Chama DAO
        EtaDAO etaDAO = new EtaDAO();
        int idEta = etaDAO.cadastrarEta(etaDTO, adminDTO);

        //Se o cadastro estiver correto, manda para a pagina de login
        if (idEta > 0) {
            response.sendRedirect(request.getContextPath() + "/assets/pages/landingpage/login.jsp");
        } else {
            request.setAttribute("erros", "Erro ao cadastrar ETA.");
            RequestDispatcher rd = request.getRequestDispatcher("/assets/pages/erroLogin.jsp");
            rd.forward(request, response);
        }
    }


    // ===============================================================
    //       Método para ALTERAR a ETA (com os VALORES NOVOS)
    // ===============================================================

    protected void alterarEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando os valores no etaDTO
        EtaDTO etaDTO = new EtaDTO();
        etaDTO.setId(Integer.parseInt(req.getParameter("id")));
        etaDTO.setNome(req.getParameter("nome"));
        etaDTO.setCapacidade(Integer.parseInt(req.getParameter("capacidade")));
        etaDTO.setTelefone(req.getParameter("telefone"));
        etaDTO.setCnpj(req.getParameter("cnpj"));

        //Settando o que tem relação ao endereço
        etaDTO.setRua(req.getParameter("rua"));
        etaDTO.setBairro(req.getParameter("bairro"));
        etaDTO.setCidade(req.getParameter("cidade"));
        etaDTO.setEstado(req.getParameter("estado"));
        etaDTO.setNumero(Integer.parseInt(req.getParameter("numero")));
        etaDTO.setCep(req.getParameter("cep"));

        // ===== VALIDAÇÃO ETA =====
        List<String> erros = Validador.validarEta(
                etaDTO.getNome(),
                etaDTO.getCapacidade(),
                etaDTO.getTelefone(),
                etaDTO.getCnpj()
        );

        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
            return;
        }

        //Chamando o DAO
        int resultado = etaDAO.alterarEta(etaDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletEta?action=mainEta");
        } else {
            req.setAttribute("erros", "Não foi possível alterar a ETA! Verifique os campos e tente novamente.");
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ===================================================================================
    //       Método para BUSCAR uma ETA (mostra os VALORES ANTIGOS na tela de edição)
    // ===================================================================================

    protected void buscarEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando o id na etaDTO
        EtaDTO etaDTO = new EtaDTO();
        etaDTO.setId(Integer.parseInt(req.getParameter("id")));

        etaDAO.buscarPorId(etaDTO); //No mesmo objeto, setta os valores encontrados

        req.setAttribute("eta", etaDTO); //Setta em um novo atributo para o JSP pegar os valores

        //Encaminhar ao documento alterarEta.jsp
        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/eta/etaAlterar.jsp");
        rd.forward(req, resp);
    }


    // ===============================================================
    //          Método para REMOVER a ETA (pelo ID pego)
    // ===============================================================


    // ===============================================================
    //        Método para FILTRAR a ETA (por coluna e valor)
    // ===============================================================

    protected void filtrarEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Armazena a query filtrada em um novo List
        List<EtaDTO> lista = etaDAO.filtroBuscaPorColuna(req.getParameter("nome_coluna"), req.getParameter("pesquisa"));

        req.setAttribute("etas", lista); //Devolve a lista de ETAs encontradas em um novo atributo
        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/eta/etaIndex.jsp");
        rd.forward(req, resp);
    }
}
