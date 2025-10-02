package com.example.servletsvireya.dao;

import com.example.servletsvireya.model.Cargo;
import com.example.servletsvireya.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {
    private Conexao conexao = new Conexao();
    //Método inserirCargo()
    public int inserirCargo(Cargo cargo){

        //Preparando o comando
        String comandoSQL = "INSERT INTO Cargo(id, nome, acesso) VALUES (?,?,?)";
        try (Connection conn = conexao.conectar(); PreparedStatement pstmt = conn.prepareStatement(comandoSQL)){

            //Definindo os valores do comando
            pstmt.setInt(1, cargo.getId());
            pstmt.setString(2, cargo.getNome());
            pstmt.setString(3, cargo.getAcesso());

            //Retornando quantas linhas foram alteradas
            return pstmt.executeUpdate();
        } catch (SQLException e){
            return -1;
        }
    }
    public int removerCargo(Cargo cargo){
        String comandoSQL = "DELETE FROM Cargo WHERE id = ?";
        try (Connection conn = conexao.conectar(); PreparedStatement pstmt = conn.prepareStatement(comandoSQL)){

            //Verificando se o cargo existe
            if (buscarCargo(cargo.getId()) == null){
                return 0;
            }

            //Definindo os valores do comando
            pstmt.setInt(1, cargo.getId());

            pstmt.executeUpdate();
            return 1;
        } catch (SQLException e) {
            return -1;
        }
    }
    public List<Cargo> buscarCargo(int id){
        String comandoSQL = "SELECT * FROM Cargo WHERE id = ?";
        try (Connection conn = conexao.conectar(); PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {

            //Instanciando uma lista de cargos
            List<Cargo> cargos = new ArrayList<>();

            //Definindo os valores do comando
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                //Instanciando um objeto Cargo
                Cargo cargo = new Cargo();

                //Settando os valores
                cargo.setNome(rs.getString("NOME"));
                cargo.setId(rs.getInt("ID"));
                cargo.setAcesso(rs.getString("acesso"));

                //Adicionando o objeto na lista
                cargos.add(cargo);
            }
            return cargos;
        } catch (SQLException e){
            return null;
        }
    }
}
