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

    // DAOs inicializados como final para garantir uma única instância thread-safe (boa prática)
    private final CargoDAO cargoDAO = new CargoDAO();
    private final EtaDAO etaDAO = new EtaDAO(); // Adicionado aqui para reutilização

    // ===============================================================
    //            Método doGet (atributos passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Se action for null, assume-se o comportamento padrão (listar/mainCargo)
        String action = req.getParameter("action");
        if (action == null) {
            action = "mainCargo";
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
                    // Ação não mapeada: Redireciona para a página principal.
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/cargo/cargoIndex.jsp");
            }
        } catch (Exception e){
            // Boas práticas: Loga a exceção e retorna um erro genérico para o usuário
            System.err.println("Erro inesperado no doGet do ServletCargo: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("erro", "Ocorreu um erro interno ao processar a requisição.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //            Método doPost (atributos passam pelo servidor)
    // ===============================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // Se action for null, o fluxo original não trata, mas é melhor garantir um valor.
        if (action == null) {
            action = ""; // Usar string vazia para cair no default
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
            // Boas práticas: Loga a exceção e retorna um erro genérico para o usuário
            System.err.println("Erro inesperado no doPost do ServletCargo: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("erro", "Ocorreu um erro interno ao processar a requisição.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ================== MÉTODOS AUXILIARES =========================


    // Método para LISTAR os cargos
    protected void listarCargos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<CargoDTO> lista = cargoDAO.listarCargos();

        req.setAttribute("cargos", lista);

        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/cargo/cargoIndex.jsp");
        rd.forward(req, resp);
    }


    // Método para BUSCAR um cargo (mostra os VALORES ANTIGOS na tela de edição)
    protected void buscarCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        // 1. Robustez: Verifica se o parâmetro ID está presente
        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("erro", "ID do cargo para busca não fornecido.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        try {
            // 2. Robustez: Converte o ID para int, tratando NumberFormatException
            int cargoId = Integer.parseInt(idParam);

            // Settando o id no CargoDTO, mantendo a lógica de passagem do DTO
            CargoDTO cargoDTO = new CargoDTO();
            cargoDTO.setId(cargoId);

            cargoDAO.buscarPorId(cargoDTO); // Assinatura original mantida: DTO entra e é preenchido.

            // 3. Clareza: Verifica se o DTO foi preenchido (se a busca foi bem-sucedida)
            if (cargoDTO.getNome() != null) { // Assumindo que o nome será preenchido se o registro for encontrado
                req.setAttribute("cargo", cargoDTO);

                // Encaminhar ao documento cargoAlterar.jsp
                RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/cargo/cargoAlterar.jsp");
                rd.forward(req, resp);
            } else {
                req.setAttribute("erro", "Cargo com ID " + cargoId + " não encontrado.");
                req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            }

        } catch (NumberFormatException e) {
            // Tratamento específico para ID inválido
            req.setAttribute("erro", "ID inválido fornecido. Deve ser um número inteiro.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // Método para INSERIR um cargo
    protected void inserirCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Criado um DTO para armazenar os valores inseridos
        CargoDTO cargoDTO = new CargoDTO();
        cargoDTO.setNome(req.getParameter("nome"));

        String acessoParam = req.getParameter("acesso");
        String nomeEta = req.getParameter("nomeEta");

        // 1. Robustez: Trata NumberFormatException na conversão de acesso
        try {
            if (acessoParam == null || acessoParam.isEmpty()) {
                throw new NumberFormatException("Nível de acesso não fornecido.");
            }
            cargoDTO.setAcesso(Integer.parseInt(acessoParam));
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "O nível de acesso deve ser um número inteiro válido.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        // Para realizar a busca do id da eta (usando a instância da classe)
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
            req.setAttribute("erro", "Não foi possível inserir esse cargo, tente novamente!");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // Método para ALTERAR o cargo (com os VALORES NOVOS)
    protected void alterarCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String acessoParam = req.getParameter("acesso");

        // 1. Robustez: Verifica se o parâmetro ID está presente
        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("erro", "ID do cargo para alteração não fornecido.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        try {
            // 2. Robustez: Converte ID e Acesso, tratando NumberFormatException
            int cargoId = Integer.parseInt(idParam);
            if (acessoParam == null || acessoParam.isEmpty()) {
                throw new NumberFormatException("Nível de acesso não fornecido.");
            }
            int acesso = Integer.parseInt(acessoParam);

            // Settando os valores no cargoDTO, mantendo a lógica de passagem do DTO
            CargoDTO cargoDTO = new CargoDTO();
            cargoDTO.setId(cargoId);
            cargoDTO.setNome(req.getParameter("nome"));
            cargoDTO.setAcesso(acesso);

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
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID ou Nível de Acesso inválido fornecido. Ambos devem ser números inteiros.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // Método para REMOVER o cargo (pelo ID pego)
    protected void removerCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        // 1. Robustez: Verifica se o parâmetro ID está presente
        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("erro", "ID do cargo para remoção não fornecido.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        try {
            // 2. Robustez: Converte o ID para int, tratando NumberFormatException
            int cargoId = Integer.parseInt(idParam);

            // Instanciando objeto DTO e settando o id para remoção
            CargoDTO cargoDTO = new CargoDTO();
            cargoDTO.setId(cargoId);

            int resultado = cargoDAO.removerCargo(cargoDTO); // Assinatura original mantida

            if (resultado == 1) {
                resp.sendRedirect(req.getContextPath() + "/ServletCargo?action=mainCargo");
            } else {
                req.setAttribute("erro", "Não foi possível remover o cargo, ID pode não existir.");
                req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID inválido fornecido para remoção.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // Método para FILTRAR o cargo (por coluna e valor)
    protected void filtrarCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String coluna = req.getParameter("nome_coluna");
        String pesquisa = req.getParameter("pesquisa");

        // Boas Práticas: Se o filtro estiver vazio, lista todos em vez de falhar.
        if (coluna == null || pesquisa == null || coluna.trim().isEmpty() || pesquisa.trim().isEmpty()) {
            listarCargos(req, resp);
            return;
        }

        List<CargoDTO> lista = cargoDAO.filtroBuscaPorColuna(coluna, pesquisa);

        req.setAttribute("cargos", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/cargo/cargoIndex.jsp");
        rd.forward(req, resp);
    }
}
//package com.example.servletsvireya.controller;
//
//        import com.example.servletsvireya.dao.CargoDAO;
//        import com.example.servletsvireya.dao.EtaDAO;
//        import com.example.servletsvireya.dto.CargoDTO;
//        import com.example.servletsvireya.util.Validador;
//        import jakarta.servlet.RequestDispatcher;
//        import jakarta.servlet.ServletException;
//        import jakarta.servlet.annotation.WebServlet;
//        import jakarta.servlet.http.HttpServlet;
//        import jakarta.servlet.http.HttpServletRequest;
//        import jakarta.servlet.http.HttpServletResponse;
//
//        import java.io.IOException;
//        import java.util.List;
//
//@WebServlet(urlPatterns = {"/ServletCargo", "/mainCargo", "/createCargo", "/selectCargo", "/updateCargo", "/deleteCargo", "/filtroCargo"}, name = "ServletCargo")
//public class ServletCargo extends HttpServlet {
//
//    // DAOs inicializados no topo da classe (mantidos como final)
//    private final CargoDAO cargoDAO = new CargoDAO();
//    private final EtaDAO etaDAO = new EtaDAO();
//
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
//            action = "mainCargo";
//        }
//
//        try{
//            switch (action) {
//                case "mainCargo":
//                    listarCargos(req, resp);
//                    break;
//                case "selectCargo":
//                    buscarCargo(req, resp);
//                    break;
//                case "filtroCargo":
//                    filtrarCargo(req, resp);
//                    break;
//                default:
//                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/cargo/cargoIndex.jsp");
//            }
//        } catch (Exception e){
//            System.err.println("Erro inesperado no doGet do ServletCargo: " + e.getMessage());
//            e.printStackTrace();
//            req.setAttribute("erro", "Ocorreu um erro interno ao processar a requisição.");
//            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
//        }
//    }
//
//
//    // ===============================================================
//    //            Método doPost (atributos passam pelo servidor)
//    // ===============================================================
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action;
//
//        action = req.getParameter("action");
//        if (action == null) {
//            action = "";
//        }
//
//        try {
//            switch (action) {
//                case "createCargo":
//                    inserirCargo(req, resp);
//                    break;
//                case "updateCargo":
//                    alterarCargo(req, resp);
//                    break;
//                case "deleteCargo":
//                    removerCargo(req, resp);
//                    break;
//                default:
//                    resp.sendRedirect(req.getContextPath() + "/ServletCargo?action=mainCargo");
//            }
//        } catch (Exception e) {
//            System.err.println("Erro inesperado no doPost do ServletCargo: " + e.getMessage());
//            e.printStackTrace();
//            req.setAttribute("erro", "Ocorreu um erro interno ao processar a requisição.");
//            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
//        }
//    }
//
//
//    // ================== MÉTODOS AUXILIARES =========================
//
//
//    // Método para LISTAR os cargos
//    protected void listarCargos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        List<CargoDTO> lista;
//        RequestDispatcher rd;
//
//        lista = cargoDAO.listarCargos();
//
//        req.setAttribute("cargos", lista);
//
//        rd = req.getRequestDispatcher("/paginasCrud/cargo/cargoIndex.jsp");
//        rd.forward(req, resp);
//    }
//
//
//    // Método para BUSCAR um cargo (mostra os VALORES ANTIGOS na tela de edição)
//    protected void buscarCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String idParam;
//        int cargoId;
//        CargoDTO cargoDTO;
//        RequestDispatcher rd;
//
//        idParam = req.getParameter("id");
//
//        if (idParam == null || idParam.isEmpty()) {
//            req.setAttribute("erro", "ID do cargo para busca não fornecido.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//            return;
//        }
//
//        try {
//            cargoId = Integer.parseInt(idParam);
//
//            cargoDTO = new CargoDTO();
//            cargoDTO.setId(cargoId);
//
//            cargoDAO.buscarPorId(cargoDTO); // Assinatura original mantida: DTO entra e é preenchido.
//
//            if (cargoDTO.getNome() != null && !cargoDTO.getNome().isEmpty()) {
//                req.setAttribute("cargo", cargoDTO);
//                rd = req.getRequestDispatcher("/paginasCrud/cargo/cargoAlterar.jsp");
//                rd.forward(req, resp);
//            } else {
//                req.setAttribute("erro", "Cargo com ID " + cargoId + " não encontrado.");
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
//    // Método para INSERIR um cargo
//    protected void inserirCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        CargoDTO cargoDTO;
//        String nome;
//        String acessoParam;
//        String nomeEta;
//        int idEta;
//        int acesso;
//        List<String> erros;
//        int resultado;
//        RequestDispatcher rd;
//
//        cargoDTO = new CargoDTO();
//        nome = req.getParameter("nome");
//        acessoParam = req.getParameter("acesso");
//        nomeEta = req.getParameter("nomeEta");
//
//        cargoDTO.setNome(nome);
//
//        try {
//            if (acessoParam == null || acessoParam.isEmpty()) {
//                throw new NumberFormatException("Nível de acesso não fornecido.");
//            }
//            acesso = Integer.parseInt(acessoParam);
//            cargoDTO.setAcesso(acesso);
//        } catch (NumberFormatException e) {
//            req.setAttribute("erro", "O nível de acesso deve ser um número inteiro válido.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//            return;
//        }
//
//        idEta = etaDAO.buscarIdPorNome(nomeEta);
//        cargoDTO.setIdEta(idEta);
//        cargoDTO.setNomeEta(nomeEta);
//
//        erros = Validador.validarCargo(cargoDTO.getNome(), cargoDTO.getAcesso());
//
//        if (!erros.isEmpty()) {
//            req.setAttribute("erros", erros);
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//            return;
//        }
//
//        resultado = cargoDAO.inserirCargo(cargoDTO);
//
//        if (resultado == 1) {
//            resp.sendRedirect(req.getContextPath() + "/ServletCargo?action=mainCargo");
//        } else {
//            req.setAttribute("erro", "Não foi possível inserir esse cargo, tente novamente!");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//        }
//    }
//
//
//    // Método para ALTERAR o cargo (com os VALORES NOVOS)
//    protected void alterarCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String idParam;
//        String nome;
//        String acessoParam;
//        CargoDTO cargoDTO;
//        int cargoId;
//        int acesso;
//        List<String> erros;
//        int resultado;
//        RequestDispatcher rd;
//
//        idParam = req.getParameter("id");
//        nome = req.getParameter("nome");
//        acessoParam = req.getParameter("acesso");
//
//        if (idParam == null || idParam.isEmpty()) {
//            req.setAttribute("erro", "ID do cargo para alteração não fornecido.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//            return;
//        }
//
//        try {
//            cargoId = Integer.parseInt(idParam);
//
//            if (acessoParam == null || acessoParam.isEmpty()) {
//                throw new NumberFormatException("Nível de acesso não fornecido.");
//            }
//            acesso = Integer.parseInt(acessoParam);
//
//            cargoDTO = new CargoDTO();
//            cargoDTO.setId(cargoId);
//            cargoDTO.setNome(nome);
//            cargoDTO.setAcesso(acesso);
//
//            erros = Validador.validarCargo(cargoDTO.getNome(), cargoDTO.getAcesso());
//
//            if (!erros.isEmpty()) {
//                req.setAttribute("erros", erros);
//                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(req, resp);
//                return;
//            }
//
//            resultado = cargoDAO.alterarCargo(cargoDTO);
//
//            if (resultado == 1) {
//                resp.sendRedirect(req.getContextPath() + "/ServletCargo?action=mainCargo");
//            } else {
//                req.setAttribute("erro", "Não foi possível alterar o cargo! Verifique os campos e tente novamente.");
//                rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//                rd.forward(req, resp);
//            }
//        } catch (NumberFormatException e) {
//            req.setAttribute("erro", "ID ou Nível de Acesso inválido fornecido. Ambos devem ser números inteiros.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//        }
//    }
//
//
//    // Método para REMOVER o cargo (pelo ID pego)
//    protected void removerCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String idParam;
//        int cargoId;
//        CargoDTO cargoDTO;
//        int resultado;
//        RequestDispatcher rd;
//
//        idParam = req.getParameter("id");
//
//        if (idParam == null || idParam.isEmpty()) {
//            req.setAttribute("erro", "ID do cargo para remoção não fornecido.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//            return;
//        }
//
//        try {
//            cargoId = Integer.parseInt(idParam);
//
//            cargoDTO = new CargoDTO();
//            cargoDTO.setId(cargoId);
//
//            resultado = cargoDAO.removerCargo(cargoDTO);
//
//            if (resultado == 1) {
//                resp.sendRedirect(req.getContextPath() + "/ServletCargo?action=mainCargo");
//            } else {
//                req.setAttribute("erro", "Não foi possível remover o cargo, ID pode não existir.");
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
//    // Método para FILTRAR o cargo (por coluna e valor)
//    protected void filtrarCargo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String coluna;
//        String pesquisa;
//        List<CargoDTO> lista;
//        RequestDispatcher rd;
//
//        coluna = req.getParameter("nome_coluna");
//        pesquisa = req.getParameter("pesquisa");
//
//        if (coluna == null || pesquisa == null || coluna.trim().isEmpty() || pesquisa.trim().isEmpty()) {
//            listarCargos(req, resp);
//            return;
//        }
//
//        lista = cargoDAO.filtroBuscaPorColuna(coluna, pesquisa);
//
//        req.setAttribute("cargos", lista);
//        rd = req.getRequestDispatcher("/paginasCrud/cargo/cargoIndex.jsp");
//        rd.forward(req, resp);
//    }
//}