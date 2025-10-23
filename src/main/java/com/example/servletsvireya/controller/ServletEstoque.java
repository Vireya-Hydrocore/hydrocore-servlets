package com.example.servletsvireya.controller;

import com.example.servletsvireya.dao.EstoqueDAO;
import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dao.ProdutoDAO;
import com.example.servletsvireya.dto.EstoqueDTO;
import com.example.servletsvireya.dto.ProdutoDTO;
import com.example.servletsvireya.model.Estoque;
import com.example.servletsvireya.model.Produto;
import com.example.servletsvireya.util.Validador;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletEstoque", "/mainEstoque", "/createEstoque", "/selectEstoque", "/updateEstoque", "/deleteEstoque", "/filtroEstoque"}, name = "ServletEstoque")
public class ServletEstoque extends HttpServlet {

    private EstoqueDAO estoqueDAO = new EstoqueDAO();
    List<String> erros = new ArrayList<>();


    // ===============================================================
    //             Método doGet (atributos passam pela URL)
    // ===============================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action"); //Vem com a ação do usuário

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar estoques (ou redirecionar)
            listarEstoque(req, resp);
            return;
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
                    resp.sendRedirect(req.getContextPath() + "/assets/pages/estoque/estoqueIndex.jsp");
            }
        } catch (Exception e) {
            erros.add("Ocorreu um erro ao processar sua requisição de estoque.");
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
            listarEstoque(req, resp);
            return;
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
            erros.add("Erro inesperado ao processar a ação de estoque.");
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ================== MÉTODOS AUXILIARES =========================


    // ===============================================================
    //                Método para LISTAR o estoque
    // ===============================================================

    protected void listarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<EstoqueDTO> lista = estoqueDAO.listarEstoque(); //List de objetos retornados na query

        req.setAttribute("estoques", lista); //Devolve a lista de estoques encontrados em um novo atributo

        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/estoque/estoqueIndex.jsp"); //Envia para a página principal
        rd.forward(req, resp);
    }


    // ================================================================================
    //   Método para BUSCAR um estoque (mostra os VALORES ANTIGOS na tela de edição)
    // ================================================================================

    protected void buscarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando o id no EstoqueDTO
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setId(Integer.parseInt(req.getParameter("id")));

        estoqueDAO.buscarPorId(estoqueDTO); //No mesmo objeto, setta os valores encontrados

        req.setAttribute("estoque", estoqueDTO); //Setta em um novo atributo para o JSP pegar os valores

        //Encaminhar ao documento estoqueAlterar.jsp
        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/estoque/estoqueAlterar.jsp");
        rd.forward(req, resp);
    }


    // ===============================================================
    //               Método para INSERIR um estoque
    // ===============================================================

    protected void inserirEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Criado um DTO para armazenar os valores inseridos
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setQuantidade(Integer.parseInt(req.getParameter("quantidade")));
        String dataStr = req.getParameter("dataValidade"); //Convertendo de String para Date
        estoqueDTO.setDataValidade(java.sql.Date.valueOf(dataStr));
        estoqueDTO.setMinPossivelEstocado(Integer.parseInt(req.getParameter("minPossivelEstocado")));

        ProdutoDAO produtoDAO = new ProdutoDAO(); //Para realizar a busca do nome do produto
        String nomeProduto = req.getParameter("nomeProduto");
        int idProduto = produtoDAO.buscarIdPorNome(nomeProduto);
        estoqueDTO.setIdProduto(idProduto);
        estoqueDTO.setNomeProduto(nomeProduto);

        EtaDAO etaDAO = new EtaDAO(); //Realizar a busca do id da ETA
        String nomeEta= req.getParameter("nomeEta");
        estoqueDTO.setIdEta(etaDAO.buscarIdPorNome(nomeEta));
        estoqueDTO.setNomeEta(nomeEta);

        //===== Validação de dados usando a classe Validador ======
        erros = new ArrayList<>();

        if (!Validador.validarLength(req.getParameter("quantidade"), 5)) {
            erros.add("A quantidade deve ser maior que zero e no máximo 5 dígitos.");
        }

        if (!Validador.validarDataValidade((Date) estoqueDTO.getDataValidade())) {
            erros.add("A data de validade tem que ser hoje ou futura (até 100 anos à frente).");
        }

        if (estoqueDTO.getMinPossivelEstocado() < 0) {
            erros.add("O valor mínimo estocado não pode ser negativo.");
        }

        //Se houver erros, retorna pra página de erro
        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
            return;
        }

        //Chamando o DAO de estoque
        int resultado = estoqueDAO.inserirEstoque(estoqueDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque"); //Lista novamente os produtos no estoque se der certo
        } else {
            erros.add("Produto ou ETA inexistente, verifique os campos e tente novamente!");
            req.setAttribute("erros", erros); //Setta um atributo com o erro generalizado
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp); //Vai para a página de erro
        }
    }


    // ===============================================================
    //     Método para ALTERAR o estoque (com os VALORES NOVOS)
    // ===============================================================

    protected void alterarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando os valores no estoqueDTO ---> ---> --> usuário não pode mudar a eta
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setId(Integer.parseInt(req.getParameter("id")));
        estoqueDTO.setQuantidade(Integer.parseInt(req.getParameter("quantidade")));
        String dataStr = req.getParameter("dataValidade");
        try {
            java.sql.Date dataValidade;
            if (dataStr.contains("/")) { // caso venha no formato dd/MM/yyyy
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date utilDate = sdf.parse(dataStr);
                dataValidade = new java.sql.Date(utilDate.getTime());
            } else { // formato YYYY-MM-DD
                dataValidade = java.sql.Date.valueOf(dataStr);
            }
            estoqueDTO.setDataValidade(dataValidade);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        estoqueDTO.setDataValidade(java.sql.Date.valueOf(dataStr));
        estoqueDTO.setMinPossivelEstocado(Integer.parseInt(req.getParameter("minPossivelEstocado")));

        //===== Validação de dados usando a classe Validador ======
        List<String> erros = new ArrayList<>();

        if (estoqueDTO.getQuantidade() <= 0) {
            erros.add("A quantidade deve ser maior que zero.");
        }

        if (!Validador.validarDataValidade((Date) estoqueDTO.getDataValidade())) {
            erros.add("A data de validade tem que ser hoje ou futura (até 100 anos à frente).");
        }

        if (estoqueDTO.getMinPossivelEstocado() < 0) {
            erros.add("O valor mínimo estocado não pode ser negativo.");
        }

        //Se houver erros, retorna pra página de erro
        if (!erros.isEmpty()) {
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
            return;
        }

        //Chamar o DAO com o id já settado no DTO
        int resultado = estoqueDAO.alterarEstoque(estoqueDTO); //Dentro do método, é settado os atributos

        //Tratando o resultado
        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
        } else {
            erros.add("Produto ou ETA inexistente, verifique os campos e tente novamente!"); //Erro generalizado
            req.setAttribute("erros", erros);
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp); //Envia para a página de erro
        }
    }


    // ===============================================================
    //         Método para REMOVER o estoque (pelo ID pego)
    // ===============================================================

    protected void removerEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Instanciando objeto DTO e settando o id para remoção
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setId(Integer.parseInt(req.getParameter("id")));

        int resultado = estoqueDAO.removerEstoque(estoqueDTO);

        if (resultado == 1) {
            resp.sendRedirect(req.getContextPath() + "/ServletEstoque?action=mainEstoque");
        } else {
            req.setAttribute("erros", "Não foi possível remover o estoque.");
            req.getRequestDispatcher("/assets/pages/erro.jsp").forward(req, resp);
        }
    }


    // ===============================================================
    //        Método para FILTRAR o estoque (por coluna e valor)
    // ===============================================================

    protected void filtrarEstoque(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Armazena a query filtrada em um novo List
        List<EstoqueDTO> lista = estoqueDAO.filtroBuscaPorColuna(req.getParameter("nome_coluna"), req.getParameter("pesquisa"));

        req.setAttribute("estoques", lista);
        RequestDispatcher rd = req.getRequestDispatcher("/assets/pages/estoque/estoqueIndex.jsp");
        rd.forward(req, resp);
    }
}