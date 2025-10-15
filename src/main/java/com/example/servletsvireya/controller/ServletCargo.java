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
import java.util.List;

@WebServlet(urlPatterns = {"/ServletCargo", "/mainCargo", "/createCargo", "/selectCargo", "/updateCargo", "/deleteCargo", "/filtroCargo"}, name = "ServletCargo")
public class ServletCargo extends HttpServlet {
    private final CargoDAO cargoDAO = new CargoDAO();


    // ===============================================================
    //            Método doGet (atributos passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action"); //Vem com a ação do usuário

        // Proteção contra NullPointerException em switch de String
        if (action == null) action = "mainCargo";

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
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/cargo/cargoIndex.jsp");
            }
        } catch (Exception e){
            e.printStackTrace(); //Mostra a exceção possível
        }
    }


    // ===============================================================
    //            Método doPost (atributos passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

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
    }


    // ================== MÉTODOS AUXILIARES =========================


    // ===============================================================
    //                Método para LISTAR os cargos
    // ===============================================================

    protected void listarCargos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<CargoDTO> lista = cargoDAO.listarCargos(); //List de objetos retornados na query

        req.setAttribute("cargos", lista); //Devolve a lista de estoques encontrados em um novo atributo

        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/cargo/cargoIndex.jsp"); //Envia para a página principal
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
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/cargo/cargoAlterar.jsp");
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

        EtaDAO etaDAO = new EtaDAO(); //Para realizar a busca do id da eta
        String nomeEta = req.getParameter("nomeEta");
        int idEta = etaDAO.buscarIdPorNome(nomeEta);
        cargoDTO.setIdEta(idEta);
        cargoDTO.setNomeEta(nomeEta);

        // VALIDAÇÃO
        List<String> erros = Validador.validarCargo(cargoDTO.getNome(), cargoDTO.getAcesso());

        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        // Chamando o DAO
        int resultado = cargoDAO.inserirCargo(cargoDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletCargo?action=mainCargo"); //Lista novamente os cargos se der certo
        } else {
            req.setAttribute("erro", "Não foi possível inserir esse cargo, tente novamente!"); //Setta um atributo com o erro
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp); //Vai para a página de erro
        }
    }


    // ===============================================================
    //       Método para ALTERAR o cargo (com os VALORES NOVOS)
    // ===============================================================

    protected void alterarCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando os valores no cargoDTO ---> ---> --> usuário não pode mudar a eta
        CargoDTO cargoDTO = new CargoDTO();
        cargoDTO.setId(Integer.parseInt(req.getParameter("id")));
        cargoDTO.setNome(req.getParameter("nome"));
        cargoDTO.setAcesso(Integer.parseInt(req.getParameter("acesso")));

        // VALIDAÇÃO
        List<String> erros = Validador.validarCargo(cargoDTO.getNome(), cargoDTO.getAcesso());

        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        int resultado = cargoDAO.alterarCargo(cargoDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletCargo?action=mainCargo");
        } else {
            req.setAttribute("erro", "Não foi possível alterar o cargo! Verifique os campos e tente novamente.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
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
            req.setAttribute("erro", "Não foi possível remover o cargo, tente novamente mais tarde.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //         Método para FILTRAR o cargo (por coluna e valor)
    // ===============================================================

    protected void filtrarCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Armazena a query filtrada em um novo List
        List<CargoDTO> lista = cargoDAO.filtroBuscaPorColuna(req.getParameter("nome_coluna"), req.getParameter("pesquisa"));

        req.setAttribute("cargos", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/cargo/cargoIndex.jsp");
        rd.forward(req, resp);
    }
}