package com.example.servletsvireya.controller;

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

@WebServlet(urlPatterns = {"/ServletEta", "/mainEta"})
public class ServletEta extends HttpServlet {

    private EtaDAO etaDAO = new EtaDAO();


    // GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("mainEta".equals(action)) { //não usei urlpatterns mais usei a comparação de ações
            listarPorAdmin(request, response);
        } else {
            // Redireciona para login caso nenhuma ação seja passada
            response.sendRedirect(request.getContextPath() + "/paginasCrud/menu/logar.jsp");//
        }
    }

    // POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("cadastrar".equals(action)) {
            cadastrarEta(request, response); //cadastro escondido que também compara ação
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
        EtaDTO etaDTO = new EtaDTO();
        String nomeEta = request.getParameter("nome");
        etaDTO.setNome(nomeEta);
        etaDTO.setCapacidade(Integer.parseInt(request.getParameter("capacidade")));
        etaDTO.setRua(request.getParameter("rua"));
        etaDTO.setNumero(Integer.parseInt(request.getParameter("numero")));
        etaDTO.setCnpj(request.getParameter("cnpj"));
        etaDTO.setBairro(request.getParameter("bairro"));
        etaDTO.setCidade(request.getParameter("cidade"));
        etaDTO.setEstado(request.getParameter("estado"));
        etaDTO.setTelefone(request.getParameter("telefone"));
        etaDTO.setCep(request.getParameter("cep"));
        String regex = "\\D+";


        // Cria DTO do Admin vinculado à ETA
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setNome(nomeEta);
        adminDTO.setEmail(request.getParameter("adminEmail"));

        String senhaDigitada = request.getParameter("adminSenha");

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

    // ======================
    // MÉTODO DE LISTAGEM DA ETA POR ADMIN LOGADO
    // ======================
    private void listarPorAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //listo a eta por admin, filtrando a tela inicial que mostra alguns dados da eta para que mostre apenas da eta em que o o admin faz parte

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idEta") == null) {
            response.sendRedirect(request.getContextPath() + "/paginasCrud/erroSenha.jsp");
            return;
        }

        int idEta = (Integer) session.getAttribute("idEta");

        // Cria DTO com o ID
        EtaDTO etaDTO = new EtaDTO();
        etaDTO.setId(idEta);

        // Chama o DAO para preencher os dados
        EtaDTO etaDTOS = etaDAO.buscarPorId(etaDTO);

        // Passa para o JSP
        request.setAttribute("eta", etaDTOS);
        RequestDispatcher rd = request.getRequestDispatcher("/paginasCrud/eta/etaIndex.jsp");
        rd.forward(request, response);
    }

}
