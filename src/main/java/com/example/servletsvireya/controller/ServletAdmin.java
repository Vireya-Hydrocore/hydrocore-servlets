package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.AdminDAO;
import com.example.servletsvireya.dao.EstoqueDAO;
import com.example.servletsvireya.dto.AdminDTO;
import com.example.servletsvireya.dto.EstoqueDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletAdmin", "/mainAdmin", "/createAdmin", "/selectAdmin", "/updateAdmin", "/deleteAdmin"}, name = "ServletAdmin")
public class ServletAdmin extends HttpServlet {

    private AdminDAO adminDAO = new AdminDAO();

    // GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        System.out.println(action);

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar admins (ou redirecionar)
//            admins(req, resp);
            return;
        }

        try {
            switch (action) {
                case "mainAdmin":
                    admins(req, resp);
                    break;
                case "selectAdmin":
                    buscarAdmin(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/admin/indexAdmin.jsp");
            }
        } catch (Exception e) {
            System.out.println("EXCEÇÃO: ");
            e.printStackTrace();
        }
    }

    // POST
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        //Não podem passar pela URL
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

    // MÉTODOS AUXILIARES

    //LISTA POR ETA
    protected void admins(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ⚠ Por enquanto, fixo para testes
        int idEta = 1;

        // ✅ No futuro, isso aqui pega da sessão:
        // HttpSession session = req.getSession();
        // AdminDTO adminLogado = (AdminDTO) session.getAttribute("adminLogado");
        // int idEta = adminLogado.getIdEta();

        List<AdminDTO> lista = adminDAO.listarAdminPorEta(idEta);

        req.setAttribute("admins", lista); //Setta a lista em um novo atributo
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/admin/indexAdmin.jsp");
        rd.forward(req, resp);
    }

    protected void inserirAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AdminDTO adminDTO = new AdminDTO();

        adminDTO.setNome("nome");
        adminDTO.setEmail(req.getParameter("email"));
        adminDTO.setSenha(req.getParameter("senha")); ///////
        adminDTO.setIdEta(1); //para teste

        int resultado = adminDAO.inserirAdmin(adminDTO);

        if (resultado == 1) {
            //Se inserir, "atualiza" a página
            resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
        } else {
            // Página de erro
            resp.sendRedirect("/paginasCrud/admin/erro.jsp");
        }
    }


    protected void buscarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Recebimento do id do admin que será editado
        int id = Integer.parseInt(req.getParameter("id"));
        //Setar a variavel admin
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(id);
        //executar o metodo buscarPorId
        adminDAO.buscarPorId(adminDTO);
        //Setar os atributos do formulário no adminDTO
        req.setAttribute("id", adminDTO.getId());
        req.setAttribute("nome", adminDTO.getNome());
        req.setAttribute("email", adminDTO.getSenha());
        req.setAttribute("senha", adminDTO.getSenha());
        //Encaminhar ao documento alterarAdmin.jsp
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/admin/alterarAdmin.jsp");
        rd.forward(req, resp);
    }

    protected void alterarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Pegando o ID do admin que está sendo alterado
        int id = Integer.parseInt(req.getParameter("id"));

        //Settando o id e os atributos em um objeto DTO
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(id);
        adminDTO.setEmail(req.getParameter("email"));
        adminDTO.setSenha(req.getParameter("senha"));

        // Chamar o método do DAO
        int resultado = adminDAO.alterarAdmin(adminDTO);

        // Tratar o resultado
        if (resultado == 1) {
//            req.setAttribute("alteradoSucesso", true);
            resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
        } else {
            // Página de erro
//            resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
        }
    }

    protected void removerAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AdminDTO adminDTO = new AdminDTO();

        adminDTO.setId(Integer.parseInt(req.getParameter("id"))); //Pegando id para localizar e remover do banco

        int resultado = adminDAO.removerAdmin(adminDTO);

        if (resultado == 1) {
            // Atualiza a lista de produtos na mesma página
            admins(req, resp);
        } else {
            // Página de erro
//            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }
}