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
    Conexao conexao = new Conexao();
    private static final Dotenv dotenv = Dotenv.load();

    private static final String ADMIN_EMAIL_AUTORIZADO = dotenv.get("ADMIN_EMAIL");
    private static final String ADMIN_SENHA = dotenv.get("ADMIN_SENHA");
    private static final String ADMIN_SENHA_HASH = SenhaHash.hashSenha(ADMIN_SENHA);

    public int inserirAdmin(AdminDTO adminDTO) {
        Connection conn = conexao.conectar();
        String comando = "INSERT INTO admin (nome, email,senha, id_eta) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setString(1, adminDTO.getNome());
            pstmt.setString(2, adminDTO.getEmail());
            if (adminDTO.getSenha().length() > 60) {
                return 0;
            }
            pstmt.setString(3, adminDTO.getSenha());
            pstmt.setInt(4, adminDTO.getIdEta());

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

    public AdminDTO buscarPorId(AdminDTO adminDTO) {
        Connection conn = conexao.conectar();

        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM admin WHERE id = ?")) {
            pstmt.setInt(1, adminDTO.getId());
            ResultSet rset = pstmt.executeQuery();

            if (rset.next()) {
                adminDTO.setSenha(rset.getString("senha")); //Setta no mesmo objeto
                adminDTO.setNome(rset.getString("nome"));
                adminDTO.setEmail(rset.getString("email"));
                return adminDTO;
            } else {
                return null;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        } finally {
            conexao.desconectar();
        }
    }


    public int alterarAdmin(AdminDTO adminDTO) {
        Connection conn = conexao.conectar();
        String comando = "UPDATE admin SET nome = ?, email = ?, senha = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
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

    public int removerAdmin(AdminDTO adminDTO) {
        Connection conn = conexao.conectar();
        String comando = "DELETE FROM admin WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
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


    public List<AdminDTO> listarAdmin() {
        List<AdminDTO> admins = new ArrayList<>();
        Connection conn = conexao.conectar();
        String comando = "SELECT a.*, e.nome AS nome_eta FROM admin a " +
                "JOIN eta e ON e.id = a.id_eta";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                AdminDTO admin = new AdminDTO(); //Instanciando um objeto a cada while

                admin.setId(rs.getInt("id"));
                admin.setNome(rs.getString("nome"));
                admin.setEmail(rs.getString("email"));
                admin.setSenha(rs.getString("senha"));
                admin.setIdEta(rs.getInt("id_eta")); //tem que ser a mesma em cada
                admin.setNomeEta(rs.getString("nome_eta"));

                admins.add(admin); //Populando o list
            }
            return admins;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>(); //Lista vazia
        } finally {
            conexao.desconectar();
        }
    }


    public String ListarporEmail(String email) {
        Connection conn = conexao.conectar();

        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Admin WHERE email= ?")) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return (rs.getString("senha"));
            } else {
                return null;
            }


        } catch (SQLException e) {//Lista vazia
            return null;
        } finally {
            conexao.desconectar();
        }
    }


    public Integer seLogar(String email, String senha) {
        String sql = "SELECT id, senha FROM admin WHERE email = ?"; // busca id do admin e senha

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String senhaBanco = rs.getString("senha");

                // Verifica se a senha informada bate com a do banco
                if (SenhaHash.verificarSenha(senha, senhaBanco)) {
                    return rs.getInt("id"); // retorna o id do admin
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // login incorreto
    }


    public String buscarSenhaPorEmail(String email) {
        String senha = null;
        String sql = "SELECT senha FROM admin WHERE email = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    senha = rs.getString("senha");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar senha por e-mail: " + e.getMessage());
        }

        return senha; // se não encontrar, retorna null
    }

    public List<AdminDTO> listarEtas() {
        List<AdminDTO> admins = new ArrayList<>();
        Connection conn = conexao.conectar();
        String comando = "SELECT * FROM admin";

        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(comando);
            while (rs.next()) {
                AdminDTO admin = new AdminDTO(); //Instanciando um objeto a cada while

                admin.setId(rs.getInt("id"));
                admin.setNome(rs.getString("nome"));
                admin.setEmail(rs.getString("email"));
                admin.setSenha(rs.getString("senha"));
                admin.setIdEta(rs.getInt("id_eta")); //tem que ser a mesma em cada

                admins.add(admin); //Populando o list
            }
            return admins;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>(); //Lista vazia
        } finally {
            conexao.desconectar();
        }
    }


    public boolean seLogarAreaRestrita(String email, String senha) {
        // Apenas o seu email permitido
        String emailPermitido = ADMIN_EMAIL_AUTORIZADO;
        String senhaPermitida = ADMIN_SENHA; // ou carregue de um dotenv

        // Confere se bate com os valores permitidos
        if (email.equals(emailPermitido) && senha.equals(senhaPermitida)) {
            return true; // login correto
        } else {
            return false; // login incorreto
        }
    }

    public List<AdminDTO> filtroBuscaPorColuna(String coluna, String pesquisa) {
        String tabela;
        if (coluna.equalsIgnoreCase("nome_eta")) {
            tabela = "ETA";
            coluna = "NOME";
        } else {
            tabela = "ADMIN";
        }
        String sql =
                "SELECT ADMIN.*, ETA.NOME AS nome_eta " +
                        "FROM ADMIN " +
                        "JOIN ETA ON ETA.id = ADMIN.id_eta " +
                        "WHERE "+tabela+"." + coluna + " LIKE ?";

        List<AdminDTO> lista = new ArrayList<>();

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + pesquisa + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AdminDTO dto = new AdminDTO();
                dto.setId(rs.getInt("id"));
                dto.setNome(rs.getString("nome"));
                dto.setEmail(rs.getString("email"));
                dto.setSenha(rs.getString("senha"));
                dto.setIdEta(rs.getInt("id_eta"));
                dto.setNomeEta(rs.getString("nome_eta"));
                lista.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }



}


