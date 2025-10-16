package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dto.AdminDTO;
import com.example.servletsvireya.dto.EtaDTO;
import com.example.servletsvireya.util.SenhaHash;
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

    // DAO inicializado como final da classe para reutilização (boa prática de performance)
    private final EtaDAO etaDAO = new EtaDAO();


    // ===============================================================
    //             Método doGet (atributos passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Se action for null, assume-se o comportamento padrão (listar/mainEta)
        String action = req.getParameter("action");
        if (action == null) {
            action = "mainEta";
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
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/eta/etaIndex.jsp");
            }
        } catch (Exception e) {
            // Loga a exceção e retorna um erro genérico
            System.err.println("Erro inesperado no doGet do ServletEta: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("erro", "Ocorreu um erro interno ao processar a requisição.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //          Método doPost (atributos passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "createEta":
                    cadastrarEta(req, resp);
                    break;
                case "updateEta":
                    alterarEta(req, resp);
                    break;
                case "deleteEta": // Adicionado tratamento para delete
                    removerEta(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/ServletEta?action=mainEta");
            }
        } catch (Exception e) {
            // Loga a exceção e retorna um erro genérico
            System.err.println("Erro inesperado no doPost do ServletEta: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("erro", "Ocorreu um erro interno ao processar a requisição.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // =================== MÉTODOS AUXILIARES ========================


    // Método para LISTAR as ETAs
    protected void listarEtas(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<EtaDTO> listEtas = etaDAO.listarEtas();

        req.setAttribute("etas", listEtas);

        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/eta/etaIndex.jsp");
        rd.forward(req, resp);
    }


    // Método para CADASTRAR uma ETA
    private void cadastrarEta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Usar false se a sessão deve existir

        if (session == null) {
            // A sessão expirou ou não foi criada corretamente no passo anterior
            request.setAttribute("erro", "Sessão expirada. Por favor, reinicie o cadastro.");
            response.sendRedirect(request.getContextPath() + "/paginasCrud/erro.jsp");
            return;
        }

        // Recuperando dados da sessão (com verificação de null/casting seguro)
        String nome = (String) session.getAttribute("nome");
        String email = (String) session.getAttribute("email");
        String senha = (String) session.getAttribute("senha");
        String cnpj = (String) session.getAttribute("cnpj");
        String telefone = (String) session.getAttribute("telefone");
        Integer capacidadeWrapper = (Integer) session.getAttribute("capacidade");
        String cepSessao = (String) session.getAttribute("cep");

        // Parâmetros do formulário
        String numeroParam = request.getParameter("numero");
        String rua = request.getParameter("rua");
        String bairro = request.getParameter("bairro");
        String cidade = request.getParameter("cidade");
        String estado = request.getParameter("estado");

        // Valores numéricos com tratamento de exceção
        if (capacidadeWrapper==null){
            request.setAttribute("erro", "ID da ETA ou dados de sessão para alteração não fornecidos.");
            request.getRequestDispatcher("/paginasCrud/erro.jsp").forward(request, response);
            return;
        }
        int capacidade = capacidadeWrapper;

        if (!Validador.ehPositivo(capacidade)) {
            request.setAttribute("erro", "A capacidade não pode ser negativa.");
            request.getRequestDispatcher("/paginasCrud/erro.jsp").forward(request, response);
            return;
        }
        int numero;

        try {
            if (numeroParam == null || numeroParam.isEmpty()) {
                throw new NumberFormatException("Número do endereço não fornecido.");
            }
            numero = Integer.parseInt(numeroParam);
        } catch (NumberFormatException e) {
            request.setAttribute("erro", "O número do endereço deve ser um valor inteiro válido.");
            request.getRequestDispatcher("/paginasCrud/erro.jsp").forward(request, response);
            return;
        }
        // ===== VALIDAÇÃO ETA (incluindo campos da sessão) =====
        List<String> erros = Validador.validarEta(nome, capacidade, telefone, cnpj, cepSessao);
        if (!erros.isEmpty()) {
            request.setAttribute("erros", erros);
            RequestDispatcher rd = request.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(request, response);
            return;
        }

        // Criação e preenchimento do DTO
        EtaDTO etaDTO = new EtaDTO();
        etaDTO.setNome(nome);
        etaDTO.setCapacidade(capacidade);
        etaDTO.setRua(rua);
        etaDTO.setNumero(numero);
        etaDTO.setBairro(bairro);
        etaDTO.setCidade(cidade);
        etaDTO.setEstado(estado);




        // Aplicação do REGEX (usando os valores formatados)
        String regex = "\\D";
        String cnpjFormatado =cnpj.replaceAll(regex, "");
        String telefoneFormatado = telefone.replaceAll(regex, "");
        String cepFormatado =  cepSessao.replaceAll(regex, "");
        etaDTO.setCnpj(cnpjFormatado);
        etaDTO.setTelefone(telefoneFormatado);
        etaDTO.setCep(cepFormatado);

        // Criando DTO do Admin
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setNome(nome);
        adminDTO.setEmail(email);
        String senhaDigitada = senha;

        // ===== VALIDAÇÃO ADMIN (senha) =====
        var errosSenha = Validador.validarSenha(senhaDigitada);
        if (!errosSenha.isEmpty()) {
            request.setAttribute("errosSenha", errosSenha);
            RequestDispatcher rd = request.getRequestDispatcher("/paginasCrud/erroSenha.jsp");
            rd.forward(request, response);
            return;
        }

        // Criptografa a senha
        String senhaCrip = SenhaHash.hashSenha(senhaDigitada);
        adminDTO.setSenha(senhaCrip);

        // Chama DAO (usando a instância final da classe)
        int idEta = etaDAO.cadastrarEta(etaDTO, adminDTO);

        // Se o cadastro estiver correto, invalida a sessão e redireciona
        if (idEta > 0) {
            session.invalidate(); // Boa prática: invalida a sessão após o uso
            response.sendRedirect(request.getContextPath() + "/paginasCrud/admin/logar.jsp");
        } else {
            request.setAttribute("erro", "Erro ao cadastrar ETA. Verifique os logs do servidor.");
            RequestDispatcher rd = request.getRequestDispatcher("/paginasCrud/erro.jsp");
            rd.forward(request, response);
        }
    }


    // Método para ALTERAR a ETA (com os VALORES NOVOS)
    protected void alterarEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String nome = req.getParameter("nome");
        String capacidadeParam = req.getParameter("capacidade");
        String rua = req.getParameter("rua");
        String bairro = req.getParameter("bairro");
        String cidade = req.getParameter("cidade");
        String estado = req.getParameter("estado");
        String numeroParam = req.getParameter("numero");

        HttpSession session = req.getSession(false); // Obtém a sessão, mas não cria uma nova

        // Assumindo que telefone, cnpj e cep VÊM DA SESSÃO, conforme o código enviado
        String telefone = (session != null) ? (String) session.getAttribute("telefone") : null;
        String cnpj = (session != null) ? (String) session.getAttribute("cnpj") : null;
        String cep = (session != null) ? (String) session.getAttribute("cep") : null;

        if (idParam == null || idParam.isEmpty() || session == null) {
            req.setAttribute("erro", "ID da ETA ou dados de sessão para alteração não fornecidos.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        // 1. Robustez: Tratamento de NumberFormatException para todos os campos numéricos
        EtaDTO etaDTO = new EtaDTO();
        int capacidade;
        int numero;

        try {
            etaDTO.setId(Integer.parseInt(idParam));

            if (capacidadeParam == null || capacidadeParam.isEmpty()) {
                throw new NumberFormatException("Capacidade não fornecida.");
            }
            capacidade = Integer.parseInt(capacidadeParam);

            if (numeroParam == null || numeroParam.isEmpty()) {
                throw new NumberFormatException("Número do endereço não fornecido.");
            }
            numero = Integer.parseInt(numeroParam);

        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID, Capacidade e/ou Número do endereço devem ser números inteiros válidos.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        // Settando os demais valores no etaDTO
        etaDTO.setNome(nome);
        etaDTO.setCapacidade(capacidade);
        etaDTO.setRua(rua);
        etaDTO.setBairro(bairro);
        etaDTO.setCidade(cidade);
        etaDTO.setEstado(estado);
        etaDTO.setNumero(numero);

        // ===== VALIDAÇÃO ETA =====
        // Usa os valores brutos da sessão para validação
        List<String> erros = Validador.validarEta(
                etaDTO.getNome(),
                etaDTO.getCapacidade(),
                telefone, // Usando valor da sessão
                cnpj,     // Usando valor da sessão
                cep       // Usando valor da sessão
        );

        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        // Aplicação do REGEX (usando os valores formatados)
        String regex = "\\D";
        String cnpjFormatado = (cnpj != null) ? cnpj.replaceAll(regex, "") : "";
        String telefoneFormatado = (telefone != null) ? telefone.replaceAll(regex, "") : "";
        String cepFormatado = (cep != null) ? cep.replaceAll(regex, "") : "";
        etaDTO.setCnpj(cnpjFormatado);
        etaDTO.setTelefone(telefoneFormatado);
        etaDTO.setCep(cepFormatado);

        // Chamando o DAO (mantendo a lógica de passagem do DTO)
        int resultado = etaDAO.alterarEta(etaDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletEta?action=mainEta");
        } else {
            req.setAttribute("erro", "Não foi possível alterar a ETA! ID pode ser inválido ou campos duplicados.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // Método para BUSCAR uma ETA (mostra os VALORES ANTIGOS na tela de edição)
    protected void buscarEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        // 1. Robustez: Verifica se o parâmetro ID está presente
        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("erro", "ID da ETA para busca não fornecido.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        try {
            // 2. Robustez: Converte o ID, tratando NumberFormatException
            int etaId = Integer.parseInt(idParam);

            // Settando o id na etaDTO, mantendo a lógica de passagem do DTO
            EtaDTO etaDTO = new EtaDTO();
            etaDTO.setId(etaId);

            etaDAO.buscarPorId(etaDTO); // Assinatura original mantida: DTO entra e é preenchido.

            // 3. Clareza: Verifica se a busca foi bem-sucedida (assumindo que o nome é preenchido)
            if (etaDTO.getNome() != null && !etaDTO.getNome().isEmpty()) {
                req.setAttribute("eta", etaDTO);
                RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/eta/etaAlterar.jsp");
                rd.forward(req, resp);
            } else {
                req.setAttribute("erro", "ETA com ID " + etaId + " não encontrada.");
                req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            }

        } catch (NumberFormatException e) {
            // Tratamento específico para ID inválido
            req.setAttribute("erro", "ID inválido fornecido. Deve ser um número inteiro.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // Método para REMOVER a ETA (pelo ID pego)
    protected void removerEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        // 1. Robustez: Verifica se o parâmetro ID está presente
        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("erro", "ID da ETA para remoção não fornecido.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        try {
            // 2. Robustez: Converte o ID, tratando NumberFormatException
            int etaId = Integer.parseInt(idParam);

            // Instanciando objeto DTO e settando o id para remoção
            EtaDTO etaDTO = new EtaDTO();
            etaDTO.setId(etaId);

            int resultado = etaDAO.removerEta(etaDTO); // Assinatura original mantida

            if (resultado == 1) {
                resp.sendRedirect(req.getContextPath() + "/ServletEta?action=mainEta");
            } else {
                req.setAttribute("erro", "Não foi possível remover a ETA. Pode haver registros vinculados ou ID inválido.");
                req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID inválido fornecido para remoção.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // Método para FILTRAR a ETA (por coluna e valor)
    protected void filtrarEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String coluna = req.getParameter("nome_coluna");
        String pesquisa = req.getParameter("pesquisa");

        // Boas Práticas: Se o filtro estiver vazio, lista todos em vez de falhar.
        if (coluna == null || pesquisa == null || coluna.trim().isEmpty() || pesquisa.trim().isEmpty()) {
            listarEtas(req, resp);
            return;
        }

        List<EtaDTO> lista = etaDAO.filtroBuscaPorColuna(coluna, pesquisa);

        req.setAttribute("etas", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/eta/etaIndex.jsp");
        rd.forward(req, resp);
    }
}
//    package com.example.servletsvireya.controller;
//
//import com.example.servletsvireya.dao.EtaDAO;
//import com.example.servletsvireya.dto.AdminDTO;
//import com.example.servletsvireya.dto.EtaDTO;
//import com.example.servletsvireya.util.SenhaHash;
//import com.example.servletsvireya.util.Validador;
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//import java.io.IOException;
//import java.util.List;
//
//    @WebServlet(urlPatterns = {"/ServletEta", "/mainEta", "/createEta", "/selectEta", "/updateEta", "/deleteEta", "/filtroEta"}, name = "ServletEta")
//    public class ServletEta extends HttpServlet {
//
//        // DAO inicializado no topo da classe (mantido como final)
//        private final EtaDAO etaDAO = new EtaDAO();
//
//
//        // ===============================================================
//        //             Método doGet (atributos passam pela URL)
//        // ===============================================================
//
//        @Override
//        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//            String action;
//
//            action = req.getParameter("action");
//            if (action == null) {
//                action = "mainEta";
//            }
//
//            try {
//                switch (action){
//                    case "mainEta":
//                        listarEtas(req, resp);
//                        break;
//                    case "selectEta":
//                        buscarEta(req, resp);
//                        break;
//                    case "filtroEta":
//                        filtrarEta(req, resp);
//                        break;
//                    default:
//                        resp.sendRedirect(req.getContextPath() + "/paginasCrud/eta/etaIndex.jsp");
//                }
//            } catch (Exception e) {
//                System.err.println("Erro inesperado no doGet do ServletEta: " + e.getMessage());
//                e.printStackTrace();
//                req.setAttribute("erro", "Ocorreu um erro interno ao processar a requisição.");
//                req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
//            }
//        }
//
//
//        // ===============================================================
//        //          Método doPost (atributos passam pelo servidor)
//        // ===============================================================
//
//        @Override
//        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//            String action;
//
//            action = req.getParameter("action");
//            if (action == null) {
//                action = "";
//            }
//
//            try {
//                switch (action){
//                    case "createEta":
//                        cadastrarEta(req, resp);
//                        break;
//                    case "updateEta":
//                        alterarEta(req, resp);
//                        break;
//                    case "deleteEta":
//                        removerEta(req, resp);
//                        break;
//                    default:
//                        resp.sendRedirect(req.getContextPath() + "/ServletEta?action=mainEta");
//                }
//            } catch (Exception e) {
//                System.err.println("Erro inesperado no doPost do ServletEta: " + e.getMessage());
//                e.printStackTrace();
//                req.setAttribute("erro", "Ocorreu um erro interno ao processar a requisição.");
//                req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
//            }
//        }
//
//
//        // =================== MÉTODOS AUXILIARES ========================
//
//
//        // Método para LISTAR as ETAs
//        protected void listarEtas(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//            List<EtaDTO> listEtas;
//            RequestDispatcher rd;
//
//            listEtas = etaDAO.listarEtas();
//
//            req.setAttribute("etas", listEtas);
//
//            rd = req.getRequestDispatcher("/paginasCrud/eta/etaIndex.jsp");
//            rd.forward(req, resp);
//        }
//
//
//        // Método para CADASTRAR uma ETA
//        private void cadastrarEta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//            HttpSession session;
//            String nome;
//            String email;
//            String senha;
//            String cnpj;
//            String telefone;
//            Integer capacidadeWrapper;
//            String cepSessao;
//            String numeroParam;
//            String rua;
//            String bairro;
//            String cidade;
//            String estado;
//            int capacidade;
//            int numero;
//
//            EtaDTO etaDTO;
//            String regex;
//            String cnpjFormatado;
//            String telefoneFormatado;
//            String cepFormatado;
//            List<String> errosEta;
//
//            AdminDTO adminDTO;
//            String senhaDigitada;
//            List<String> errosSenha;
//            String senhaCrip;
//            int idEta;
//            RequestDispatcher rd;
//
//            session = request.getSession(false);
//
//            if (session == null) {
//                request.setAttribute("erro", "Sessão expirada. Por favor, reinicie o cadastro.");
//                response.sendRedirect(request.getContextPath() + "/paginasCrud/erro.jsp");
//                return;
//            }
//
//            // Recuperando dados da sessão
//            nome = (String) session.getAttribute("nome");
//            email = (String) session.getAttribute("email");
//            senha = (String) session.getAttribute("senha");
//            cnpj = (String) session.getAttribute("cnpj");
//            telefone = (String) session.getAttribute("telefone");
//            capacidadeWrapper = (Integer) session.getAttribute("capacidade");
//            cepSessao = (String) session.getAttribute("cep");
//
//            // Parâmetros do formulário
//            numeroParam = request.getParameter("numero");
//            rua = request.getParameter("rua");
//            bairro = request.getParameter("bairro");
//            cidade = request.getParameter("cidade");
//            estado = request.getParameter("estado");
//
//            // Tratamento de exceção para valores numéricos
//            capacidade = (capacidadeWrapper != null) ? capacidadeWrapper : 0;
//
//            try {
//                if (numeroParam == null || numeroParam.isEmpty()) {
//                    throw new NumberFormatException("Número do endereço não fornecido.");
//                }
//                numero = Integer.parseInt(numeroParam);
//            } catch (NumberFormatException e) {
//                request.setAttribute("erro", "O número do endereço deve ser um valor inteiro válido.");
//                rd = request.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(request, response);
//                return;
//            }
//
//            // Criação e preenchimento do DTO
//            etaDTO = new EtaDTO();
//            etaDTO.setNome(nome);
//            etaDTO.setCapacidade(capacidade);
//            etaDTO.setRua(rua);
//            etaDTO.setNumero(numero);
//            etaDTO.setBairro(bairro);
//            etaDTO.setCidade(cidade);
//            etaDTO.setEstado(estado);
//
//
//            // Validação ETA
//            errosEta = Validador.validarEta(nome, capacidade, telefone, cnpj, cepSessao);
//            if (!errosEta.isEmpty()) {
//                request.setAttribute("erros", errosEta);
//                rd = request.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(request, response);
//                return;
//            }
//
//            // Aplicação do REGEX
//            regex = "\\D";
//            cnpjFormatado = (cnpj != null) ? cnpj.replaceAll(regex,"") : "";
//            telefoneFormatado = (telefone != null) ? telefone.replaceAll(regex,"") : "";
//            cepFormatado = (cepSessao != null) ? cepSessao.replaceAll(regex,"") : "";
//            etaDTO.setCnpj(cnpjFormatado);
//            etaDTO.setTelefone(telefoneFormatado);
//            etaDTO.setCep(cepFormatado);
//
//            // Criando DTO do Admin
//            adminDTO = new AdminDTO();
//            adminDTO.setNome(nome);
//            adminDTO.setEmail(email);
//            senhaDigitada = senha;
//
//            // Validação ADMIN (senha)
//            errosSenha = Validador.validarSenha(senhaDigitada);
//            if (!errosSenha.isEmpty()) {
//                request.setAttribute("errosSenha", errosSenha);
//                rd = request.getRequestDispatcher("/paginasCrud/erroSenha.jsp");
//                rd.forward(request, response);
//                return;
//            }
//
//            // Criptografa a senha
//            senhaCrip = SenhaHash.hashSenha(senhaDigitada);
//            adminDTO.setSenha(senhaCrip);
//
//            // Chama DAO
//            idEta = etaDAO.cadastrarEta(etaDTO, adminDTO);
//
//            // Se o cadastro estiver correto, invalida a sessão e redireciona
//            if (idEta > 0) {
//                session.invalidate();
//                response.sendRedirect(request.getContextPath() + "/paginasCrud/admin/logar.jsp");
//            } else {
//                request.setAttribute("erro", "Erro ao cadastrar ETA. Verifique os logs do servidor.");
//                rd = request.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(request, response);
//            }
//        }
//
//
//        // Método para ALTERAR a ETA (com os VALORES NOVOS)
//        protected void alterarEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//            String idParam;
//            String nome;
//            String capacidadeParam;
//            String rua;
//            String bairro;
//            String cidade;
//            String estado;
//            String numeroParam;
//            HttpSession session;
//            String telefone;
//            String cnpj;
//            String cep;
//            EtaDTO etaDTO;
//            int capacidade;
//            int numero;
//            List<String> erros;
//            String regex;
//            String cnpjFormatado;
//            String telefoneFormatado;
//            String cepFormatado;
//            int resultado;
//            RequestDispatcher rd;
//
//            idParam = req.getParameter("id");
//            nome = req.getParameter("nome");
//            capacidadeParam = req.getParameter("capacidade");
//            rua = req.getParameter("rua");
//            bairro = req.getParameter("bairro");
//            cidade = req.getParameter("cidade");
//            estado = req.getParameter("estado");
//            numeroParam = req.getParameter("numero");
//            session = req.getSession(false);
//
//            // Assumindo que telefone, cnpj e cep VÊM DA SESSÃO, conforme o código enviado
//            telefone = (session != null) ? (String) session.getAttribute("telefone") : null;
//            cnpj = (session != null) ? (String) session.getAttribute("cnpj") : null;
//            cep = (session != null) ? (String) session.getAttribute("cep") : null;
//
//            if (idParam == null || idParam.isEmpty()) {
//                req.setAttribute("erro", "ID da ETA para alteração não fornecido.");
//                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(req, resp);
//                return;
//            }
//
//            if (session == null) {
//                req.setAttribute("erro", "Sessão inválida ou expirada para alteração.");
//                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(req, resp);
//                return;
//            }
//
//            // Tratamento de NumberFormatException para todos os campos numéricos
//            etaDTO = new EtaDTO();
//            try {
//                etaDTO.setId(Integer.parseInt(idParam));
//
//                if (capacidadeParam == null || capacidadeParam.isEmpty()) {
//                    throw new NumberFormatException("Capacidade não fornecida.");
//                }
//                capacidade = Integer.parseInt(capacidadeParam);
//
//                if (numeroParam == null || numeroParam.isEmpty()) {
//                    throw new NumberFormatException("Número do endereço não fornecido.");
//                }
//                numero = Integer.parseInt(numeroParam);
//
//            } catch (NumberFormatException e) {
//                req.setAttribute("erro", "ID, Capacidade e/ou Número do endereço devem ser números inteiros válidos.");
//                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(req, resp);
//                return;
//            }
//
//            // Settando os demais valores no etaDTO
//            etaDTO.setNome(nome);
//            etaDTO.setCapacidade(capacidade);
//            etaDTO.setRua(rua);
//            etaDTO.setBairro(bairro);
//            etaDTO.setCidade(cidade);
//            etaDTO.setEstado(estado);
//            etaDTO.setNumero(numero);
//
//            // Validação ETA
//            erros = Validador.validarEta(
//                    etaDTO.getNome(),
//                    etaDTO.getCapacidade(),
//                    telefone, // Usando valor da sessão
//                    cnpj,     // Usando valor da sessão
//                    cep       // Usando valor da sessão
//            );
//
//            if (!erros.isEmpty()) {
//                req.setAttribute("erros", erros);
//                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(req, resp);
//                return;
//            }
//
//            // Aplicação do REGEX (usando os valores formatados)
//            regex = "\\D";
//            cnpjFormatado = (cnpj != null) ? cnpj.replaceAll(regex,"") : "";
//            telefoneFormatado = (telefone != null) ? telefone.replaceAll(regex,"") : "";
//            cepFormatado = (cep != null) ? cep.replaceAll(regex,"") : "";
//            etaDTO.setCnpj(cnpjFormatado);
//            etaDTO.setTelefone(telefoneFormatado);
//            etaDTO.setCep(cepFormatado);
//
//            // Chamando o DAO
//            resultado = etaDAO.alterarEta(etaDTO);
//
//            if (resultado == 1) {
//                resp.sendRedirect(req.getContextPath() + "/ServletEta?action=mainEta");
//            } else {
//                req.setAttribute("erro", "Não foi possível alterar a ETA! ID pode ser inválido ou campos duplicados.");
//                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(req, resp);
//            }
//        }
//
//
//        // Método para BUSCAR uma ETA (mostra os VALORES ANTIGOS na tela de edição)
//        protected void buscarEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//            String idParam;
//            int etaId;
//            EtaDTO etaDTO;
//            RequestDispatcher rd;
//
//            idParam = req.getParameter("id");
//
//            if (idParam == null || idParam.isEmpty()) {
//                req.setAttribute("erro", "ID da ETA para busca não fornecido.");
//                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(req, resp);
//                return;
//            }
//
//            try {
//                etaId = Integer.parseInt(idParam);
//
//                etaDTO = new EtaDTO();
//                etaDTO.setId(etaId);
//
//                etaDAO.buscarPorId(etaDTO);
//
//                if (etaDTO.getNome() != null && !etaDTO.getNome().isEmpty()) {
//                    req.setAttribute("eta", etaDTO);
//                    rd = req.getRequestDispatcher("/paginasCrud/eta/etaAlterar.jsp");
//                    rd.forward(req, resp);
//                } else {
//                    req.setAttribute("erro", "ETA com ID " + etaId + " não encontrada.");
//                    rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                    rd.forward(req, resp);
//                }
//
//            } catch (NumberFormatException e) {
//                req.setAttribute("erro", "ID inválido fornecido. Deve ser um número inteiro.");
//                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(req, resp);
//            }
//        }
//
//
//        // Método para REMOVER a ETA (pelo ID pego)
//        protected void removerEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//            String idParam;
//            int etaId;
//            EtaDTO etaDTO;
//            int resultado;
//            RequestDispatcher rd;
//
//            idParam = req.getParameter("id");
//
//            if (idParam == null || idParam.isEmpty()) {
//                req.setAttribute("erro", "ID da ETA para remoção não fornecido.");
//                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(req, resp);
//                return;
//            }
//
//            try {
//                etaId = Integer.parseInt(idParam);
//
//                etaDTO = new EtaDTO();
//                etaDTO.setId(etaId);
//
//                resultado = etaDAO.removerEta(etaDTO);
//
//                if (resultado == 1) {
//                    resp.sendRedirect(req.getContextPath() + "/ServletEta?action=mainEta");
//                } else {
//                    req.setAttribute("erro", "Não foi possível remover a ETA. Pode haver registros vinculados ou ID inválido.");
//                    rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                    rd.forward(req, resp);
//                }
//            } catch (NumberFormatException e) {
//                req.setAttribute("erro", "ID inválido fornecido para remoção.");
//                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(req, resp);
//            }
//        }
//
//
//        // Método para FILTRAR a ETA (por coluna e valor)
//        protected void filtrarEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//            String coluna;
//            String pesquisa;
//            List<EtaDTO> lista;
//            RequestDispatcher rd;
//
//            coluna = req.getParameter("nome_coluna");
//            pesquisa = req.getParameter("pesquisa");
//
//            if (coluna == null || pesquisa == null || coluna.trim().isEmpty() || pesquisa.trim().isEmpty()) {
//                listarEtas(req, resp);
//                return;
//            }
//
//            lista = etaDAO.filtroBuscaPorColuna(coluna, pesquisa);
//
//            req.setAttribute("etas", lista);
//            rd = req.getRequestDispatcher("/paginasCrud/eta/etaIndex.jsp");
//            rd.forward(req, resp);
//        }
//    }
//}