package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.CargoDTO;
import com.example.servletsvireya.dto.EstoqueDTO;
import com.example.servletsvireya.model.Cargo;
import com.example.servletsvireya.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {
    private Conexao conexao = new Conexao();


    // ========== Método para inserir um cargo ========== //
    public int inserirCargo(CargoDTO cargo) {
        Connection conn = conexao.conectar(); //Criando conexão com o banco
        //Preparando comando de inserção
        String comando = "INSERT INTO cargo (nome, acesso, id_eta) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            //Settando nos parâmetros do comando
            pstmt.setString(1, cargo.getNome());
            pstmt.setInt(2, cargo.getAcesso());
            pstmt.setInt(3, cargo.getIdEta()); // Novo campo

            if (pstmt.executeUpdate() > 0) {
                return 1; //Inserção bem sucedida
            } else {
                return 0; //Não deu certo
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1; //-1 se cair em exceção
        } finally {
            conexao.desconectar(); //Desconecta mesmo se passar pela exceção
        }
    }


    // ========== Método para remover um cargo ========== //
    public int removerCargo(CargoDTO cargo) {
        Connection conn = conexao.conectar();
        String comando = "DELETE FROM Cargo WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, cargo.getId());

            return (pstmt.executeUpdate() > 0) ? 1 : 0; //Forma simplificada para retornar

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para buscar um cargo pelo ID ========== //
    public CargoDTO buscarPorId(CargoDTO cargo) {
        Connection conn = conexao.conectar();
        String comando = "SELECT * FROM cargo WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, cargo.getId());
            ResultSet rs = pstmt.executeQuery(); //Armazena os dados da query

            if (rs.next()) { //Se houver registro
                cargo.setId(rs.getInt("id")); //Setta no mesmo objeto vindo como parâmetro
                cargo.setNome(rs.getString("nome"));
                cargo.setAcesso(rs.getInt("acesso"));
                cargo.setIdEta(rs.getInt("id_eta")); // Adiciona o id_eta também
            }
            return cargo;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        } finally {
            conexao.desconectar();
        }
    }


//    // 🔍 LISTAR CARGOS POR ETA (sem depender de funcionário)
//    public List<CargoDTO> listarCargoPorEta(int idEta) {
//        List<CargoDTO> cargos = new ArrayList<>();
//        String sql = "SELECT id, nome, acesso, id_eta FROM Cargo WHERE id_eta = ? ORDER BY nome";
//
//        try (Connection conn = conexao.conectar();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setInt(1, idEta);
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                CargoDTO dto = new CargoDTO();
//                dto.setId(rs.getInt("id"));
//                dto.setNome(rs.getString("nome"));
//                dto.setAcesso(rs.getInt("acesso"));
//                dto.setIdEta(rs.getInt("id_eta"));
//                cargos.add(dto);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            conexao.desconectar();
//        }
//        return cargos;
//    }


//    public List<CargoDTO> buscarPorNome(String nomeCargo) { //busca id_eta tambem
//        List<CargoDTO> lista = new ArrayList<>();
//        Connection conn = conexao.conectar();
//        String sql = "SELECT c.*, e.nome AS nome_eta FROM cargo c " +
//                "JOIN funcionario f ON c.id = f.id_cargo " +
//                "JOIN eta e ON e.id = f.id_eta " +
//                "WHERE LOWER(nome) = LOWER(?)";
//
//        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, nomeCargo);
//            ResultSet rset = pstmt.executeQuery();
//
//            if (rset.next()) {
//                CargoDTO dto = new CargoDTO();
//                dto.setId(rset.getInt("id"));
//                dto.setNome(rset.getString("nome"));
//                dto.setAcesso(rset.getInt("acesso"));
//                dto.setIdEta(rset.getInt("id_eta"));
//                dto.setNomeEta(rset.getString("nome_eta"));
//                lista.add(dto);
//            }
//            return lista;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return new ArrayList<>();
//        } finally {
//            conexao.desconectar();
//        }
//    }


    // ========== Método para buscar o ID de cargo pelo nome ========== //
    public Integer buscarIdPorNome(String nomeCargo) {
        Connection conn = conexao.conectar();
        Integer id = null;
        String comando = "SELECT id FROM cargo WHERE LOWER(nome) = LOWER(?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setString(1, nomeCargo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }
            return id; //Retorna o id ou vazio

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        } finally{
            conexao.desconectar();
        }
    }


    // ========== Método para alterar um cargo ========== //
    public int alterarCargo(CargoDTO cargo) {
        Connection conn = conexao.conectar();
        String comando = "UPDATE cargo SET nome = ?, acesso = ?, id_eta = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setString(1, cargo.getNome());
            pstmt.setInt(2, cargo.getAcesso());
            pstmt.setInt(3, cargo.getIdEta());
            pstmt.setInt(4, cargo.getId()); //WHERE id = ?

            return (pstmt.executeUpdate() > 0) ? 1 : 0;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        }
    }


    // ========== Método para listar os cargos ========== //
    public List<CargoDTO> listarCargos() {
        Connection conn = conexao.conectar();
        List<CargoDTO> cargos = new ArrayList<>();
        //Prepara o comando SQL com JOIN
        String comando = "SELECT c.*, e.nome AS nome_eta FROM cargo c " +
                "JOIN eta e ON e.id = c.id_eta " +
                "ORDER BY c.nome";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)){
            ResultSet rs = pstmt.executeQuery(comando);

            while (rs.next()) {
                CargoDTO cargo = new CargoDTO(); //Cria um objeto a cada repetição

                cargo.setId(rs.getInt("id"));
                cargo.setNome(rs.getString("nome"));
                cargo.setAcesso(rs.getInt("acesso"));
                cargo.setIdEta(rs.getInt("id_eta"));
                cargo.setNomeEta(rs.getString("nome_eta"));

                cargos.add(cargo); //Populando o list
            }
            return cargos;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>(); //List vazio
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para filtrar cargo(s) ========== //
    public List<CargoDTO> filtroBuscaPorColuna(String coluna, String pesquisa) {
        int novapesquisa = 0;
        String tabela;
        String operador = "LIKE"; // Por padrão

        if (coluna.equalsIgnoreCase("nome_eta")) {
            tabela = "ETA";
            coluna = "NOME";
        } else if (coluna.equalsIgnoreCase("acesso")) {
            tabela = "CARGO";
            operador = "="; // muda o operador
            novapesquisa = Integer.parseInt(pesquisa);
        } else {
            tabela = "CARGO";
        }

        Connection conn = conexao.conectar(); //Criando conexão com o banco
        List<CargoDTO> lista = new ArrayList<>();
        String comando =
                "SELECT CARGO.*, ETA.NOME AS nome_eta " +
                        "FROM CARGO " +
                        "JOIN ETA ON ETA.id = CARGO.id_eta " +
                        "WHERE " + tabela + "." + coluna + " " + operador + " ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {

            // Define o tipo de dado corretamente
            if (coluna.equalsIgnoreCase("acesso")) {
                pstmt.setInt(1, novapesquisa);
            } else {
                pstmt.setString(1, "%" + pesquisa + "%");
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                CargoDTO cargo = new CargoDTO();
                cargo.setId(rs.getInt("id"));
                cargo.setNome(rs.getString("nome"));
                cargo.setAcesso(rs.getInt("acesso"));
                cargo.setIdEta(rs.getInt("id_eta"));
                cargo.setNomeEta(rs.getString("nome_eta")); //Campo extra do DTO
                lista.add(cargo);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>(); //List vazio
        }

        return lista; //Retorna a list contendo os resultados ou vazia
    }
}