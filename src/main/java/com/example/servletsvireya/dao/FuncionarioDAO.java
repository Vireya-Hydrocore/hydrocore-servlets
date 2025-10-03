package com.example.servletsvireya.dao;

import com.example.servletsvireya.model.Funcionario;
import com.example.servletsvireya.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    Conexao conexao;


    // Método inserir()
    public int inserirFuncionario(Funcionario funcionario) {
        Connection conn = conexao.conectar(); //Criando conexão com o banco
        //Preparando o comandoSQL
        String comandoSQL = "INSERT INTO funcionario (nome, email, data_admissao, data_nascimento, id_eta, id_cargo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {

            //Definindo os valores do comando
            pstmt.setString(2, funcionario.getNome());
            pstmt.setString(3, funcionario.getEmail());
            pstmt.setDate(4, Date.valueOf(funcionario.getDataAdmissao()));
            pstmt.setDate(5, Date.valueOf(funcionario.getDataNascimento()));
            pstmt.setInt(6, funcionario.getIdEta());
            pstmt.setInt(7, funcionario.getIdCargo());

            //Checando se alterou alguma linha
            if (pstmt.executeUpdate() > 0) {
                return 1; //Se sim, retorna 1
            } else {
                return 0;//Se não, retorna 0
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace(); //Mostra o erro
            return -1;
        } finally {
            conexao.desconectar(); //Desconecta do banco mesmo que passe por exceção
        }
    }


    // Método listarFuncionario()
    public List<Funcionario> listarFuncionario(Funcionario funcionario) {
        List<Funcionario> funcionarios = new ArrayList<>(); //Instanciando uma lista de funcionarios
        Connection conn = conexao.conectar();
        String comandoSQL = "SELECT f.*, c.nome AS Cargo, e.nome AS Eta FROM funcionario f WHERE id = ?" +
                "JOIN cargo c ON id = id_cargo" +
                "JOIN eta e ON id = id_eta";

        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {
            pstmt.setInt(1, funcionario.getId());  //Definindo o parâmetro do comando
            ResultSet rs = pstmt.executeQuery(); //Executa a consulta

            while (rs.next()) {
//                funcionario.setId(rs.getInt("id")); //nao precisa settar o id né?
                funcionario.setNome(rs.getString("nome")); //pode pegar pelo numero da coluna ou pelo nome
                funcionario.setEmail(rs.getString("email"));
                funcionario.setDataAdmissao(rs.getDate("data_admissao").toLocalDate()); // .toLocalDate - Pega a data no SQL em tranforma em um LocalDate em Java
                funcionario.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                funcionario.setIdEta(rs.getInt("id_eta"));
                funcionario.setIdCargo(rs.getInt("id_cargo"));

                funcionarios.add(funcionario);
            }
            return funcionarios;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>();
        } finally {
            conexao.desconectar();
        }
    }


    // Método alterarFuncionario
    public int alterarFuncionario(Funcionario funcionario) {
        Connection conn = conexao.conectar();
        String comandoSQL = "UPDATE funcionario SET nome = ?, email = ?, data_admissao = ?, " +
                "data_nascimento = ?, id_eta = ?, id_cargo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {
            pstmt.setString(1, funcionario.getNome());
            pstmt.setString(2, funcionario.getEmail());
            pstmt.setDate(3, Date.valueOf(funcionario.getDataAdmissao()));
            pstmt.setDate(4, Date.valueOf(funcionario.getDataNascimento()));
            pstmt.setInt(5, funcionario.getIdEta());
            pstmt.setInt(6, funcionario.getIdCargo());

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


    // Método remover()
    public int removerFuncionario(Funcionario funcionario) {
        Connection conn = conexao.conectar();
        String comandoSQL = "DELETE FROM funcionario WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {

            //Verificando se o funcionario existe
//            if (buscarFuncionarioPorId(funcionario.getId()) == null){
//                return 0;
//            }

            pstmt.setInt(1, funcionario.getId());

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

    // Método removerDuplicadas ()
    public int removerDuplicadas() {
        Connection conn = conexao.conectar();
        String comandoSQL = "DELETE FROM funcionario WHERE id NOT IN (SELECT MIN(id) " +
                "FROM ETA GROUP BY nome, email, data_admissao, data_nascimento, id_eta, id_cargo)";
        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {
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
}