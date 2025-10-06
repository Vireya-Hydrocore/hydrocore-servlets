//package com.example.servletsvireya.dao;
//
//import com.example.servletsvireya.dto.FuncionarioDTO;
//import com.example.servletsvireya.model.Funcionario;
//import com.example.servletsvireya.util.Conexao;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class FuncionarioDAO {
//
//    private Conexao conexao = new Conexao();
//
//    // ✅ INSERIR FUNCIONÁRIO
//    public int inserirFuncionario(FuncionarioDTO Funcionario) {
//        Connection conn = conexao.conectar();
//        String comandoSQL = "INSERT INTO Funcionario (nome, email, data_admissao, data_nascimento,senha, id_eta, id_cargo) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {
//            pstmt.setString(1, Funcionario.getNome());
//            pstmt.setString(2, Funcionario.getEmail());
//            pstmt.setDate(3, new java.sql.Date(Funcionario.getDataAdmissao().getTime()));
//            pstmt.setDate(4, new java.sql.Date(Funcionario.getDataNascimento().getTime()));
//            pstmt.setInt(7, Funcionario.getIdCargo());
//            pstmt.setInt(6, Funcionario.getIdEta());
//            pstmt.setString(5, Funcionario.getSenha());
//
//            return (pstmt.executeUpdate() > 0) ? 1 : 0;
//
//        } catch (SQLException sqle) {
//            sqle.printStackTrace();
//            return -1;
//        } finally {
//            conexao.desconectar();
//        }
//    }
//
//    // ✅ LISTAR FUNCIONÁRIOS (com JOIN no cargo e eta)
//    public List<FuncionarioDTO> listarFuncionariosPorEta(int idEta) {
//        List<FuncionarioDTO> Funcionarios = new ArrayList<>();
//        Connection conn = conexao.conectar();
//
//        String comandoSQL = "SELECT f.*, c.nome AS nome_cargo FROM Funcionario f JOIN cargo c ON f.id_cargo = c.id WHERE f.id_eta = ? ORDER BY f.nome";
//
//        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {
//            pstmt.setInt(1, idEta);
//            ResultSet rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                FuncionarioDTO func = new FuncionarioDTO();
//                func.setId(rs.getInt("id"));
//                func.setNome(rs.getString("nome"));
//                func.setEmail(rs.getString("email"));
//                func.setDataAdmissao(rs.getDate("data_admissao"));
//                func.setDataNascimento(rs.getDate("data_nascimento"));
//                func.setIdEta(rs.getInt("id_eta"));
//                func.setIdCargo(rs.getInt("id_cargo"));
//                func.setSenha(rs.getString("senha"));
//                func.setNomeCargo(rs.getString("nome_cargo"));
//
//                Funcionarios.add(func);
//            }
//            return Funcionarios;
//
//        } catch (SQLException sqle) {
//            sqle.printStackTrace();
//            return new ArrayList<>();
//        } finally {
//            conexao.desconectar();
//        }
//    }
//
//    // ✅ ALTERAR FUNCIONÁRIO
//    public int alterarFuncionario(FuncionarioDTO funcionario) {
//        Connection conn = conexao.conectar();
//        String comandoSQL = " UPDATE funcionario SET nome = ?, email = ?, data_admissao = ?, data_nascimento = ?, id_cargo = ?, senha= ? WHERE id = ?";
//        String delete= "DELETE FROM tarefa WHERE id_funcionario= ?" ;
//        try(PreparedStatement pstmt2 = conn.prepareStatement(delete)) {
//            pstmt2.setInt(1, funcionario.getId());
//            pstmt2.executeUpdate();
//        } catch (SQLException e){
//            e.printStackTrace();
//        }
//
//        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {
//            pstmt.setString(1, funcionario.getNome());
//            pstmt.setString(2, funcionario.getEmail());
//            pstmt.setDate(3, new java.sql.Date(funcionario.getDataAdmissao().getTime()));
//            pstmt.setDate(4, new java.sql.Date(funcionario.getDataNascimento().getTime()));
//            pstmt.setInt(5, funcionario.getIdCargo());
//            pstmt.setString(6, funcionario.getSenha());
//            pstmt.setInt(7, funcionario.getId()); // WHERE id = ?
//
//            return (pstmt.executeUpdate() > 0) ? 1 : 0;
//
//        } catch (SQLException sqle) {
//            sqle.printStackTrace();
//            return -1;
//        } finally {
//            conexao.desconectar();
//        }
//    }
//
//    // ✅ REMOVER FUNCIONÁRIO
//    public int removerFuncionario(int idFuncionario) {
//        Connection conn = conexao.conectar();
//        String comandoSQL = "DELETE FROM funcionario WHERE id = ?";
//
//        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {
//            pstmt.setInt(1, idFuncionario);
//            return (pstmt.executeUpdate() > 0) ? 1 : 0;
//
//        } catch (SQLException sqle) {
//            sqle.printStackTrace();
//            return -1;
//        } finally {
//            conexao.desconectar();
//        }
//    }
//
//    // ✅ REMOVER DUPLICADOS
//    public int removerDuplicadas() {
//        Connection conn = conexao.conectar();
//        String comandoSQL = " DELETE FROM funcionario WHERE id NOT IN ( SELECT MIN(id) FROM funcionario GROUP BY nome, email, data_admissao, data_nascimento, id_eta, id_cargo";
//
//        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {
//            return (pstmt.executeUpdate() > 0) ? 1 : 0;
//
//        } catch (SQLException sqle) {
//            sqle.printStackTrace();
//            return -1;
//        } finally {
//            conexao.desconectar();
//        }
//    }
//
//    public FuncionarioDTO selecionarFuncionario(FuncionarioDTO funcionario) {
//        ResultSet rset = null;
//        Connection conn = conexao.conectar();
//
//        // SQL para buscar funcionário pelo ID
//        String comando = "SELECT f.*, c.nome AS nome_cargo " +
//                "FROM funcionario f " +
//                "JOIN cargo c ON f.id_cargo = c.id " +
//                "WHERE f.id = ?";
//
//        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
//            pstmt.setInt(1, funcionario.getId());
//            rset = pstmt.executeQuery();
//
//            if (rset.next()) {
//                funcionario.setNome(rset.getString("nome"));
//                funcionario.setEmail(rset.getString("email"));
//                funcionario.setDataAdmissao(rset.getDate("data_admissao"));
//                funcionario.setDataNascimento(rset.getDate("data_nascimento"));
//                funcionario.setIdEta(rset.getInt("id_eta"));
//                funcionario.setIdCargo(rset.getInt("id_cargo"));
//                funcionario.setNomeCargo(rset.getString("nome_cargo")); // campo extra do DTO
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            conexao.desconectar();
//        }
//
//        return funcionario; // retorna com os dados preenchidos ou vazio se não achou
//    }
//
//
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
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return funcionario;
//    }
//
//
//
//}
