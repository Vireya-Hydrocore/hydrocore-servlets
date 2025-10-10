package com.example.servletsvireya.controller;

//import com.example.servletsvireya.util.SenhaHash;


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

@WebServlet(urlPatterns = {"/ServletEta", "/mainEta"}, name = "ServletEta")
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
            // comportamento padrão: listar admins (ou redirecionar)
            listar(req, resp);
            return;
        }

        switch (action){
            case "mainEta":
                listar(req, resp);
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

        if ("cadastrar".equals(action)) {
            cadastrarEta(req, resp); //cadastro escondido que também compara ação
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
        String cnpj = request.getParameter("cnpj");
        String telefone = request.getParameter("telefone");
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
//        String senhaCrip = SenhaHash.hashSenha(senhaDigitada);
//        adminDTO.setSenha(senhaCrip);


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
    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //listo a eta por admin, filtrando a tela inicial que mostra alguns dados da eta para que mostre apenas da eta em que o o admin faz parte

        List<EtaDTO> listEtas = etaDAO.listarEta();

        // Passa para o JSP
        req.setAttribute("etas", listEtas);
        RequestDispatcher rd = req.getRequestDispatcher("/paginasCrud/eta/etaIndex.jsp");
        rd.forward(req, resp);
    }
}
