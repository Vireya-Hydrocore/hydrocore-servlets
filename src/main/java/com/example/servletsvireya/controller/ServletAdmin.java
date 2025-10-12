package com.example.servletsvireya.controller;
import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.util.SenhaHash;
import com.example.servletsvireya.dao.AdminDAO;
import com.example.servletsvireya.dao.EstoqueDAO;
import com.example.servletsvireya.dto.AdminDTO;
import com.example.servletsvireya.dto.EstoqueDTO;
import com.example.servletsvireya.util.Validador;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletAdmin", "/mainAdmin", "/createAdmin", "/selectAdmin", "/updateAdmin", "/deleteAdmin", "/logar", "/filtroAdmin", "/logarAdmin"}, name = "/ServletAdmin")
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
            listarAdmin(req, resp);
            return;
        }

        try {
            switch (action) {
                case "mainAdmin":
                    listarAdmin(req, resp);
                    break;
                case "selectAdmin": //Seleciona por id para alterar o Admin
                    buscarAdmin(req, resp);
                    break;
                case "filtroAdmin": //Seleciona por id para alterar o Admin
                    filtroAdmin(req, resp);
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

        //Não podem passar dados pela URL
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
            case "logar":
                logar(req, resp);
                break;
            case "logarAdmin":
                logarAdmin(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
        }
    }

    // MÉTODOS AUXILIARES

    //LISTA POR ETA
    protected void listarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<AdminDTO> lista = adminDAO.listarAdmin(); //Armazena numa lista

        req.setAttribute("admins", lista); //Setta a lista em um novo atributo
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/admin/indexAdmin.jsp");
        rd.forward(req, resp);
    }


    //INSERIR ADMIN
    protected void inserirAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AdminDTO adminDTO = new AdminDTO();
        EtaDAO etaDAO= new EtaDAO();

        adminDTO.setNome(req.getParameter("nome"));
        adminDTO.setEmail(req.getParameter("email"));
        String nomeEta = req.getParameter("nomeEta");
        adminDTO.setIdEta(etaDAO.buscarIdPorNome(nomeEta));
        adminDTO.setNomeEta(nomeEta);

        String senhacrip = SenhaHash.hashSenha(req.getParameter("senha"));
        adminDTO.setSenha(senhacrip);

        int resultado = adminDAO.inserirAdmin(adminDTO);

        if (resultado == 1) {
            //Se inserir, "atualiza" a página
            resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
        } else {
            // Página de erro
            resp.sendRedirect("/paginasCrud/erro.jsp");
        }
    }


    //BUSCAR PARA REALIZAÇÃO DO UPDATE ------> arrumar metodo buscarPorId
    protected void buscarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Recebimento do id do admin que será editado
        int id = Integer.parseInt(req.getParameter("id"));
        //Setar a variavel admin
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(id);
        //Executar o método buscarPorId
        adminDAO.buscarPorId(adminDTO);
        //Setar os atributos do formulário no adminDTO
        req.setAttribute("id", adminDTO.getId());
        req.setAttribute("nome", adminDTO.getNome());
        req.setAttribute("email", adminDTO.getEmail());
        req.setAttribute("senha", adminDTO.getSenha());
        //Encaminhar ao documento alterarAdmin.jsp
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/admin/alterarAdmin.jsp");
        rd.forward(req, resp);
    }


    //ALTERA O ADMIN
    protected void alterarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Pegando o ID do admin que está sendo alterado
        int id = Integer.parseInt(req.getParameter("id"));

        //Settando o id e os atributos em um objeto DTO
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(id);
        adminDTO.setNome(req.getParameter("nome"));
        adminDTO.setEmail(req.getParameter("email"));
        adminDTO.setSenha(req.getParameter("senha"));

        // Chamar o método do DAO
        int resultado = adminDAO.alterarAdmin(adminDTO);

        if (resultado == 1) {
            //Redireciona para a listagem
            resp.sendRedirect(req.getContextPath() + "/ServletAdmin?action=mainAdmin");
        } else {
            // Página de erro
            req.setAttribute("erro", "E-mail ou senha inválidos");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    //REMOVER ADMIN
    protected void removerAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AdminDTO adminDTO = new AdminDTO();

        adminDTO.setId(Integer.parseInt(req.getParameter("id"))); //Pegando id para localizar e remover do banco

        int resultado = adminDAO.removerAdmin(adminDTO);

        if (resultado == 1) {
            // Atualiza a lista de produtos na mesma página
            listarAdmin(req, resp);
        } else {
            // Página de erro
            req.setAttribute("erro", "Não foi possível remover este Admin");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    //LOGIN DO ADMIN
    protected void logarAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");
        Boolean resultado = adminDAO.seLogarAreaRestrita(email, senha);

        if (resultado) {
            // Redireciona para página principal do admin
            resp.sendRedirect(req.getContextPath() + "/ServletEta?action=mainEta"); //??????????
        } else {
            req.setAttribute("erroLogin", "E-mail ou senha incorretos.");
            RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/menu/index.jsp");
            rd.forward(req, resp);
        }
    }
    protected void filtroAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<AdminDTO> lista = adminDAO.filtroBuscaPorColuna(req.getParameter("nome_coluna"),req.getParameter("pesquisa")); //Armazena numa lista

        req.setAttribute("admins", lista); //Setta a lista em um novo atributo
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/admin/indexAdmin.jsp");
        rd.forward(req, resp);
    }
    protected void logar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");
        Integer idAdmin = adminDAO.seLogar(email, senha);

        if (idAdmin != null) {
            // Redireciona para página principal do admin
            resp.sendRedirect(req.getContextPath() + "/ServletEta?action=mainEta"); //??????????
        } else {
            req.setAttribute("erroLogin", "E-mail ou senha incorretos.");
            RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/menu/index.jsp");
            rd.forward(req, resp);
        }
    }


}
