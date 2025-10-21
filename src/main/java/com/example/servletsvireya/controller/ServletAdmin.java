package com.example.servletsvireya.controller;
import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.util.SenhaHash;
import com.example.servletsvireya.dao.AdminDAO;
import com.example.servletsvireya.dto.AdminDTO;
import com.example.servletsvireya.util.Validador;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletAdmin", "/mainAdmin", "/createAdmin", "/selectAdmin", "/updateAdmin", "/deleteAdmin", "/filtroAdmin"}, name = "/ServletAdmin")
public class ServletAdmin extends HttpServlet {

    private AdminDAO adminDAO = new AdminDAO();


    // ===============================================================
    //            Método doGet (atributos passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action"); //Vem com a ação do usuário

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar admins (ou redirecionar)
            listarAdmins(req, resp);
            return;
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
                    resp.sendRedirect(req.getContextPath() + "/assets/pages/admin/adminIndex.jsp");
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

        String action = req.getParameter("action");

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar admins (ou redirecionar)
            listarAdmins(req, resp);
            return;
        }

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
            default:
                resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
        }
    }


    // ==================== MÉTODOS AUXILIARES =======================


    // ===============================================================
    //                 Método para LISTAR os admins
    // ===============================================================

    protected void listarAdmins(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<AdminDTO> lista = adminDAO.listarAdmin(); //List de objetos retornados na query

        req.setAttribute("admins", lista); //Devolve a lista de ETAs encontradas em um novo atributo, para a pagina JSP

        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/admin/adminIndex.jsp"); //Envia para a página principal
        rd.forward(req, resp);
    }


    // ================================================================================
    //    Método para BUSCAR um admin (mostra os VALORES ANTIGOS na tela de edição)
    // ================================================================================

    protected void buscarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Recebimento do id do admin que será editado
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(Integer.parseInt(req.getParameter("id")));

        //Executando o método buscarPorId
        adminDAO.buscarPorId(adminDTO);

        //Settando os atributos do formulário no adminDTO
        req.setAttribute("admin", adminDTO);

        //Encaminhando ao documento adminAlterar.jsp
        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/admin/adminAlterar.jsp");
        rd.forward(req, resp);
    }


    // ===============================================================
    //                 Método para INSERIR um admin
    // ===============================================================

    protected void inserirAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Criado um DTO para armazenar os valores inseridos
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setNome(req.getParameter("nome"));
        adminDTO.setEmail(req.getParameter("email"));
        String senhaDigitada = req.getParameter("senha");
        String nomeEta = req.getParameter("nomeEta");

        EtaDAO etaDAO= new EtaDAO(); //Para realizar a busca do id da ETA
        adminDTO.setIdEta(etaDAO.buscarIdPorNome(nomeEta));
        adminDTO.setNomeEta(nomeEta);

        // ===== VALIDAÇÃO =====
        List<String> erros = Validador.validarAdmin(adminDTO.getNome(), adminDTO.getEmail(), senhaDigitada);

        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
            return; // Interrompe execução se houver erros
        }

        //Criptografando a senha
        String senhacrip = SenhaHash.hashSenha(senhaDigitada);
        adminDTO.setSenha(senhacrip);

        int resultado = adminDAO.inserirAdmin(adminDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin"); //Lista novamente os admins se der certo
        } else {
            erros.add("ETA Inexistente. Verifique os campos e tente novamente!");
            req.setAttribute("erros", erros); //Setta um atributo com o erro
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp); //Vai para a página de erro
        }
    }


    // ===============================================================
    //      Método para ALTERAR o admin (com os VALORES NOVOS)
    // ===============================================================

    protected void alterarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando os valores no adminDTO ---> ---> --> usuário não pode mudar a eta
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(Integer.parseInt(req.getParameter("id")));
        adminDTO.setNome(req.getParameter("nome"));
        adminDTO.setEmail(req.getParameter("email"));
        String senhaDigitada = req.getParameter("senha"); // pegar senha digitada

        // ===== VALIDAÇÃO =====
        List<String> erros = Validador.validarAdmin(adminDTO.getNome(), adminDTO.getEmail(), senhaDigitada);

        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
            return; // Interrompe execução se houver erros
        }

        // Criptografando a senha antes de salvar
        adminDTO.setSenha(SenhaHash.hashSenha(senhaDigitada));

        // Chamar o método do DAO
        int resultado = adminDAO.alterarAdmin(adminDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
        } else {
            req.setAttribute("erro", "E-mail ou senha inválidos");
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //         Método para REMOVER o admin (pelo ID pego)
    // ===============================================================

    protected void removerAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Instanciando objeto DTO e settando o id para remoção
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(Integer.parseInt(req.getParameter("id")));

        int resultado = adminDAO.removerAdmin(adminDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
        } else {
            // Página de erro
            req.setAttribute("erro", "Não foi possível remover este Admin");
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //        Método para FILTRAR o admin (por coluna e valor)
    // ===============================================================

    protected void filtrarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Armazena a query filtrada em um novo List
        List<AdminDTO> lista = adminDAO.filtroBuscaPorColuna(req.getParameter("nome_coluna"), req.getParameter("pesquisa")); //Armazena numa lista

        req.setAttribute("admins", lista); //Setta a lista em um novo atributo
        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/admin/adminIndex.jsp");
        rd.forward(req, resp);
    }
}
