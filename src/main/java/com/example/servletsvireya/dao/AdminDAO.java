package com.example.servletsvireya.dao;

import com.example.servletsvireya.model.Admin;
import com.example.servletsvireya.model.Cargo;
import com.example.servletsvireya.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    private Conexao conexao = new Conexao();
    //Método inserirAdmin()
    public int inserirAdmin(Admin admin){

        //Preparando o comando
        String comandoSQL = "INSERT INTO Admin(id, nome, email, idEta) VALUES (?,?,?,?)";
        try (Connection conn = conexao.conectar(); PreparedStatement pstmt = conn.prepareStatement(comandoSQL)){

            //Definindo os valores do comando
            pstmt.setInt(1, admin.getId());
            pstmt.setString(2, admin.getNome());
            pstmt.setString(3, admin.getEmail());
            pstmt.setInt(4, admin.getIdEta());

            //Retornando quantas linhas foram alteradas
            return pstmt.executeUpdate();
        } catch (SQLException e){
            return -1;
        }
    }
    public int removerAdmin(Admin admin){
        String comandoSQL = "DELETE FROM Admin WHERE id = ?";
        try (Connection conn = conexao.conectar(); PreparedStatement pstmt = conn.prepareStatement(comandoSQL)){

            //Verificando se o cargo existe
            if (buscarAdmin(admin.getId()) == null){
                return 0;
            }

            //Definindo os valores do comando
            pstmt.setInt(1, admin.getId());

            pstmt.executeUpdate();
            return 1;
        } catch (SQLException e) {
            return -1;
        }
    }
    public List<Admin> buscarAdmin(int id){
        String comandoSQL = "SELECT * FROM Admin WHERE id = ?";
        try (Connection conn = conexao.conectar(); PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {

            //Instanciando uma lista de admins
            List<Admin> admins = new ArrayList<>();

            //Definindo os valores do comando
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                //Instanciando um objeto admins
                Admin admin = new Admin();

                //Settando os valores
                admin.setNome(rs.getString("NOME"));
                admin.setId(rs.getInt("ID"));
                admin.setEmail(rs.getString("email"));

                //Adicionando o objeto na lista
                admins.add(admin);
            }
            return admins;
        } catch (SQLException e){
            return null;
        }
    }
}