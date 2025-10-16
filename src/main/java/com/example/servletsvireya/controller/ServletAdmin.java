package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.util.SenhaHash;
import com.example.servletsvireya.dao.AdminDAO;
import com.example.servletsvireya.dto.AdminDTO;
import com.example.servletsvireya.util.Validador;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletAdmin", "/mainAdmin", "/createAdmin", "/selectAdmin", "/updateAdmin", "/deleteAdmin", "/filtroAdmin"}, name = "/ServletAdmin")
public class ServletAdmin extends HttpServlet {

    // Mantido como final para garantir uma única instância de DAO por Servlet (boa prática para DAOs thread-safe)
    private final AdminDAO adminDAO = new AdminDAO();
    private final EtaDAO etaDAO = new EtaDAO();

    // ===============================================================
    //            Método doGet (atributos passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Se action for null, assume-se o comportamento padrão (listar/mainAdmin)
        String action = req.getParameter("action");
        if (action == null) {
            action = "mainAdmin";
        }

        try {
            switch (action) {
                case "mainAdmin":
                    listarAdmins(req, resp);
                    break;
                case "selectAdmin":
                    buscarAdmin(req, resp);
                    break;
                case "filtroAdmin":
                    filtrarAdmin(req, resp);
                    break;
                default:
                    // Ação não mapeada: Redireciona para a página principal.
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/admin/indexAdmin.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ===============================================================
    //          Método doPost (atributos passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Se action for null, assume-se o comportamento padrão (listar/mainAdmin)
        String action = req.getParameter("action");
        if (action == null) {
            action = "mainAdmin";
        }

        try {
            switch (action) {
                case "createAdmin":
                    inserirAdmin(req, resp);
                    break;
                case "updateAdmin":
                    alterarAdmin(req, resp);
                    break;
                case "deleteAdmin":
                    removerAdmin(req, resp);
                    break;
                case "mainAdmin": // Garante o fluxo padrão se a ação não for reconhecida ou for "mainAdmin"
                default:
                    resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ==================== MÉTODOS AUXILIARES =======================


    // Método para LISTAR os admins
    protected void listarAdmins(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<AdminDTO> lista = adminDAO.listarAdmin();

        req.setAttribute("admins", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/admin/indexAdmin.jsp");
        rd.forward(req, resp);
    }


    // Método para BUSCAR um admin (mostra os VALORES ANTIGOS na tela de edição)
    protected void buscarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        // 1. Robustez: Verifica se o parâmetro ID está presente
        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("erro", "ID do administrador para busca não fornecido.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        try {
            // 2. Robustez: Converte o ID para int, tratando NumberFormatException
            int adminId = Integer.parseInt(idParam);

            // Instanciando e settando o ID, conforme sua lógica original
            AdminDTO adminDTO = new AdminDTO();
            adminDTO.setId(adminId);

            // Executando o método buscarPorId (passando o DTO)
            adminDAO.buscarPorId(adminDTO); // Assinatura original mantida: DTO entra e é preenchido no método do DAO.

            // 3. Clareza: Verifica se o DTO foi preenchido (se a busca foi bem-sucedida)
            if (adminDTO.getNome() != null) { // Assumindo que o nome será preenchido se o registro for encontrado
                req.setAttribute("admin", adminDTO);
                RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/admin/alterarAdmin.jsp");
                rd.forward(req, resp);
            } else {
                req.setAttribute("erro", "Administrador com ID " + adminId + " não encontrado.");
                req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            }

        } catch (NumberFormatException e) {
            // Tratamento específico para ID inválido
            req.setAttribute("erro", "ID inválido fornecido. Deve ser um número inteiro.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // Método para INSERIR um admin
    protected void inserirAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Criado um DTO para armazenar os valores inseridos
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setNome(req.getParameter("nome"));
        adminDTO.setEmail(req.getParameter("email"));
        String senhaDigitada = req.getParameter("senha");
        String nomeEta = req.getParameter("nomeEta");

        // Busca do id da ETA usando a instância de EtaDAO da classe
        adminDTO.setIdEta(etaDAO.buscarIdPorNome(nomeEta));
        adminDTO.setNomeEta(nomeEta);

        // ===== VALIDAÇÃO =====
        List<String> erros = Validador.validarAdmin(adminDTO.getNome(), adminDTO.getEmail(), senhaDigitada);

        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        // Criptografando a senha
        adminDTO.setSenha(SenhaHash.hashSenha(senhaDigitada));

        int resultado = adminDAO.inserirAdmin(adminDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
        } else {
            req.setAttribute("erro", "Não foi possível inserir esse admin. Verifique os campos e tente novamente!");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // Método para ALTERAR o admin (com os VALORES NOVOS)
    protected void alterarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String senhaDigitada = req.getParameter("senha");

        // 1. Robustez: Verifica se o parâmetro ID está presente
        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("erro", "ID do administrador para alteração não fornecido.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        try {
            // 2. Robustez: Converte o ID para int, tratando NumberFormatException
            int adminId = Integer.parseInt(idParam);

            // Settando os valores no adminDTO, mantendo a lógica de passagem do DTO
            AdminDTO adminDTO = new AdminDTO();
            adminDTO.setId(adminId); // ID é essencial para a alteração
            adminDTO.setNome(req.getParameter("nome"));
            adminDTO.setEmail(req.getParameter("email"));

            // ===== VALIDAÇÃO =====
            List<String> erros = Validador.validarAdmin(adminDTO.getNome(), adminDTO.getEmail(), senhaDigitada);

            if (!erros.isEmpty()) {
                req.setAttribute("erros", erros);
                req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
                return;
            }

            // Criptografando a senha antes de salvar
            adminDTO.setSenha(SenhaHash.hashSenha(senhaDigitada));

            int resultado = adminDAO.alterarAdmin(adminDTO);

            if (resultado == 1) {
                resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
            } else {
                req.setAttribute("erro", "Não foi possível alterar o administrador. ID ou dados inválidos.");
                req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID inválido fornecido para alteração.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // Método para REMOVER o admin (pelo ID pego)
    protected void removerAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        // 1. Robustez: Verifica se o parâmetro ID está presente
        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("erro", "ID do administrador para remoção não fornecido.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        try {
            // 2. Robustez: Converte o ID para int, tratando NumberFormatException
            int adminId = Integer.parseInt(idParam);

            // Instanciando e settando o ID no DTO, mantendo a lógica de passagem do DTO
            AdminDTO adminDTO = new AdminDTO();
            adminDTO.setId(adminId);

            int resultado = adminDAO.removerAdmin(adminDTO); // Assinatura original mantida

            if (resultado == 1) {
                resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
            } else {
                req.setAttribute("erro", "Não foi possível remover este Admin. ID pode não existir.");
                req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID inválido fornecido para remoção.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // Método para FILTRAR o admin (por coluna e valor)
    protected void filtrarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String coluna = req.getParameter("nome_coluna");
        String pesquisa = req.getParameter("pesquisa");

        // Boas Práticas: Se o filtro estiver vazio, lista todos em vez de falhar.
        if (coluna == null || pesquisa == null || coluna.trim().isEmpty() || pesquisa.trim().isEmpty()) {
            listarAdmins(req, resp);
            return;
        }

        List<AdminDTO> lista = adminDAO.filtroBuscaPorColuna(coluna, pesquisa);

        req.setAttribute("admins", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/admin/indexAdmin.jsp");
        rd.forward(req, resp);
    }
}
//package com.example.servletsvireya.controller;
//
//        import com.example.servletsvireya.dao.EtaDAO;
//        import com.example.servletsvireya.util.SenhaHash;
//        import com.example.servletsvireya.dao.AdminDAO;
//        import com.example.servletsvireya.dto.AdminDTO;
//        import com.example.servletsvireya.util.Validador;
//        import jakarta.servlet.RequestDispatcher;
//        import jakarta.servlet.ServletException;
//        import jakarta.servlet.http.HttpServlet;
//        import jakarta.servlet.http.HttpServletRequest;
//        import jakarta.servlet.http.HttpServletResponse;
//        import jakarta.servlet.annotation.WebServlet;
//
//        import java.io.IOException;
//        import java.util.List;
//
//@WebServlet(urlPatterns = {"/ServletAdmin", "/mainAdmin", "/createAdmin", "/selectAdmin", "/updateAdmin", "/deleteAdmin", "/filtroAdmin"}, name = "/ServletAdmin")
//public class ServletAdmin extends HttpServlet {
//
//    // DAOs inicializados no topo da classe (mantidos como final)
//    private final AdminDAO adminDAO = new AdminDAO();
//    private final EtaDAO etaDAO = new EtaDAO();
//
//    // ===============================================================
//    //            Método doGet (atributos passam pela URL)
//    // ===============================================================
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action;
//
//        action = req.getParameter("action");
//        if (action == null) {
//            action = "mainAdmin";
//        }
//
//        try {
//            switch (action) {
//                case "mainAdmin":
//                    listarAdmins(req, resp);
//                    break;
//                case "selectAdmin":
//                    buscarAdmin(req, resp);
//                    break;
//                case "filtroAdmin":
//                    filtrarAdmin(req, resp);
//                    break;
//                default:
//                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/admin/indexAdmin.jsp");
//            }
//        } catch (Exception e) {
//            System.err.println("Erro no doGet: " + e.getMessage());
//            e.printStackTrace();
//            req.setAttribute("erro", "Ocorreu um erro interno no sistema.");
//            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
//        }
//    }
//
//
//    // ===============================================================
//    //          Método doPost (atributos passam pelo servidor)
//    // ===============================================================
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action;
//
//        action = req.getParameter("action");
//        if (action == null) {
//            action = "mainAdmin";
//        }
//
//        try {
//            switch (action) {
//                case "createAdmin":
//                    inserirAdmin(req, resp);
//                    break;
//                case "updateAdmin":
//                    alterarAdmin(req, resp);
//                    break;
//                case "deleteAdmin":
//                    removerAdmin(req, resp);
//                    break;
//                default:
//                    resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
//            }
//        } catch (Exception e) {
//            System.err.println("Erro no doPost: " + e.getMessage());
//            e.printStackTrace();
//            req.setAttribute("erro", "Ocorreu um erro interno ao processar a requisição.");
//            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
//        }
//    }
//
//
//    // ==================== MÉTODOS AUXILIARES =======================
//
//
//    // Método para LISTAR os admins
//    protected void listarAdmins(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        List<AdminDTO> lista;
//        RequestDispatcher rd;
//
//        lista = adminDAO.listarAdmin();
//
//        req.setAttribute("admins", lista);
//        rd = req.getRequestDispatcher("/paginasCrud/admin/indexAdmin.jsp");
//        rd.forward(req, resp);
//    }
//
//
//    // Método para BUSCAR um admin (mostra os VALORES ANTIGOS na tela de edição)
//    protected void buscarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String idParam;
//        int adminId;
//        AdminDTO adminDTO;
//        RequestDispatcher rd;
//
//        idParam = req.getParameter("id");
//
//        if (idParam == null || idParam.isEmpty()) {
//            req.setAttribute("erro", "ID do administrador não fornecido.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//            return;
//        }
//
//        try {
//            adminId = Integer.parseInt(idParam);
//
//            adminDTO = new AdminDTO();
//            adminDTO.setId(adminId);
//
//            adminDAO.buscarPorId(adminDTO); // Assinatura original mantida
//
//            if (adminDTO.getNome() != null && !adminDTO.getNome().isEmpty()) {
//                req.setAttribute("admin", adminDTO);
//                rd = req.getRequestDispatcher("/paginasCrud/admin/alterarAdmin.jsp");
//                rd.forward(req, resp);
//            } else {
//                req.setAttribute("erro", "Administrador com ID " + adminId + " não encontrado.");
//                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(req, resp);
//            }
//
//        } catch (NumberFormatException e) {
//            req.setAttribute("erro", "ID inválido fornecido. Deve ser um número inteiro.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//        }
//    }
//
//
//    // Método para INSERIR um admin
//    protected void inserirAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        AdminDTO adminDTO;
//        String nome;
//        String email;
//        String senhaDigitada;
//        String nomeEta;
//        int idEta;
//        List<String> erros;
//        String senhaCrip;
//        int resultado;
//        RequestDispatcher rd;
//
//        adminDTO = new AdminDTO();
//        nome = req.getParameter("nome");
//        email = req.getParameter("email");
//        senhaDigitada = req.getParameter("senha");
//        nomeEta = req.getParameter("nomeEta");
//
//        adminDTO.setNome(nome);
//        adminDTO.setEmail(email);
//
//        idEta = etaDAO.buscarIdPorNome(nomeEta);
//        adminDTO.setIdEta(idEta);
//        adminDTO.setNomeEta(nomeEta);
//
//        erros = Validador.validarAdmin(adminDTO.getNome(), adminDTO.getEmail(), senhaDigitada);
//
//        if (!erros.isEmpty()) {
//            req.setAttribute("erros", erros);
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//            return;
//        }
//
//        senhaCrip = SenhaHash.hashSenha(senhaDigitada);
//        adminDTO.setSenha(senhaCrip);
//
//        resultado = adminDAO.inserirAdmin(adminDTO);
//
//        if (resultado == 1) {
//            resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
//        } else {
//            req.setAttribute("erro", "Não foi possível inserir esse admin. Verifique os campos e tente novamente!");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//        }
//    }
//
//
//    // Método para ALTERAR o admin (com os VALORES NOVOS)
//    protected void alterarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String idParam;
//        String senhaDigitada;
//        AdminDTO adminDTO;
//        int adminId;
//        List<String> erros;
//        String senhaCrip;
//        int resultado;
//        RequestDispatcher rd;
//
//        idParam = req.getParameter("id");
//        senhaDigitada = req.getParameter("senha");
//
//        if (idParam == null || idParam.isEmpty()) {
//            req.setAttribute("erro", "ID do administrador para alteração não fornecido.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//            return;
//        }
//
//        try {
//            adminId = Integer.parseInt(idParam);
//
//            adminDTO = new AdminDTO();
//            adminDTO.setId(adminId);
//            adminDTO.setNome(req.getParameter("nome"));
//            adminDTO.setEmail(req.getParameter("email"));
//
//            erros = Validador.validarAdmin(adminDTO.getNome(), adminDTO.getEmail(), senhaDigitada);
//
//            if (!erros.isEmpty()) {
//                req.setAttribute("erros", erros);
//                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(req, resp);
//                return;
//            }
//
//            senhaCrip = SenhaHash.hashSenha(senhaDigitada);
//            adminDTO.setSenha(senhaCrip);
//
//            resultado = adminDAO.alterarAdmin(adminDTO);
//
//            if (resultado == 1) {
//                resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
//            } else {
//                req.setAttribute("erro", "Não foi possível alterar o administrador. Verifique os campos.");
//                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(req, resp);
//            }
//        } catch (NumberFormatException e) {
//            req.setAttribute("erro", "ID inválido fornecido para alteração.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//        }
//    }
//
//
//    // Método para REMOVER o admin (pelo ID pego)
//    protected void removerAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String idParam;
//        int adminId;
//        AdminDTO adminDTO;
//        int resultado;
//        RequestDispatcher rd;
//
//        idParam = req.getParameter("id");
//
//        if (idParam == null || idParam.isEmpty()) {
//            req.setAttribute("erro", "ID do administrador para remoção não fornecido.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//            return;
//        }
//
//        try {
//            adminId = Integer.parseInt(idParam);
//
//            adminDTO = new AdminDTO();
//            adminDTO.setId(adminId);
//
//            resultado = adminDAO.removerAdmin(adminDTO); // Assinatura original mantida
//
//            if (resultado == 1) {
//                resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
//            } else {
//                req.setAttribute("erro", "Não foi possível remover este Admin. ID pode não existir.");
//                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(req, resp);
//            }
//        } catch (NumberFormatException e) {
//            req.setAttribute("erro", "ID inválido fornecido para remoção.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//        }
//    }
//
//
//    // Método para FILTRAR o admin (por coluna e valor)
//    protected void filtrarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String coluna;
//        String pesquisa;
//        List<AdminDTO> lista;
//        RequestDispatcher rd;
//
//        coluna = req.getParameter("nome_coluna");
//        pesquisa = req.getParameter("pesquisa");
//
//        if (coluna == null || pesquisa == null || coluna.trim().isEmpty() || pesquisa.trim().isEmpty()) {
//            listarAdmins(req, resp);
//            return;
//        }
//
//        lista = adminDAO.filtroBuscaPorColuna(coluna, pesquisa);
//
//        req.setAttribute("admins", lista);
//        rd = req.getRequestDispatcher("/paginasCrud/admin/indexAdmin.jsp");
//        rd.forward(req, resp);
//    }
//}