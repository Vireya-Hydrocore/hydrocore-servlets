package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.FuncionarioDTO;
import com.example.servletsvireya.model.Funcionario;
import com.example.servletsvireya.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    private Conexao conexao = new Conexao();


    public int inserirFuncionario(FuncionarioDTO Funcionario) {
        Connection conn = conexao.conectar();
        String comando = "INSERT INTO funcionario (nome, email, senha, data_admissao, data_nascimento, id_cargo, id_eta) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setString(1, Funcionario.getNome());
            pstmt.setString(2, Funcionario.getEmail());
            pstmt.setString(3, Funcionario.getSenha());
            pstmt.setDate(4, (Date) (Funcionario.getDataAdmissao()));
            pstmt.setDate(5, (Date) (Funcionario.getDataNascimento()));
            pstmt.setInt(6, Funcionario.getIdCargo());
            pstmt.setInt(7, Funcionario.getIdEta());

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


    // LISTAR FUNCIONÁRIOS (com JOIN no cargo e eta)
    public List<FuncionarioDTO> listarFuncionariosPorEta(int idEta) {
        List<FuncionarioDTO> funcionarios = new ArrayList<>();
        Connection conn = conexao.conectar();

        String comandoSQL = "SELECT f.*, c.nome AS nome_cargo FROM Funcionario f JOIN cargo c ON f.id_cargo = c.id WHERE f.id_eta = ? ORDER BY f.nome";

        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {
            pstmt.setInt(1, idEta);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                FuncionarioDTO func = new FuncionarioDTO();
                func.setId(rs.getInt("id"));
                func.setNome(rs.getString("nome"));
                func.setEmail(rs.getString("email"));
                func.setDataAdmissao(rs.getDate("data_admissao"));
                func.setDataNascimento(rs.getDate("data_nascimento"));
                func.setIdEta(rs.getInt("id_eta"));
                func.setIdCargo(rs.getInt("id_cargo"));
                func.setSenha(rs.getString("senha"));
                func.setNomeCargo(rs.getString("nome_cargo"));

                funcionarios.add(func);
            }
            return funcionarios;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>();
        } finally {
            conexao.desconectar();
        }
    }


    // ALTERAR FUNCIONÁRIO
    public int alterarFuncionario(FuncionarioDTO funcionario) {
        Connection conn = conexao.conectar();
        String comando = " UPDATE funcionario SET nome = ?, email = ?, data_admissao = ?, data_nascimento = ?, id_cargo = ?, senha= ? WHERE id = ?";
        String delete= "DELETE FROM tarefa WHERE id_funcionario= ?" ;
        try(PreparedStatement pstmt2 = conn.prepareStatement(delete)) {
            pstmt2.setInt(1, funcionario.getId());
            pstmt2.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setString(1, funcionario.getNome());
            pstmt.setString(2, funcionario.getEmail());
            pstmt.setDate(3, new java.sql.Date(funcionario.getDataAdmissao().getTime()));
            pstmt.setDate(4, new java.sql.Date(funcionario.getDataNascimento().getTime()));
            pstmt.setInt(5, funcionario.getIdCargo());
            pstmt.setString(6, funcionario.getSenha());
            pstmt.setInt(7, funcionario.getId()); // WHERE id = ?

            return (pstmt.executeUpdate() > 0) ? 1 : 0;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    // REMOVER FUNCIONÁRIO
    public int removerFuncionario(int idFuncionario) {
        Connection conn = conexao.conectar();
        String comandoSQL = "DELETE FROM funcionario WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {
            pstmt.setInt(1, idFuncionario);
            return (pstmt.executeUpdate() > 0) ? 1 : 0;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    // REMOVER DUPLICADOS
    public int removerDuplicadas() {
        Connection conn = conexao.conectar();
        String comandoSQL = " DELETE FROM funcionario WHERE id NOT IN ( SELECT MIN(id) FROM funcionario GROUP BY nome, email, data_admissao, data_nascimento, id_eta, id_cargo";

        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {
            return (pstmt.executeUpdate() > 0) ? 1 : 0;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    public FuncionarioDTO buscarPorId(FuncionarioDTO funcionario) { //por id
        ResultSet rset = null;
        Connection conn = conexao.conectar();
        String comando = "SELECT f.*, c.nome AS nome_cargo, e.nome AS nome_eta FROM funcionario f " +
                "JOIN cargo c ON c.id = f.id_cargo " +
                "JOIN eta e ON e.id = f.id_eta " +
                "WHERE f.id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, funcionario.getId());
            rset = pstmt.executeQuery();

            if (rset == null){
                return null;
            }

            if (rset.next()) {
                funcionario.setNome(rset.getString("nome"));
                funcionario.setEmail(rset.getString("email"));
                funcionario.setSenha(rset.getString("senha"));
                funcionario.setDataAdmissao(rset.getDate("data_admissao"));
                funcionario.setDataNascimento(rset.getDate("data_nascimento"));
                funcionario.setIdEta(rset.getInt("id_eta"));
                funcionario.setNomeCargo(rset.getString("nome_cargo")); // campo extra do DTO
                funcionario.setIdCargo(rset.getInt("id_cargo"));
                funcionario.setNomeEta(rset.getString("nome_eta")); // campo extra do DTO
            }
            return funcionario; // Retorna com os dados preenchidos no mesmo objeto DTO

        } catch (SQLException e) {
            e.printStackTrace();
            return null; //vazio se nao achou
        } finally {
            conexao.desconectar();
        }
    }


//    public FuncionarioDTO buscarPorId(int id) {
//        FuncionarioDTO funcionario = null;
//        String sql = "SELECT * FROM funcionario WHERE id = ?";
//        try (Connection conn = conexao.conectar();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, id);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                funcionario = new FuncionarioDTO();
//                funcionario.setId(rs.getInt("id"));
//                funcionario.setNome(rs.getString("nome"));
//                funcionario.setEmail(rs.getString("email"));
//                funcionario.setSenha(rs.getString("senha"));
//                funcionario.setIdCargo(rs.getInt("id_cargo"));
//                funcionario.setIdEta(rs.getInt("id_eta"));
//                funcionario.setDataNascimento(rs.getDate("data_nascimento"));
//                funcionario.setDataAdmissao(rs.getDate("data_admissao"));
//            }
//            return funcionario;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            conexao.desconectar();
//        }
//    }

    public List<FuncionarioDTO> listarFuncionarios() {
        List<FuncionarioDTO> funcionarios = new ArrayList<>();
        Connection conn = conexao.conectar();
        String comandoSQL = "SELECT f.*, c.nome AS nome_cargo, e.nome AS nome_eta FROM Funcionario f " +
                "JOIN cargo c ON f.id_cargo = c.id " +
                "JOIN eta e ON f.id_eta = e.id " +
                "ORDER BY f.nome";

        try {
           Statement pstmt = conn.createStatement();
            ResultSet rs = pstmt.executeQuery(comandoSQL);


            while (rs.next()) {
                FuncionarioDTO func = new FuncionarioDTO();
                func.setId(rs.getInt("id"));
                func.setNome(rs.getString("nome"));
                func.setEmail(rs.getString("email"));
                func.setDataAdmissao(rs.getDate("data_admissao"));
                func.setDataNascimento(rs.getDate("data_nascimento"));
                func.setIdEta(rs.getInt("id_eta"));
                func.setNomeEta(rs.getString("nome_eta"));
                func.setIdCargo(rs.getInt("id_cargo"));
                func.setSenha(rs.getString("senha"));
                func.setNomeCargo(rs.getString("nome_cargo"));

                funcionarios.add(func);
            }
            return funcionarios;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>();
        } finally {
            conexao.desconectar();
        }
    }


    public List<FuncionarioDTO> buscarPorNome(String nome) {
        List<FuncionarioDTO> lista = new ArrayList<>();
        String sql = "SELECT f.*, e.nome AS nome_eta FROM funcionario f " +
                "JOIN eta e ON e.id = f.id_eta " +
                "WHERE LOWER(nome) = LOWER(?)";

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FuncionarioDTO dto = new FuncionarioDTO();
                dto.setId(rs.getInt("id"));
                dto.setNome(rs.getString("nome"));
                dto.setEmail(rs.getString("email"));
                dto.setDataAdmissao(rs.getDate("data_admissao"));
                dto.setDataNascimento(rs.getDate("data_nascimento"));
                dto.setSenha(rs.getString("senha"));
                dto.setIdEta(rs.getInt("id_eta")); //precisa do id????????
                dto.setNomeEta(rs.getString("nome_eta"));
                dto.setIdCargo(rs.getInt("id_cargo"));
                lista.add(dto);
            }
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}