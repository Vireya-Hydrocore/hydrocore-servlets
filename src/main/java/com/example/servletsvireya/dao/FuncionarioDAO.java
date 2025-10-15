package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.FuncionarioDTO;
import com.example.servletsvireya.model.Funcionario;
import com.example.servletsvireya.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    private Conexao conexao = new Conexao(); //Para os métodos de conectar() e desconectar()


    // ========== Método para inserir um funcionário ========== //
    public int inserirFuncionario(FuncionarioDTO funcionarioDTO) {
        Connection conn = conexao.conectar(); //Cria conexão com o banco
        //Preparando instrução SQL para inserção do novo funcionário
        String comando = "INSERT INTO funcionario (nome, email, senha, data_admissao, data_nascimento, id_cargo, id_eta) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            //Settando os valores no comando
            pstmt.setString(1, funcionarioDTO.getNome());
            pstmt.setString(2, funcionarioDTO.getEmail());
            pstmt.setString(3, funcionarioDTO.getSenha());
            pstmt.setDate(4, (Date) (funcionarioDTO.getDataAdmissao()));
            pstmt.setDate(5, (Date) (funcionarioDTO.getDataNascimento()));
            pstmt.setInt(6, funcionarioDTO.getIdCargo());
            pstmt.setInt(7, funcionarioDTO.getIdEta());

            if (pstmt.executeUpdate() > 0){ //Se adicionar alguma linha
                return 1; //Deu certo
            } else {
                return 0; //Deu erro
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace(); //Mostra no terminal
            return -1; //Caiu em exceção
        } finally {
            conexao.desconectar(); //Encerra a conexão, mesmo se cair em exceção
        }
    }


    // ========== Método para listar os funcionários ========== //
//    public List<FuncionarioDTO> listarFuncionariosPorEta(int idEta) {
//        List<FuncionarioDTO> funcionarios = new ArrayList<>();
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
//                funcionarios.add(func);
//            }
//            return funcionarios;
//
//        } catch (SQLException sqle) {
//            sqle.printStackTrace();
//            return new ArrayList<>();
//        } finally {
//            conexao.desconectar();
//        }
//    }


    // ========== Método para alterar um funcionário ========== //
    public int alterarFuncionario(FuncionarioDTO funcionario) {
        Connection conn = conexao.conectar();
        //Preparando os comandos SQL
        String comandoFuncionario = " UPDATE funcionario SET nome = ?, email = ?, data_admissao = ?, data_nascimento = ?, id_cargo = ?, senha= ? WHERE id = ?";
        String comandoTarefa = "DELETE FROM tarefa WHERE id_funcionario= ?"; //Para deletar qualquer tarefa relacionada

        try(PreparedStatement pstmtFuncionario = conn.prepareStatement(comandoFuncionario);
            PreparedStatement pstmtTarefa = conn.prepareStatement(comandoTarefa)) {
            pstmtTarefa.setInt(1, funcionario.getId());
            pstmtTarefa.executeUpdate();

            pstmtFuncionario.setString(1, funcionario.getNome());
            pstmtFuncionario.setString(2, funcionario.getEmail());
            pstmtFuncionario.setDate(3, new java.sql.Date(funcionario.getDataAdmissao().getTime()));
            pstmtFuncionario.setDate(4, new java.sql.Date(funcionario.getDataNascimento().getTime()));
            pstmtFuncionario.setInt(5, funcionario.getIdCargo());
            pstmtFuncionario.setString(6, funcionario.getSenha());
            pstmtFuncionario.setInt(7, funcionario.getId()); // WHERE id = ?

            return (pstmtFuncionario.executeUpdate() > 0) ? 1 : 0;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para remover um funcionário ========== //
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


    // ========== Método para remover funcionários duplicados ========== //
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


    // ========== Método para buscar um funcionário pelo id ========== //
    public FuncionarioDTO buscarPorId(FuncionarioDTO funcionarioDTO) {
        Connection conn = conexao.conectar();
        ResultSet rset = null; //Para consulta
        String comando = "SELECT f.*, c.nome AS nome_cargo, e.nome AS nome_eta FROM funcionario f " +
                "JOIN cargo c ON c.id = f.id_cargo " +
                "JOIN eta e ON e.id = f.id_eta " +
                "WHERE f.id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, funcionarioDTO.getId()); //WHERE id = ?
            rset = pstmt.executeQuery(); //Executa e armazena a query

            if (rset == null) return null;

            if (rset.next()) {
                funcionarioDTO.setNome(rset.getString("nome"));
                funcionarioDTO.setEmail(rset.getString("email"));
                funcionarioDTO.setSenha(rset.getString("senha"));
                funcionarioDTO.setDataAdmissao(rset.getDate("data_admissao"));
                funcionarioDTO.setDataNascimento(rset.getDate("data_nascimento"));
                funcionarioDTO.setIdEta(rset.getInt("id_eta"));
                funcionarioDTO.setNomeCargo(rset.getString("nome_cargo")); // campo extra do DTO
                funcionarioDTO.setIdCargo(rset.getInt("id_cargo"));
                funcionarioDTO.setNomeEta(rset.getString("nome_eta")); // campo extra do DTO
            }
            return funcionarioDTO; // Retorna com os dados preenchidos no mesmo objeto obtido como parâmetro

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null; //Vazio se nao achou
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


    // ========== Método para listar os funcionários ========== //
    public List<FuncionarioDTO> listarFuncionarios() {
        List<FuncionarioDTO> funcionarios = new ArrayList<>(); //List para armazenar os dados
        Connection conn = conexao.conectar();
        //Prepara a consulta com JOINS para pegar cargo e nome da ETA
        String comando = "SELECT f.*, c.nome AS nome_cargo, e.nome AS nome_eta FROM Funcionario f " +
                "JOIN cargo c ON f.id_cargo = c.id " +
                "JOIN eta e ON f.id_eta = e.id " +
                "ORDER BY f.nome"; //Ordena por ordem alfabética

        try (PreparedStatement pstmt = conn.prepareStatement(comando)){
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                FuncionarioDTO func = new FuncionarioDTO(); //Cria um objeto a cada repetição
                func.setId(rs.getInt("id"));
                func.setNome(rs.getString("nome"));
                func.setEmail(rs.getString("email"));
                func.setSenha(rs.getString("senha"));
                func.setDataAdmissao(rs.getDate("data_admissao"));
                func.setDataNascimento(rs.getDate("data_nascimento"));
                func.setIdEta(rs.getInt("id_eta"));
                func.setNomeEta(rs.getString("nome_eta")); //Campo extra do DTO
                func.setIdCargo(rs.getInt("id_cargo"));
                func.setNomeCargo(rs.getString("nome_cargo")); //Campo extra do DTO

                funcionarios.add(func); //Populando o List
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>();
        } finally {
            conexao.desconectar();
        }

        return funcionarios; //Retorna os funcionários ou vazio se não achar
    }


    // ========== Método para buscar um funcionário pelo nome ========== //
//    public List<FuncionarioDTO> buscarPorNome(String nome) {
//        List<FuncionarioDTO> lista = new ArrayList<>();
//        String sql = "SELECT f.*, e.nome AS nome_eta FROM funcionario f " +
//                "JOIN eta e ON e.id = f.id_eta " +
//                "WHERE LOWER(nome) = LOWER(?)";
//
//        try (Connection conn = conexao.conectar();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setString(1, nome);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                FuncionarioDTO dto = new FuncionarioDTO();
//                dto.setId(rs.getInt("id"));
//                dto.setNome(rs.getString("nome"));
//                dto.setEmail(rs.getString("email"));
//                dto.setDataAdmissao(rs.getDate("data_admissao"));
//                dto.setDataNascimento(rs.getDate("data_nascimento"));
//                dto.setSenha(rs.getString("senha"));
//                dto.setIdEta(rs.getInt("id_eta")); //precisa do id????????
//                dto.setNomeEta(rs.getString("nome_eta"));
//                dto.setIdCargo(rs.getInt("id_cargo"));
//                lista.add(dto);
//            }
//            return lista;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return new ArrayList<>();
//        }
//    }


    // ========== Método para filtrar funcionário(s) ========== //
    public List<FuncionarioDTO> filtroBuscaPorColuna(String coluna, String pesquisa) {
        String tabela;

        if (coluna.equalsIgnoreCase("nome_eta")) {
            tabela = "ETA";
            coluna = "NOME";
        } else if (coluna.equalsIgnoreCase("nome_cargo")) {
            tabela = "CARGO";
            coluna = "NOME";
        } else {
            tabela = "FUNCIONARIO";
        }

        Connection conn = conexao.conectar();
        List<FuncionarioDTO> lista = new ArrayList<>();
        String comando =
                "SELECT FUNCIONARIO.*, ETA.nome AS nome_eta, CARGO.nome AS nome_cargo " +
                        "FROM FUNCIONARIO " +
                        "JOIN ETA ON ETA.id = FUNCIONARIO.id_eta " +
                        "JOIN CARGO ON CARGO.id = FUNCIONARIO.id_cargo " +
                        "WHERE " + tabela + "." + coluna + " LIKE LOWER(?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setString(1, "%" + pesquisa + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                FuncionarioDTO func = new FuncionarioDTO();
                func.setId(rs.getInt("id"));
                func.setNome(rs.getString("nome"));
                func.setEmail(rs.getString("email"));
                func.setSenha(rs.getString("senha"));
                func.setDataAdmissao(rs.getDate("data_admissao"));
                func.setDataNascimento(rs.getDate("data_nascimento"));
                func.setIdCargo(rs.getInt("id_cargo"));
                func.setNomeCargo(rs.getString("nome_cargo")); //Campo extra
                func.setIdEta(rs.getInt("id_eta"));
                func.setNomeEta(rs.getString("nome_eta")); //Campo extra
                lista.add(func);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); //Vazio
        } finally {
            conexao.desconectar();
        }

        return lista; //Retorna com os funcionários ou vazio
    }
}