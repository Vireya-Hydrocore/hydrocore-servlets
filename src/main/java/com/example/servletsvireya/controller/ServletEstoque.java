package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.EstoqueDAO;
import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dao.ProdutoDAO;
import com.example.servletsvireya.dto.EstoqueDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.Date; // Importação específica para java.sql.Date
import java.util.List;

@WebServlet(urlPatterns = {"/ServletEstoque", "/mainEstoque", "/createEstoque", "/selectEstoque", "/updateEstoque", "/deleteEstoque", "/filtroEstoque"}, name = "ServletEstoque")
public class ServletEstoque extends HttpServlet {

    // DAOs inicializados como final da classe para reutilização (boa prática de performance)
    private final EstoqueDAO estoqueDAO = new EstoqueDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final EtaDAO etaDAO = new EtaDAO();


    // ===============================================================
    //             Método doGet (atributos passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Se action for null, assume-se o comportamento padrão (listar/mainEstoque)
        String action = req.getParameter("action");
        if (action == null) {
            action = "mainEstoque";
        }

        try {
            switch (action) {
                case "mainEstoque":
                    listarEstoque(req, resp);
                    break;
                case "selectEstoque":
                    buscarEstoque(req, resp);
                    break;
                case "filtroEstoque":
                    filtrarEstoque(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/estoque/estoqueIndex.jsp");
            }
        } catch (Exception e) {
            // Loga a exceção e retorna um erro genérico
            System.err.println("Erro inesperado no doGet do ServletEstoque: " + e.getMessage());
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

        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "createEstoque":
                    inserirEstoque(req, resp);
                    break;
                case "updateEstoque":
                    alterarEstoque(req, resp);
                    break;
                case "deleteEstoque":
                    removerEstoque(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
            }
        } catch (Exception e) {
            // Loga a exceção e retorna um erro genérico
            System.err.println("Erro inesperado no doPost do ServletEstoque: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("erro", "Ocorreu um erro interno ao processar a requisição.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ================== MÉTODOS AUXILIARES =========================


    // Método para LISTAR o estoque
    protected void listarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<EstoqueDTO> lista = estoqueDAO.listarEstoque();

        req.setAttribute("estoques", lista);

        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/estoque/estoqueIndex.jsp");
        rd.forward(req, resp);
    }


    // Método para BUSCAR um estoque (mostra os VALORES ANTIGOS na tela de edição)
    protected void buscarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        // 1. Robustez: Verifica se o parâmetro ID está presente
        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("erro", "ID do estoque não fornecido.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        try {
            // 2. Robustez: Trata NumberFormatException
            int estoqueId = Integer.parseInt(idParam);

            // Settando o id no EstoqueDTO, mantendo a lógica de passagem do DTO
            EstoqueDTO estoqueDTO = new EstoqueDTO();
            estoqueDTO.setId(estoqueId);

            estoqueDAO.buscarPorId(estoqueDTO); // Assinatura original mantida: DTO entra e é preenchido.

            // 3. Clareza: Verifica se a busca foi bem-sucedida (assumindo que quantidade é preenchida)
            if (estoqueDTO.getQuantidade() > -1) { // Usando -1 como uma suposição de que o DTO não inicializado teria valores zero ou default. Adapte se a verificação for outra.
                req.setAttribute("estoque", estoqueDTO);
                RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/estoque/alterarEstoque.jsp");
                rd.forward(req, resp);
            } else {
                req.setAttribute("erro", "Estoque com ID " + estoqueId + " não encontrado.");
                req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            }


        } catch (NumberFormatException e) {
            // Tratamento específico para ID inválido
            req.setAttribute("erro", "ID inválido fornecido. Deve ser um número inteiro.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // Método para INSERIR um estoque
    protected void inserirEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EstoqueDTO estoqueDTO = new EstoqueDTO();

        String quantidadeParam = req.getParameter("quantidade");
        String minPossivelParam = req.getParameter("minPossivelEstocado");
        String dataStr = req.getParameter("dataValidade");

        // 1. Robustez: Tratamento de NumberFormatException para campos numéricos
        try {
            estoqueDTO.setQuantidade(Integer.parseInt(quantidadeParam));
            estoqueDTO.setMinPossivelEstocado(Integer.parseInt(minPossivelParam));
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "Quantidade e/ou Mínimo Estocado devem ser números inteiros válidos.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        // 2. Robustez: Tratamento de IllegalArgumentException para conversão de Data
        try {
            estoqueDTO.setDataValidade(Date.valueOf(dataStr));
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", "Formato de data inválido. Use YYYY-MM-DD.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        // 3. Performance/Clareza: Uso das instâncias de DAO da classe
        String nomeProduto = req.getParameter("nomeProduto");
        int idProduto = produtoDAO.buscarIdPorNome(nomeProduto);
        estoqueDTO.setIdProduto(idProduto);
        estoqueDTO.setNomeProduto(nomeProduto);

        String nomeEta = req.getParameter("nomeEta");
        estoqueDTO.setIdEta(etaDAO.buscarIdPorNome(nomeEta));
        estoqueDTO.setNomeEta(nomeEta);

        int resultado = estoqueDAO.inserirEstoque(estoqueDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
        } else {
            req.setAttribute("erro", "Não foi possível inserir esse estoque, Verifique os campos e tente novamente!");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // Método para ALTERAR o estoque (com os VALORES NOVOS)
    protected void alterarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EstoqueDTO estoqueDTO = new EstoqueDTO();

        String idParam = req.getParameter("id");
        String quantidadeParam = req.getParameter("quantidade");
        String minPossivelParam = req.getParameter("minPossivelEstocado");
        String dataStr = req.getParameter("dataValidade");

        // 1. Robustez: Tratamento de NumberFormatException para campos numéricos (ID e valores)
        try {
            if (idParam == null || idParam.isEmpty()) {
                throw new NumberFormatException("ID não fornecido.");
            }
            estoqueDTO.setId(Integer.parseInt(idParam));
            estoqueDTO.setQuantidade(Integer.parseInt(quantidadeParam));
            estoqueDTO.setMinPossivelEstocado(Integer.parseInt(minPossivelParam));
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID, Quantidade e/ou Mínimo Estocado devem ser números inteiros válidos.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        // 2. Robustez: Tratamento de IllegalArgumentException para conversão de Data
        try {
            estoqueDTO.setDataValidade(Date.valueOf(dataStr));
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", "Formato de data inválido. Use YYYY-MM-DD.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        // Chamar o DAO (mantendo a lógica de passagem do DTO)
        int resultado = estoqueDAO.alterarEstoque(estoqueDTO);

        // Tratando o resultado
        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
        } else {
            req.setAttribute("erro", "Não foi possível alterar o estoque! Verifique os campos e tente novamente.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // Método para REMOVER o estoque (pelo ID pego)
    protected void removerEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        // 1. Robustez: Verifica se o parâmetro ID está presente
        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("erro", "ID do estoque para remoção não fornecido.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            return;
        }

        try {
            // 2. Robustez: Converte o ID, tratando NumberFormatException
            int estoqueId = Integer.parseInt(idParam);

            // Instanciando objeto DTO e settando o id para remoção
            EstoqueDTO estoqueDTO = new EstoqueDTO();
            estoqueDTO.setId(estoqueId);

            int resultado = estoqueDAO.removerEstoque(estoqueDTO); // Assinatura original mantida

            if (resultado == 1) {
                resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
            } else {
                req.setAttribute("erro", "Não foi possível remover o estoque, tente novamente mais tarde. ID pode não existir.");
                req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID inválido fornecido para remoção.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // Método para FILTRAR o estoque (por coluna e valor)
    protected void filtrarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String coluna = req.getParameter("nome_coluna");
        String pesquisa = req.getParameter("pesquisa");

        // Boas Práticas: Se o filtro estiver vazio, lista todos em vez de falhar.
        if (coluna == null || pesquisa == null || coluna.trim().isEmpty() || pesquisa.trim().isEmpty()) {
            listarEstoque(req, resp);
            return;
        }

        List<EstoqueDTO> lista = estoqueDAO.filtroBuscaPorColuna(coluna, pesquisa);

        req.setAttribute("estoques", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/estoque/estoqueIndex.jsp");
        rd.forward(req, resp);
    }
}
//package com.example.servletsvireya.controller;
//
//        import com.example.servletsvireya.dao.EstoqueDAO;
//        import com.example.servletsvireya.dao.EtaDAO;
//        import com.example.servletsvireya.dao.ProdutoDAO;
//        import com.example.servletsvireya.dto.EstoqueDTO;
//        import jakarta.servlet.RequestDispatcher;
//        import jakarta.servlet.ServletException;
//        import jakarta.servlet.http.HttpServlet;
//        import jakarta.servlet.http.HttpServletRequest;
//        import jakarta.servlet.http.HttpServletResponse;
//        import jakarta.servlet.annotation.WebServlet;
//
//        import java.io.IOException;
//        import java.sql.Date;
//        import java.util.List;
//
//@WebServlet(urlPatterns = {"/ServletEstoque", "/mainEstoque", "/createEstoque", "/selectEstoque", "/updateEstoque", "/deleteEstoque", "/filtroEstoque"}, name = "ServletEstoque")
//public class ServletEstoque extends HttpServlet {
//
//    // DAOs inicializados no topo da classe (mantidos como final)
//    private final EstoqueDAO estoqueDAO = new EstoqueDAO();
//    private final ProdutoDAO produtoDAO = new ProdutoDAO();
//    private final EtaDAO etaDAO = new EtaDAO();
//
//
//    // ===============================================================
//    //             Método doGet (atributos passam pela URL)
//    // ===============================================================
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action;
//
//        action = req.getParameter("action");
//        if (action == null) {
//            action = "mainEstoque";
//        }
//
//        try {
//            switch (action) {
//                case "mainEstoque":
//                    listarEstoque(req, resp);
//                    break;
//                case "selectEstoque":
//                    buscarEstoque(req, resp);
//                    break;
//                case "filtroEstoque":
//                    filtrarEstoque(req, resp);
//                    break;
//                default:
//                    resp.sendRedirect(req.getContextPath() + "/paginasCrud/estoque/estoqueIndex.jsp");
//            }
//        } catch (Exception e) {
//            System.err.println("Erro inesperado no doGet do ServletEstoque: " + e.getMessage());
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
//                case "createEstoque":
//                    inserirEstoque(req, resp);
//                    break;
//                case "updateEstoque":
//                    alterarEstoque(req, resp);
//                    break;
//                case "deleteEstoque":
//                    removerEstoque(req, resp);
//                    break;
//                default:
//                    resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
//            }
//        } catch (Exception e) {
//            System.err.println("Erro inesperado no doPost do ServletEstoque: " + e.getMessage());
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
//    // Método para LISTAR o estoque
//    protected void listarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        List<EstoqueDTO> lista;
//        RequestDispatcher rd;
//
//        lista = estoqueDAO.listarEstoque();
//
//        req.setAttribute("estoques", lista);
//
//        rd = req.getRequestDispatcher("/paginasCrud/estoque/estoqueIndex.jsp");
//        rd.forward(req, resp);
//    }
//
//
//    // Método para BUSCAR um estoque (mostra os VALORES ANTIGOS na tela de edição)
//    protected void buscarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String idParam;
//        int estoqueId;
//        EstoqueDTO estoqueDTO;
//        RequestDispatcher rd;
//
//        idParam = req.getParameter("id");
//
//        if (idParam == null || idParam.isEmpty()) {
//            req.setAttribute("erro", "ID do estoque não fornecido.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//            return;
//        }
//
//        try {
//            estoqueId = Integer.parseInt(idParam);
//
//            estoqueDTO = new EstoqueDTO();
//            estoqueDTO.setId(estoqueId);
//
//            estoqueDAO.buscarPorId(estoqueDTO); // Assinatura original mantida: DTO entra e é preenchido.
//
//            if (estoqueDTO.getQuantidade() > -1) {
//                req.setAttribute("estoque", estoqueDTO);
//                rd = req.getRequestDispatcher("/paginasCrud/estoque/alterarEstoque.jsp");
//                rd.forward(req, resp);
//            } else {
//                req.setAttribute("erro", "Estoque com ID " + estoqueId + " não encontrado.");
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
//    // Método para INSERIR um estoque
//    protected void inserirEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        EstoqueDTO estoqueDTO;
//        String quantidadeParam;
//        String minPossivelParam;
//        String dataStr;
//        int quantidade;
//        int minPossivelEstocado;
//        String nomeProduto;
//        int idProduto;
//        String nomeEta;
//        int idEta;
//        int resultado;
//        RequestDispatcher rd;
//
//        estoqueDTO = new EstoqueDTO();
//        quantidadeParam = req.getParameter("quantidade");
//        minPossivelParam = req.getParameter("minPossivelEstocado");
//        dataStr = req.getParameter("dataValidade");
//
//        try {
//            quantidade = Integer.parseInt(quantidadeParam);
//            minPossivelEstocado = Integer.parseInt(minPossivelParam);
//            estoqueDTO.setQuantidade(quantidade);
//            estoqueDTO.setMinPossivelEstocado(minPossivelEstocado);
//        } catch (NumberFormatException e) {
//            req.setAttribute("erro", "Quantidade e/ou Mínimo Estocado devem ser números inteiros válidos.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//            return;
//        }
//
//        try {
//            estoqueDTO.setDataValidade(Date.valueOf(dataStr));
//        } catch (IllegalArgumentException e) {
//            req.setAttribute("erro", "Formato de data inválido. Use YYYY-MM-DD.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//            return;
//        }
//
//        nomeProduto = req.getParameter("nomeProduto");
//        idProduto = produtoDAO.buscarIdPorNome(nomeProduto);
//        estoqueDTO.setIdProduto(idProduto);
//        estoqueDTO.setNomeProduto(nomeProduto);
//
//        nomeEta = req.getParameter("nomeEta");
//        idEta = etaDAO.buscarIdPorNome(nomeEta);
//        estoqueDTO.setIdEta(idEta);
//        estoqueDTO.setNomeEta(nomeEta);
//
//        resultado = estoqueDAO.inserirEstoque(estoqueDTO);
//
//        if (resultado == 1) {
//            resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
//        } else {
//            req.setAttribute("erro", "Não foi possível inserir esse estoque, Verifique os campos e tente novamente!");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//        }
//    }
//
//
//    // Método para ALTERAR o estoque (com os VALORES NOVOS)
//    protected void alterarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        EstoqueDTO estoqueDTO;
//        String idParam;
//        String quantidadeParam;
//        String minPossivelParam;
//        String dataStr;
//        int estoqueId;
//        int quantidade;
//        int minPossivelEstocado;
//        int resultado;
//        RequestDispatcher rd;
//
//        estoqueDTO = new EstoqueDTO();
//        idParam = req.getParameter("id");
//        quantidadeParam = req.getParameter("quantidade");
//        minPossivelParam = req.getParameter("minPossivelEstocado");
//        dataStr = req.getParameter("dataValidade");
//
//        try {
//            if (idParam == null || idParam.isEmpty()) {
//                throw new NumberFormatException("ID não fornecido.");
//            }
//            estoqueId = Integer.parseInt(idParam);
//            estoqueDTO.setId(estoqueId);
//
//            quantidade = Integer.parseInt(quantidadeParam);
//            minPossivelEstocado = Integer.parseInt(minPossivelParam);
//            estoqueDTO.setQuantidade(quantidade);
//            estoqueDTO.setMinPossivelEstocado(minPossivelEstocado);
//        } catch (NumberFormatException e) {
//            req.setAttribute("erro", "ID, Quantidade e/ou Mínimo Estocado devem ser números inteiros válidos.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//            return;
//        }
//
//        try {
//            estoqueDTO.setDataValidade(Date.valueOf(dataStr));
//        } catch (IllegalArgumentException e) {
//            req.setAttribute("erro", "Formato de data inválido. Use YYYY-MM-DD.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//            return;
//        }
//
//        resultado = estoqueDAO.alterarEstoque(estoqueDTO);
//
//        if (resultado == 1) {
//            resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
//        } else {
//            req.setAttribute("erro", "Não foi possível alterar o estoque! Verifique os campos e tente novamente.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//        }
//    }
//
//
//    // Método para REMOVER o estoque (pelo ID pego)
//    protected void removerEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String idParam;
//        int estoqueId;
//        EstoqueDTO estoqueDTO;
//        int resultado;
//        RequestDispatcher rd;
//
//        idParam = req.getParameter("id");
//
//        if (idParam == null || idParam.isEmpty()) {
//            req.setAttribute("erro", "ID do estoque para remoção não fornecido.");
//            rd = req.getRequestDispatcher("/paginasCrud/erro.jsp");
//            rd.forward(req, resp);
//            return;
//        }
//
//        try {
//            estoqueId = Integer.parseInt(idParam);
//
//            estoqueDTO = new EstoqueDTO();
//            estoqueDTO.setId(estoqueId);
//
//            resultado = estoqueDAO.removerEstoque(estoqueDTO);
//
//            if (resultado == 1) {
//                resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
//            } else {
//                req.setAttribute("erro", "Não foi possível remover o estoque, tente novamente mais tarde. ID pode não existir.");
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
//    // Método para FILTRAR o estoque (por coluna e valor)
//    protected void filtrarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String coluna;
//        String pesquisa;
//        List<EstoqueDTO> lista;
//        RequestDispatcher rd;
//
//        coluna = req.getParameter("nome_coluna");
//        pesquisa = req.getParameter("pesquisa");
//
//        if (coluna == null || pesquisa == null || coluna.trim().isEmpty() || pesquisa.trim().isEmpty()) {
//            listarEstoque(req, resp);
//            return;
//        }
//
//        lista = estoqueDAO.filtroBuscaPorColuna(coluna, pesquisa);
//
//        req.setAttribute("estoques", lista);
//        rd = req.getRequestDispatcher("/paginasCrud/estoque/estoqueIndex.jsp");
//        rd.forward(req, resp);
//    }
//}