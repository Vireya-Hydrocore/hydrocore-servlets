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
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletEta", "/mainEta", "/createEta", "/selectEta", "/updateEta", "/deleteEta", "/filtroEta"}, name = "ServletEta")
public class ServletEta extends HttpServlet {

    private EtaDAO etaDAO = new EtaDAO();
    private List<String> erros = new ArrayList<>();

    // Variáveis de escopo de método movidas para o topo (declaradas)
    private String action;
    private List<EtaDTO> listEtas;
    private RequestDispatcher rd;
    private EtaDTO etaDTO;
    private AdminDTO adminDTO;
    private int resultado;

    // Variáveis da sessão do método cadastrarEta, movidas para o topo (declaradas)
    private HttpSession session;
    private String nome;
    private String email;
    private String senhaDigitada;
    private String cnpj;
    private String telefone;
    private int capacidade;


    // ===============================================================
    //             Método doGet (atributos passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        action = req.getParameter("action"); //Vem com a ação do usuário

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
            erros.clear();
            erros.add("Ocorreu um erro ao processar sua solicitação de ETA.");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erros/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //          Método doPost (atributos passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        action = req.getParameter("action");

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar etas (ou redirecionar)
            listarEtas(req, resp);
            return;
        }

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
            erros.clear();
            erros.add("Erro inesperado ao processar a ação de ETA.");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erros/erro.jsp").forward(req, resp);
        }
    }


    // =================== MÉTODOS AUXILIARES ========================


    // ===============================================================
    //                  Método para LISTAR as ETAs
    // ===============================================================

    protected void listarEtas(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        listEtas = etaDAO.listarEtas(); //List de objetos retornados na query

        req.setAttribute("etas", listEtas); //Devolve a lista de ETAs encontradas em um novo atributo, para a pagina JSP

        rd = req.getRequestDispatcher("/assets/pages/eta/etaIndex.jsp"); //Envia para a página principal
        rd.forward(req, resp);
    }


    // ===============================================================
    //       Método para CADASTRAR uma ETA (ocorre no cadastro)
    // ===============================================================

    private void cadastrarEta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        session = request.getSession();
        nome = (String) session.getAttribute("nome");
        email = (String) session.getAttribute("email");
        senhaDigitada = (String) session.getAttribute("senha");
        cnpj= (String) session.getAttribute("cnpj");
        telefone = (String) session.getAttribute("telefone");
        capacidade = (Integer) session.getAttribute("capacidade");

        //Criando um DTO de ETA para armazenar os valores inseridos
        etaDTO = new EtaDTO();
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
        erros = Validador.validarEta(nome, capacidade, telefoneFormatado, cnpjFormatado);
        if (!erros.isEmpty()) {
            request.setAttribute("erros", erros);
            rd = request.getRequestDispatcher("/assets/pages/erroLogin.jsp");
            rd.forward(request, response);
            return; // Interrompe execução se houver erros
        }

        //Criando DTO do Admin vinculado à ETA
        adminDTO = new AdminDTO();
        adminDTO.setNome(nome);
        adminDTO.setEmail(email);

        // ===== VALIDAÇÃO ADMIN (senha) =====
        erros = Validador.validarSenha(senhaDigitada);
        if (!erros.isEmpty()) {
            request.setAttribute("erros", erros);
            rd = request.getRequestDispatcher("/assets/pages/erroLogin.jsp");
            rd.forward(request, response);
            return;
        }

        //Se estiver tudo certo, criptografa a senha
        String senhaCrip = SenhaHash.hashSenha(senhaDigitada);
        adminDTO.setSenha(senhaCrip);

        //Chama DAO
        // EtaDAO etaDAO = new EtaDAO(); // Removido por já estar instanciado no topo
        resultado = etaDAO.cadastrarEta(etaDTO, adminDTO);

        //Se o cadastro estiver correto, manda para a pagina de login
        if (resultado > 0) {
            response.sendRedirect(request.getContextPath() + "/assets/pages/landingpage/login.jsp");
        } else {
            erros.add("Não foi possível cadastrar esta ETA, verifique os campos e tente novamente.");
            request.setAttribute("erros", erros);
            rd = request.getRequestDispatcher("/assets/pages/erroLogin.jsp");
            rd.forward(request, response);
        }
    }


    // ===============================================================
    //       Método para ALTERAR a ETA (com os VALORES NOVOS)
    // ===============================================================

    protected void alterarEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando os valores no etaDTO
        etaDTO = new EtaDTO();
        etaDTO.setId(Integer.parseInt(req.getParameter("id")));
        etaDTO.setNome(req.getParameter("nome"));
        etaDTO.setCapacidade(Integer.parseInt(req.getParameter("capacidade")));
        String telefone =req.getParameter("telefone");
        String cnpj =req.getParameter("cnpj");

        //Settando o que tem relação ao endereço
        etaDTO.setRua(req.getParameter("rua"));
        etaDTO.setBairro(req.getParameter("bairro"));
        etaDTO.setCidade(req.getParameter("cidade"));
        etaDTO.setEstado(req.getParameter("estado"));
        String cep = req.getParameter("cep");
        etaDTO.setNumero(Integer.parseInt(req.getParameter("numero")));

        //Regex
        cep = cep.replaceAll("-", "");
        etaDTO.setCep(cep);

        telefone = telefone.replaceAll("\\D", "");
        etaDTO.setTelefone(telefone);

        cnpj = cnpj.replaceAll("\\D","");
        etaDTO.setCnpj(cnpj);

        // ===== VALIDAÇÃO ETA =====
        erros = Validador.validarEta(
                etaDTO.getNome(),
                etaDTO.getCapacidade(),
                etaDTO.getTelefone(),
                etaDTO.getCnpj()
        );

        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erros/erro.jsp").forward(req, resp);
            return;
        }

        //Chamando o DAO
        resultado = etaDAO.alterarEta(etaDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletEta?action=mainEta");
        } else {
            erros.add("Não foi possível alterar a ETA! Verifique os campos e tente novamente.");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erros/erro.jsp").forward(req, resp);
        }
    }


    // ===================================================================================
    //       Método para BUSCAR uma ETA (mostra os VALORES ANTIGOS na tela de edição)
    // ===================================================================================

    protected void buscarEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando o id na etaDTO
        etaDTO = new EtaDTO();
        etaDTO.setId(Integer.parseInt(req.getParameter("id")));

        etaDAO.buscarPorId(etaDTO); //No mesmo objeto, setta os valores encontrados

        req.setAttribute("eta", etaDTO); //Setta em um novo atributo para o JSP pegar os valores

        //Encaminhar ao documento alterarEta.jsp
        rd = req.getRequestDispatcher("/assets/pages/eta/etaAlterar.jsp");
        rd.forward(req, resp);
    }


    // ===============================================================
    //        Método para FILTRAR a ETA (por coluna e valor)
    // ===============================================================

    protected void filtrarEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Armazena a query filtrada em um novo List
        listEtas = etaDAO.filtroBuscaPorColuna(req.getParameter("nome_coluna"), req.getParameter("pesquisa"));

        req.setAttribute("etas", listEtas); //Devolve a lista de ETAs encontradas em um novo atributo
        rd = req.getRequestDispatcher("/assets/pages/eta/etaIndex.jsp");
        rd.forward(req, resp);
    }
}