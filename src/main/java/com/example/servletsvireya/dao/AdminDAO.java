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
        String comando = "INSERT INTO admin (nome, email,senha) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setString(1, adminDTO.getNome());
            pstmt.setString(2, adminDTO.getEmail());
            pstmt.setString(3, adminDTO.getSenha());

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

            if (rset != null) {
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

            if (pstmt.executeUpdate() > 0){
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

            if (pstmt.executeUpdate() > 0){
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
}