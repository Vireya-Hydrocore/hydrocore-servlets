package com.example.servletsvireya.dao;
import com.example.servletsvireya.util.SenhaHash;
import com.example.servletsvireya.dto.AdminDTO;
import com.example.servletsvireya.model.Admin;
import com.example.servletsvireya.util.Conexao;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    private Conexao conexao = new Conexao(); // Mantido como private e instanciado no topo

    private static final Dotenv dotenv = Dotenv.load();
    private static final String ADMIN_EMAIL_AUTORIZADO = dotenv.get("ADMIN_EMAIL");
    private static final String ADMIN_SENHA = dotenv.get("ADMIN_SENHA");
    private static final String ADMIN_SENHA_HASH = SenhaHash.hashSenha(ADMIN_SENHA);

    // Variáveis de fluxo de método instanciadas/declaradas no topo
    private String comando;
    private AdminDTO adminDTO; // Usado para objetos temporários dentro dos loops
    private List<AdminDTO> lista; // Usado para listas retornadas, como em filtro
    private String tabela;
    private String coluna;
    private String emailPermitido;
    private String senhaPermitida;
    private String senhaBanco;


    // ========== Método para inserir um admin ========== //
    public int inserirAdmin(AdminDTO adminDTO) {
        Connection conn = conexao.conectar(); //Criando conexão com o banco
        //Preparando a instrução SQL
        comando = "INSERT INTO admin (nome, email, senha, id_eta) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comando);
            //Settando os valores no comando/pstmt
            pstmt.setString(1, adminDTO.getNome());
            pstmt.setString(2, adminDTO.getEmail());
            pstmt.setString(3, adminDTO.getSenha());
            pstmt.setInt(4, adminDTO.getIdEta());

            if (pstmt.executeUpdate() > 0) { //Se inserir
                return 1; //Se realizou a inserção
            } else {
                return 0; //Deu errado
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1; //Caiu em exceção
        } finally {
            conexao.desconectar(); //Finaliza a conexão, mesmo se cair em exceção
        }
    }


    // ========== Método para buscar um admin pelo ID ========== //
    public AdminDTO buscarPorId(AdminDTO adminDTO) {
        Connection conn = conexao.conectar();
        comando = "SELECT a.*, e.nome AS nome_eta FROM admin a JOIN ETA e on e.id=a.id_eta WHERE a.id = ?";

        try {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comando);
            pstmt.setInt(1, adminDTO.getId());
            ResultSet rset = pstmt.executeQuery(); //Armazena os dados no ResultSet

            if (rset.next()) {
                adminDTO.setSenha(rset.getString("senha")); //Setta no mesmo objeto
                adminDTO.setNome(rset.getString("nome"));
                adminDTO.setEmail(rset.getString("email"));
                adminDTO.setNomeEta(rset.getString("nome_eta"));
            }
            return adminDTO; //Retorna com o objeto preenchido ou vazio

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para alterar um admin ========== //
    public int alterarAdmin(AdminDTO adminDTO) {
        Connection conn = conexao.conectar();
        comando = "UPDATE admin SET nome = ?, email = ?, senha = ? WHERE id = ?";

        try {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comando);
            pstmt.setString(1, adminDTO.getNome());
            pstmt.setString(2, adminDTO.getEmail());
            pstmt.setString(3, adminDTO.getSenha());
            pstmt.setInt(4, adminDTO.getId());

            if (pstmt.executeUpdate() > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para remover um admin ========== //
    public int removerAdmin(AdminDTO adminDTO) {
        Connection conn = conexao.conectar();
        comando = "DELETE FROM admin WHERE id = ?";

        try {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comando);
            pstmt.setInt(1, adminDTO.getId());

            if (pstmt.executeUpdate() > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para listar os admins ========== //
    public List<AdminDTO> listarAdmin() {
        lista = new ArrayList<>(); // Instanciada no topo, re-instanciada aqui.
        Connection conn = conexao.conectar();
        //Preparando instrução SQL com JOIN
        comando = "SELECT a.*, e.nome AS nome_eta FROM admin a " +
                "JOIN eta e ON e.id = a.id_eta";

        try {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comando);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                adminDTO = new AdminDTO(); // Instanciada no topo, re-instanciada aqui
                adminDTO.setId(rs.getInt("id"));
                adminDTO.setNome(rs.getString("nome"));
                adminDTO.setEmail(rs.getString("email"));
                adminDTO.setSenha(rs.getString("senha"));
                adminDTO.setIdEta(rs.getInt("id_eta"));
                adminDTO.setNomeEta(rs.getString("nome_eta")); //Campo extra do DTO

                lista.add(adminDTO); //Populando o list
            }
            return lista;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>(); //Lista vazia
        } finally {
            conexao.desconectar();
        }
    }


//    public String ListarporEmail(String email) {
//        Connection conn = conexao.conectar();
//
//        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Admin WHERE email= ?")) {
//            pstmt.setString(1, email);
//            ResultSet rs = pstmt.executeQuery();
//            if (rs.next()) {
//                return (rs.getString("senha"));
//            } else {
//                return null;
//            }
//
//
//        } catch (SQLException e) {//Lista vazia
//            return null;
//        } finally {
//            conexao.desconectar();
//        }
//    }


    // ========== Método para logar um admin --> gerente da ETA ========== //
    public Integer seLogar(String email, String senha) {
        Connection conn = conexao.conectar();
        comando = "SELECT id, senha FROM admin WHERE email = ?"; // busca id do admin e senha

        try {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comando);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                senhaBanco = rs.getString("senha");

                // Verifica se a senha informada bate com a do banco
                if (SenhaHash.verificarSenha(senha, senhaBanco)) {
                    return rs.getInt("id"); // Retorna o id do admin
                }
            }
            return null; // login incorreto

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }


//    public String buscarSenhaPorEmail(String email) {
//        String senha = null;
//        String sql = "SELECT senha FROM admin WHERE email = ?";
//
//        try (Connection conn = conexao.conectar();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, email);
//
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    senha = rs.getString("senha");
//                }
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Erro ao buscar senha por e-mail: " + e.getMessage());
//        }
//
//        return senha; // se não encontrar, retorna null
//    }

//    public List<AdminDTO> listarEtas() {
//        List<AdminDTO> admins = new ArrayList<>();
//        Connection conn = conexao.conectar();
//        String comando = "SELECT * FROM admin";
//
//        try{
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery(comando);
//            while (rs.next()) {
//                AdminDTO admin = new AdminDTO(); //Instanciando um objeto a cada while
//
//                admin.setId(rs.getInt("id"));
//                admin.setNome(rs.getString("nome"));
//                admin.setEmail(rs.getString("email"));
//                admin.setSenha(rs.getString("senha"));
//                admin.setIdEta(rs.getInt("id_eta")); //tem que ser a mesma em cada
//
//                admins.add(admin); //Populando o list
//            }
//            return admins;
//
//        } catch (SQLException sqle) {
//            sqle.printStackTrace();
//            return new ArrayList<>(); //Lista vazia
//        } finally {
//            conexao.desconectar();
//        }
//    }


    // ========== Método para logar na área restrita --> desenvolvedores do projeto ========== //
    public boolean seLogarAreaRestrita(String email, String senha) {
        // Apenas o email permitido
        emailPermitido = ADMIN_EMAIL_AUTORIZADO;
        senhaPermitida = ADMIN_SENHA; // ou carregue de um dotenv

        // Confere se bate com os valores permitidos
        if (email.equals(emailPermitido) && senha.equals(senhaPermitida)) {
            return true; // login correto
        } else {
            return false; // login incorreto
        }
    }


    // ========== Método para filtrar um admin ========== //
    public List<AdminDTO> filtroBuscaPorColuna(String colunaParam, String pesquisa) {
        coluna = colunaParam;

        if (coluna.equalsIgnoreCase("nome_eta")) {
            tabela = "ETA";
            coluna = "NOME";
        } else {
            tabela = "ADMIN";
        }

        Connection conn = conexao.conectar();
        lista = new ArrayList<>(); // Instanciada no topo, re-instanciada aqui
        comando =
                "SELECT ADMIN.*, ETA.NOME AS nome_eta " +
                        "FROM ADMIN " +
                        "JOIN ETA ON ETA.id = ADMIN.id_eta " +
                        "WHERE "+tabela+"." + coluna + " LIKE ?";

        try {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comando);
            pstmt.setString(1, "%" + pesquisa + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                adminDTO = new AdminDTO(); // Instanciada no topo, re-instanciada aqui
                adminDTO.setId(rs.getInt("id"));
                adminDTO.setNome(rs.getString("nome"));
                adminDTO.setEmail(rs.getString("email"));
                adminDTO.setSenha(rs.getString("senha"));
                adminDTO.setIdEta(rs.getInt("id_eta"));
                adminDTO.setNomeEta(rs.getString("nome_eta"));
                lista.add(adminDTO);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>(); //Lista vazia
        }

        return lista; //Com os dados ou vazia
    }
}