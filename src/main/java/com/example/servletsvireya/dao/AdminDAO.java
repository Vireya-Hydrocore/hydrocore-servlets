package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.AdminDTO;
import com.example.servletsvireya.model.Admin;
import com.example.servletsvireya.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    Conexao conexao = new Conexao();

    public int inserirAdmin(AdminDTO adminDTO) {
        Connection conn = conexao.conectar();
        String comando = "INSERT INTO admin (nome, email,senha, id_eta) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setString(1, adminDTO.getNome());
            pstmt.setString(2, adminDTO.getEmail());
            if (adminDTO.getSenha().length() > 30) {
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


    public List<AdminDTO> listarAdminPorEta(int idEta) {
        List<AdminDTO> admins = new ArrayList<>();
        Connection conn = conexao.conectar();
        String comando = "SELECT * FROM admin WHERE id_eta = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, idEta);

            ResultSet rs = pstmt.executeQuery();
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

    public Integer seLogar(String email, String senha) {
        String sql = "SELECT a.*, e.nome AS nomeEta FROM admin a " +
                "JOIN eta e on a.id_eta = e.id " +
                "WHERE email = ? AND senha = ? ";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, senha);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                int id_eta = rs.getInt("id_eta");
                String nomeEta = rs.getString("nomeEta");
                String nome = rs.getString("nome");
                Admin admin = new Admin(nome,email,senha);
                AdminDTO adminDTO = new AdminDTO(id,admin,id_eta,nomeEta);
                return id_eta; // retorna o id_eta do admin encontrado
            } else {
                System.out.println("passou");
                return null; // login incorreto
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("excecao");
            return null;
        }
    }
}