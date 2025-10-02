package com.example.servletsvireya.dao;

import com.example.servletsvireya.model.Admin;
import com.example.servletsvireya.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AdminDAO{
    Conexao conexao = new Conexao();
    public int inserirAdmin(Admin admin) {
        Connection conn = conexao.conectar();

        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Admin (nome, email,senha) VALUES (?, ?, ?)")){
            pstmt.setString(1, admin.getNome());
            pstmt.setString(2, admin.getEmail());
            pstmt.setString(3, admin.getSenha());

//            return pstmt.executeUpdate();
            if (pstmt.executeUpdate() > 0){
                return 1;
            } else{
                return 0;
            }
        } catch (SQLException exceptionSQL) {
            return -1;
        } finally {
            conexao.desconectar();
        }
    }

    public List<Admin> buscarAdmin(Admin admin) {
        List<Admin> admins = new ArrayList<>();
        Connection conn = conexao.conectar();

        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Admin WHERE id = ?")){
            pstmt.setInt(1, admin.getId());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                admin.setSenha(rs.getString("senha"));
                admin.setNome(rs.getString("nome"));
                admin.setEmail(rs.getString("email"));
                admins.add(admin);
            }

            return admins;
        } catch (SQLException e) {

            return null;
        } finally {
            conexao.desconectar();
        }
    }


    public int alterarAdmin(Admin admin) {
        Connection conn = conexao.conectar();
        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE Admin SET nome = ?, email = ? WHERE id = ?")){
            pstmt.setString(1, admin.getNome());
            pstmt.setString(2, admin.getEmail());
            pstmt.setInt(3, admin.getId());
            return pstmt.executeUpdate();

        } catch (SQLException sqle) {
            return -1;
        } finally {
            conexao.desconectar();
        }
    }

    public int removerAdmin(String cnpj) {
        Connection conn = conexao.conectar();
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Admin WHERE cnpj = ?")){
            pstmt.setString(1, cnpj);
            return pstmt.executeUpdate();
        } catch (SQLException sqle) {
            return -1;
        } finally {
            conexao.desconectar();
        }
    }
}
