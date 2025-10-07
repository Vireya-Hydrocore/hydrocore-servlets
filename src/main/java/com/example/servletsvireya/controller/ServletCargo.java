package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.CargoDAO;
import com.example.servletsvireya.dto.CargoDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletCargo", "/mainCargo", "/selectCargo", "/updateCargo", "/deleteCargo", "/editarCargo"}, name = "ServletCargo")
public class ServletCargo extends HttpServlet {
    private final CargoDAO cargoDAO = new CargoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "mainCargo";

        switch (action) {
            case "mainCargo":
                listarCargoPorEta(req, resp);
                break;
            case "editarCargo":
                abrirTelaEdicao(req, resp);
                break;
            case "deleteCargo":
                removerCargo(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/paginasCrud/cargo/cargoIndex.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "mainCargo";

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

    // 🔹 Lista apenas cargos da ETA 1 (fixo por enquanto)
    protected void listarCargoPorEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idEta = 1; // temporário (poderá vir da sessão depois)
        List<CargoDTO> lista = cargoDAO.listarCargoPorEta(idEta);
        req.setAttribute("cargo", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/cargo/cargoIndex.jsp");
        rd.forward(req, resp);
    }

    protected void abrirTelaEdicao(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        CargoDTO cargo = cargoDAO.buscarCargo(id);
        req.setAttribute("CargoSelecionado", cargo);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/cargo/cargoAlterar.jsp");
        rd.forward(req, resp);
    }

    // 🔹 Agora insere também o id_eta
    protected void inserirCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CargoDTO cargo = new CargoDTO();
        cargo.setNome(req.getParameter("nome"));
        cargo.setAcesso(Integer.parseInt(req.getParameter("acesso")));

        // Adicionando o ID da ETA (fixo por enquanto)
        cargo.setIdEta(1);

        int resultado = cargoDAO.inserirCargo(cargo);
        if (resultado > 0) {
            resp.sendRedirect(req.getContextPath() + "/ServletCargo?action=mainCargo");
        } else {
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }

    protected void alterarCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        CargoDTO cargoNovo = new CargoDTO();
        cargoNovo.setId(id);
        cargoNovo.setNome(req.getParameter("nome"));
        cargoNovo.setAcesso(Integer.parseInt(req.getParameter("tipo")));

        // Mantendo o vínculo com a ETA 1 (ou poderia vir da sessão)
        cargoNovo.setIdEta(1);

        int resultado = cargoDAO.alterarCargo(cargoNovo);
        if (resultado == 1) {
            HttpSession session = req.getSession();
            session.setAttribute("alteradoSucesso", true);
        }
        resp.sendRedirect(req.getContextPath() + "/ServletCargo?action=mainCargo");
    }

    protected void removerCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        CargoDTO cargoDTO = new CargoDTO();
        cargoDTO.setId(id);
        int resultado = cargoDAO.removerCargo(cargoDTO);
        if (resultado > 0) {
            resp.sendRedirect(req.getContextPath() + "/ServletCargo?action=mainCargo");
        } else {
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }
}
