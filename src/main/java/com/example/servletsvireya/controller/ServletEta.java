package com.example.servletsvireya.controller;

import com.example.servletsvireya.dto.ProdutoDTO;
import com.example.servletsvireya.model.Eta;
import com.example.servletsvireya.util.SenhaHash;


import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dto.AdminDTO;
import com.example.servletsvireya.dto.EtaDTO;
import com.example.servletsvireya.util.Validador;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/ServletEta", "/mainEta", "/filtroEta", "/createEta"}, name = "ServletEta")
public class ServletEta extends HttpServlet {

    private EtaDAO etaDAO = new EtaDAO();

    // GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        System.out.println(action);

        // Proteção contra NullPointerException em switch de String
        if (action == null) {
            // comportamento padrão: listar etas (ou redirecionar)
            listar(req, resp);
            return;
        }

        switch (action){
            case "mainEta":
                listar(req, resp);
                break;
            case "selectEta":
                buscarEta(req, resp);
                break;
            case "filtroEta":
                filtrarEta(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/paginasCrud/eta/etaIndex.jsp");
        }
    }

    // POST
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        switch (action){
            case "createEta":
                cadastrarEta(req, resp);
                break;
            case "updateEta":
                alterarEta(req, resp);
        }
    }

    // ======================
    // MÉTODO DE CADASTRO (mantido intacto)
    // ======================
    private void cadastrarEta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //no cadastro é criado um dto de endereco, eta e admin. Já que quando eu crio uma eta eu coloco também dados no endereco e crio um admin primario para começar os processos
        //que nada mais sera do que um admin com o nome e email da eta e não uma pessoa em si.

        // Cria DTO da ETA
        HttpSession session = request.getSession();
        String nome = (String) session.getAttribute("nome");
        String email = (String) session.getAttribute("email");
        String senha = (String) session.getAttribute("senha");
        String cnpj= (String) session.getAttribute("cnpj");
        String telefone = (String) session.getAttribute("telefone");
        int capacidade = (Integer) session.getAttribute("capacidade");
        EtaDTO etaDTO = new EtaDTO();

        etaDTO.setNome(nome);
        etaDTO.setCapacidade(capacidade);
        etaDTO.setRua(request.getParameter("rua"));
        etaDTO.setNumero(Integer.parseInt(request.getParameter("numero")));
        etaDTO.setBairro(request.getParameter("bairro"));
        etaDTO.setCidade(request.getParameter("cidade"));
        etaDTO.setEstado(request.getParameter("estado"));
        etaDTO.setCep(request.getParameter("cep"));
        String regex = "\\D";
        String cnpjFormatado = cnpj.replaceAll(regex,"");
        String telefoneFormatado = telefone.replaceAll(regex,"");
        etaDTO.setCnpj(cnpjFormatado);
        etaDTO.setTelefone(telefoneFormatado);

        // Cria DTO do Admin vinculado à ETA
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setNome(nome);
        adminDTO.setEmail(email);
        String senhaDigitada = senha;
        // Valida a senha e coleta os erros
        var errosSenha = Validador.validarSenha(senhaDigitada);

        if (!errosSenha.isEmpty()) {
            // Se houver erros, encaminha para o JSP de erro mostrando só os erros da senha
            request.setAttribute("errosSenha", errosSenha);
            RequestDispatcher rd = request.getRequestDispatcher("/paginasCrud/erroSenha.jsp");
            rd.forward(request, response);
            return;
        }
        // Se estiver tudo certo, criptografa e segue
        String senhaCrip = SenhaHash.hashSenha(senhaDigitada);
        adminDTO.setSenha(senhaCrip);

        // Chama DAO
        EtaDAO dao = new EtaDAO();
        int idEta = dao.inserir(etaDTO, adminDTO);

        // se a o cadastro estiver correto ele manda para a pagina de login
        if (idEta > 0) {
            response.sendRedirect(request.getContextPath() + "/paginasCrud/admin/logar.jsp");
        } else {
            request.setAttribute("erro", "Erro ao cadastrar ETA.");
            RequestDispatcher rd = request.getRequestDispatcher("/paginasCrud/erroSenha.jsp");
            rd.forward(request, response);
        }
    }


    protected void buscarEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Setta o id no EtaDTO
        EtaDTO etaDTO = new EtaDTO();
        etaDTO.setId(Integer.parseInt(req.getParameter("id")));
        etaDAO.buscarPorId(etaDTO);

        req.setAttribute("eta", etaDTO); //Para o JSP pegar os valores

        //Envia para a página de alterar
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/eta/etaAlterar.jsp");
        rd.forward(req, resp);
    }

    protected void alterarEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Settando os valores da ETA
        EtaDTO etaDTO = new EtaDTO();
        etaDTO.setId(Integer.parseInt(req.getParameter("id")));
        etaDTO.setNome(req.getParameter("nome"));
        etaDTO.setCapacidade(Integer.parseInt(req.getParameter("capacidade")));
        etaDTO.setTelefone(req.getParameter("telefone"));
        etaDTO.setCnpj(req.getParameter("cnpj"));

        //E em relação a endereço
        etaDTO.setRua(req.getParameter("rua"));
        etaDTO.setBairro(req.getParameter("bairro"));
        etaDTO.setCidade(req.getParameter("cidade"));
        etaDTO.setEstado(req.getParameter("estado"));
        etaDTO.setNumero(Integer.parseInt(req.getParameter("numero")));
        etaDTO.setCep(req.getParameter("cep"));

        //Chamando o produtoDAO
        int resultado = etaDAO.alterarEta(etaDTO);

        if (resultado == 1) {
            //Redireciona para a listagem por ETA
            resp.sendRedirect(req.getContextPath() + "/ServletEta?action=mainEta");
        } else {
            // Página de erro
            req.setAttribute("erro", "Não foi possível alterar a ETA! Verifique os campos e tente novamente.");
            req.getRequestDispatcher("/paginasCrud/erro.jsp").forward(req, resp);
        }
    }


    // ======================
    // MÉTODO DE LISTAGEM DA ETA POR ADMIN LOGADO
    // ======================
    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //listo a eta por admin, filtrando a tela inicial que mostra alguns dados da eta para que mostre apenas da eta em que o o admin faz parte

        List<EtaDTO> listEtas = etaDAO.listarEta();

        // Passa para o JSP
        req.setAttribute("etas", listEtas);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/eta/etaIndex.jsp");
        rd.forward(req, resp);
    }


    protected void filtrarEta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<EtaDTO> lista = etaDAO.filtroBuscaPorColuna(req.getParameter("nome_coluna"),req.getParameter("pesquisa"));

        req.setAttribute("etas", lista); //Devolve a lista de etas encontrados
        //Envia para a página principal
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/eta/etaIndex.jsp");
        rd.forward(req, resp);
    }
}
