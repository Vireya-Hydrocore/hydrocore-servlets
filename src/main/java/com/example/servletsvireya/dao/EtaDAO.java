package com.example.servletsvireya.dao;

import com.example.servletsvireya.model.Eta;
import com.example.servletsvireya.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtaDAO {
    Conexao conexao;

    public EtaDAO(){ this.conexao = new Conexao(); }

    // Método inserirEta()
    public int inserirETA(Eta eta){
        Connection conn = conexao.conectar();
        String comando = "INSERT INTO ETA (id, nome, capacidade) " +
                "VALUES (?, ?, ?)";
        try(PreparedStatement pstmt = conn.prepareStatement(comando)){
            pstmt.setInt(1, eta.getId());
            pstmt.setString(2, eta.getNome());
            pstmt.setInt(3, eta.getCapacidade());
            pstmt.setString(4, eta.getTelefone());

            return pstmt.executeUpdate(); //
        }
        catch (SQLException sqle){
            sqle.printStackTrace();
            return -1;
        }
        finally {
            conexao.desconectar();
        }
    }

    // Método buscarETA()

    public List<Eta> buscarETA(){
        ResultSet rs = null;
        List<Eta> etas = new ArrayList<>();

        String comando = "INSERT INTO ETA (id, nome, capacidade) VALUES (?, ?, ?)";
        try(Connection conn = conexao.conectar(); PreparedStatement pstmt = conn.prepareStatement(comando)){

            rs = pstmt.executeQuery();

            while(rs.next()){
                Eta eta = new Eta();
                eta.setId(rs.getInt("id"));
                eta.setNome(rs.getString("nome"));
                eta.setCapacidade(rs.getInt("capacidade"));
                eta.setTelefone(rs.getString("telefone"));

                //Populando o list
                etas.add(eta);
            }
            return etas;

        }catch (SQLException sqle){
            sqle.printStackTrace();
            return new ArrayList<>();
        }finally {
            conexao.desconectar();
        }
    }


//    // Método buscarTodasEtas()
//    public List<Eta> buscarTodasEtas() {
//
//        List<Eta> etas = new ArrayList<>();
//        Connection conn = conexao.conectar();
//
//        try (Statement stmt = conn.createStatement()) {
//
//            ResultSet rs = stmt.executeQuery("SELECT * FROM ETA");
//
//            while (rs.next()) {
//                Eta eta = new Eta();
//                eta.setId(rs.getInt("id"));
//                eta.setNome(rs.getString("nome"));
//                eta.setCapacidade(rs.getInt("capacidade"));
//                eta.setTelefone(rs.getString("telefone"));
//
//                etas.add(eta);
//            }
//
//            return etas;
//        }
//        catch (SQLException e) {
//            return new ArrayList<>();
//        }
//        finally {
//            conexao.desconectar();
//        }
//    }

    // Método alterarETA()
    public int alterarETA(Eta eta){
        Connection conn = conexao.conectar();
        try(PreparedStatement pstmt = conn.prepareStatement("UPDATE ETA SET nome = ?, capacidade = ? WHERE id = ?")){

            pstmt.setString(1, eta.getNome());
            pstmt.setInt(2, eta.getCapacidade());
            pstmt.setString(3, eta.getTelefone());
            pstmt.setInt(4, eta.getId());

            return pstmt.executeUpdate();
        }
        catch (SQLException sqle){
            return -1;
        }
        finally {
            conexao.desconectar();
        }
    }


    // Método remover()
    public int removerETA(Eta eta){
        Connection conn = conexao.conectar();
        try(PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ETA WHERE id = ?")){
            pstmt.setInt(1,eta.getId());

            //aqui verificar se a eta existe

            if (pstmt.executeUpdate() > 0) {
                return 1; //Deleção bem sucedida
            } else {
                return 0;
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    // Método removerDuplicadas()
    public int removerDuplicadas(){
        Connection conn = conexao.conectar();
        try(PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ETA WHERE id NOT IN (SELECT MIN(id) FROM ETA GROUP BY nome, capacidade)")){

            int qtdRemovida = pstmt.executeUpdate();
            return qtdRemovida;
        }
        catch (SQLException sqle){
            return -1;
        }
        finally {
            conexao.desconectar();
        }
    }
}
