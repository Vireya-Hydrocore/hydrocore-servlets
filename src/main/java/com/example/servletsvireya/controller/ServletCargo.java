package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.CargoDAO;
import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dto.CargoDTO;
import com.example.servletsvireya.util.Validador;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletCargo", "/mainCargo", "/createCargo", "/selectCargo", "/updateCargo", "/deleteCargo", "/filtroCargo"}, name = "ServletCargo")
public class ServletCargo extends HttpServlet {

    CargoDAO cargoDAO = new CargoDAO();
    List<String> erros = new ArrayList<>();


    // ===============================================================
    //            Método doGet (atributos passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action"); //Vem com a ação do usuário

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar estoques (ou redirecionar)
            listarCargos(req, resp);
            return;
        }

        try{
            switch (action) {
                case "mainCargo":
                    listarCargos(req, resp);
                    break;
                case "selectCargo":
                    buscarCargo(req, resp);
                    break;
                case "filtroCargo":
                    filtrarCargo(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/assets/pages/cargo/cargoIndex.jsp");
            }
        } catch (Exception e) {
            erros.add("Ocorreu um erro ao processar a requisição.");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //            Método doPost (atributos passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar estoques (ou redirecionar)
            listarCargos(req, resp);
            return;
        }

        try {
            switch (action) {
                case "createCargo":
                    inserirCargo(req, resp);
                    break;
                case "updateCargo":
                    alterarCargo(req, resp);
                    break;
                case "deleteCargo":
                    removerCargo(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/ServletCargo?action=mainCargo");
            }
        } catch (Exception e) {
            erros.add("Erro ao processar requisição no servidor.");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ================== MÉTODOS AUXILIARES =========================


    // ===============================================================
    //                Método para LISTAR os cargos
    // ===============================================================

    protected void listarCargos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<CargoDTO> lista = cargoDAO.listarCargos(); //List de objetos retornados na query

        req.setAttribute("cargos", lista); //Devolve a lista de cargos encontrados em um novo atributo

        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/cargo/cargoIndex.jsp"); //Envia para a página principal
        rd.forward(req, resp);
    }


    // ================================================================================
    //     Método para BUSCAR um cargo (mostra os VALORES ANTIGOS na tela de edição)
    // ================================================================================

    protected void buscarCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando o id no CargoDTO
        CargoDTO cargoDTO = new CargoDTO();
        cargoDTO.setId(Integer.parseInt(req.getParameter("id")));

        cargoDAO.buscarPorId(cargoDTO); //No mesmo objeto, setta os valores encontrados

        req.setAttribute("cargo", cargoDTO); //Setta em um novo atributo para o JSP pegar os valores

        //Encaminhar ao documento cargoAlterar.jsp
        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/cargo/cargoAlterar.jsp");
        rd.forward(req, resp);
    }


    // ===============================================================
    //                Método para INSERIR um cargo
    // ===============================================================

    protected void inserirCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Criado um DTO para armazenar os valores inseridos
        CargoDTO cargoDTO = new CargoDTO();
        cargoDTO.setNome(req.getParameter("nome"));
        cargoDTO.setAcesso(Integer.parseInt(req.getParameter("acesso")));

        String nomeEta = req.getParameter("nomeEta");
        int idEta = Integer.parseInt(req.getParameter("nomeEta")); //
        cargoDTO.setIdEta(idEta);
        cargoDTO.setNomeEta(nomeEta);

        // VALIDAÇÃO
        List<String> erros = Validador.validarCargo(cargoDTO.getNome(), cargoDTO.getAcesso());

        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
            return;
        }

        // Chamando o DAO
        int resultado = cargoDAO.inserirCargo(cargoDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletCargo?action=mainCargo"); //Lista novamente os cargos se der certo
        } else {
            erros.add("Não foi possível inserir esse cargo, tente novamente!");
            req.setAttribute("erros", erros); //Setta um atributo com o erro
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp); //Vai para a página de erro
        }
    }


    // ===============================================================
    //       Método para ALTERAR o cargo (com os VALORES NOVOS)
    // ===============================================================

    protected void alterarCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando os valores no cargoDTO
        CargoDTO cargoDTO = new CargoDTO();
        cargoDTO.setId(Integer.parseInt(req.getParameter("id")));
        cargoDTO.setNome(req.getParameter("nome"));
        cargoDTO.setAcesso(Integer.parseInt(req.getParameter("acesso")));

        // ============ VALIDAÇÃO ============
        erros = Validador.validarCargo(cargoDTO.getNome(), cargoDTO.getAcesso());

        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
            return;
        }

        int resultado = cargoDAO.alterarCargo(cargoDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletCargo?action=mainCargo");
        } else {
            erros.add("Não foi possível alterar o cargo! Verifique os campos e tente novamente.");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //         Método para REMOVER o cargo (pelo ID pego)
    // ===============================================================

    protected void removerCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Instanciando objeto DTO e settando o id para remoção
        CargoDTO cargoDTO = new CargoDTO();
        cargoDTO.setId(Integer.parseInt(req.getParameter("id")));

        int resultado = cargoDAO.removerCargo(cargoDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletCargo?action=mainCargo");
        } else {
            erros.add("Não foi possível remover o cargo, tente novamente mais tarde.");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //         Método para FILTRAR o cargo (por coluna e valor)
    // ===============================================================

    protected void filtrarCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Armazena a query filtrada em um novo List
        List<CargoDTO> lista = cargoDAO.filtroBuscaPorColuna(req.getParameter("nome_coluna"), req.getParameter("pesquisa"));

        req.setAttribute("cargos", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/cargo/cargoIndex.jsp");
        rd.forward(req, resp);
    }
}