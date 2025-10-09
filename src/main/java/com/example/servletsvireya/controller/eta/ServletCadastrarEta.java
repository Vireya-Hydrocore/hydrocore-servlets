package com.example.servletsvireya.controller.eta;

import com.example.servletsvireya.dao.AdminDAO;
import com.example.servletsvireya.dao.Endereco;
import com.example.servletsvireya.dao.EtaDAO;
import com.example.servletsvireya.dto.AdminDTO;
import com.example.servletsvireya.dto.EtaDTO;
import com.example.servletsvireya.model.Admin;
import com.example.servletsvireya.model.Eta;
import com.example.servletsvireya.util.Validador;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
@WebServlet(name = "ServletCadastrarEta", value = "/servlet-cadastrar-eta")
public class ServletCadastrarEta extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Pegando dados do formulário
        String nome = req.getParameter("nome");
        String email = req.getParameter("email");
        String cnpj = req.getParameter("cnpj");
        String senha = req.getParameter("senha");
        int capacidade = Integer.parseInt(req.getParameter("capacidade"));
        String telefone = req.getParameter("telefone");
        if (Validador.ehPositivo(capacidade)){

            cnpj = cnpj.replaceAll("\\D","");
            telefone = telefone.replaceAll("\\D","");

            // Criando objeto Admin


            EtaDTO etaDTO = new EtaDTO();
            etaDTO.setIdEndereco(4);
            etaDTO.setEstadoEndereco("São Paulo");
            etaDTO.setRuaEndereco("Carapicuibas");
            etaDTO.setCepEndereco("1234");
            etaDTO.setNumeroEndereco(12);
            etaDTO.setBairroEndereco("São Paulo");
            etaDTO.setCidadeEndereco("Sãop Paulo");

            Endereco endereco = new Endereco();
            int idEnderecoGerado = endereco.inserirEndereco(etaDTO);

            if (idEnderecoGerado <= 0) {
                resp.sendRedirect("erro.jsp");
                return;
            };

            etaDTO.setNome(nome);
            etaDTO.setCapacidade(capacidade);
            etaDTO.setCnpj(cnpj);
            etaDTO.setTelefone(telefone);


            EtaDAO etaDao = new EtaDAO();
            int idEtaGerado = etaDao.inserirEtaERetornarId(etaDTO);

            if (idEtaGerado <= 0) {
                resp.sendRedirect("erro.jsp");
                return;
            }

            AdminDTO adminDTO = new AdminDTO();
            adminDTO.setNome(nome);
            adminDTO.setEmail(email);
            adminDTO.setSenha(senha);
            adminDTO.setIdEta(idEtaGerado);


            // Inserindo no banco via DAO

            AdminDAO adminDAO = new AdminDAO();
            int resultado = adminDAO.inserirAdmin(adminDTO);

            if (resultado > 0) {
                resp.sendRedirect("/paginasCrud/admin/indexAdmin.jsp"); //sucesso
            } else {
                resp.sendRedirect("erro.jsp"); //erro
            }
        }else {
            resp.sendRedirect("erro.jsp");
        }

    }
}
