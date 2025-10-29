package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.FuncionarioDTO;
import com.example.servletsvireya.util.Conexao;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {


    Conexao conexao = new Conexao(); //Para os métodos de conectar() e desconectar()


    // ========== Método para inserir um funcionário ========== //
    public int inserirFuncionario(FuncionarioDTO funcionarioDTO) {
        Connection conn;
        conn = conexao.conectar(); //Cria conexão com o banco

        //Preparando instrução SQL para inserção do novo funcionário
        String comando;
        comando = "INSERT INTO funcionario (nome, email, senha, data_admissao, data_nascimento, id_cargo, id_eta) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comando);
            //Settando os valores no comando
            pstmt.setString(1, funcionarioDTO.getNome());
            pstmt.setString(2, funcionarioDTO.getEmail());
            pstmt.setString(3, funcionarioDTO.getSenha());

            Date dataAdmissao;
            dataAdmissao = (Date) (funcionarioDTO.getDataAdmissao());
            pstmt.setDate(4, dataAdmissao);

            Date dataNascimento;
            dataNascimento = (Date) (funcionarioDTO.getDataNascimento());
            pstmt.setDate(5, dataNascimento);

            pstmt.setInt(6, funcionarioDTO.getIdCargo());
            pstmt.setInt(7, funcionarioDTO.getIdEta());

            int resultado;
            resultado = pstmt.executeUpdate();

            if (resultado > 0){ //Se adicionar alguma linha
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


    // ========== Método para alterar um funcionário ========== //
    public int alterarFuncionario(FuncionarioDTO funcionario) {
        Connection conn;
        conn = conexao.conectar();

        //Preparando os comandos SQL
        String comandoFuncionario;
        comandoFuncionario = " UPDATE funcionario SET nome = ?, email = ?, data_admissao = ?, data_nascimento = ?, id_cargo = ?, senha= ? WHERE id = ?";

        String comandoTarefa;
        comandoTarefa = "DELETE FROM tarefa WHERE id_funcionario= ?"; //Para deletar qualquer tarefa relacionada

        try {
            PreparedStatement pstmtFuncionario,pstmtTarefa;
            pstmtFuncionario = conn.prepareStatement(comandoFuncionario);
            pstmtTarefa = conn.prepareStatement(comandoTarefa);

            pstmtTarefa.setInt(1, funcionario.getId());
            pstmtTarefa.executeUpdate();

            pstmtFuncionario.setString(1, funcionario.getNome());
            pstmtFuncionario.setString(2, funcionario.getEmail());

            Date sqlDataAdmissao;
            sqlDataAdmissao = new java.sql.Date(funcionario.getDataAdmissao().getTime());
            pstmtFuncionario.setDate(3, sqlDataAdmissao);

            Date sqlDataNascimento;
            sqlDataNascimento = new java.sql.Date(funcionario.getDataNascimento().getTime());
            pstmtFuncionario.setDate(4, sqlDataNascimento);

            pstmtFuncionario.setInt(5, funcionario.getIdCargo());
            pstmtFuncionario.setString(6, funcionario.getSenha());
            pstmtFuncionario.setInt(7, funcionario.getId()); // WHERE id = ?

            int resultado;
            resultado = pstmtFuncionario.executeUpdate();

            return (resultado > 0) ? 1 : 0;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para remover um funcionário ========== //
    public int removerFuncionario(int idFuncionario) {
        Connection conn;
        conn = conexao.conectar();

        String comandoSQL2;
        comandoSQL2 = "DELETE FROM TAREFA WHERE ID_FUNCIONARIO= ?";

        String comandoSQL;
        comandoSQL = "DELETE FROM funcionario WHERE id= ?";

        try {
            PreparedStatement pstm2;
            pstm2 = conn.prepareStatement(comandoSQL2);

            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comandoSQL);

            pstm2.setInt(1, idFuncionario);
            pstmt.setInt(1, idFuncionario);
            pstm2.executeUpdate();

            int resultado;
            resultado = pstmt.executeUpdate();

            if (resultado > 0) {
                return 1;
            } else {
                return -1;
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para remover funcionários duplicados ========== //
    public int removerDuplicadas() {
        Connection conn;
        conn = conexao.conectar();

        String comandoSQL;
        comandoSQL = " DELETE FROM funcionario WHERE id NOT IN ( SELECT MIN(id) FROM funcionario GROUP BY nome, email, data_admissao, data_nascimento, id_eta, id_cargo";

        try {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comandoSQL);
            int resultado;
            resultado = pstmt.executeUpdate();

            return (resultado > 0) ? 1 : 0;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para buscar um funcionário pelo id ========== //
    public FuncionarioDTO buscarPorId(FuncionarioDTO funcionarioDTO) {
        Connection conn;
        conn = conexao.conectar();

        ResultSet rset; //Para consulta
        rset = null;

        String comando;
        comando = "SELECT f.*, c.nome AS nome_cargo, e.nome AS nome_eta FROM funcionario f " +
                "LEFT JOIN cargo c ON c.id = f.id_cargo " +
                "LEFT JOIN eta e ON e.id = f.id_eta " +
                "WHERE f.id = ?";

        try  {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comando);
            pstmt.setInt(1, funcionarioDTO.getId()); //WHERE id = ?
            rset = pstmt.executeQuery(); //Executa e armazena a query

            if (rset.next()) {
                String nome;
                nome = rset.getString("nome");
                funcionarioDTO.setNome(nome);

                String email;
                email = rset.getString("email");
                funcionarioDTO.setEmail(email);

                String senha;
                senha = rset.getString("senha");
                funcionarioDTO.setSenha(senha);

                Date dataAdmissao;
                dataAdmissao = rset.getDate("data_admissao");
                funcionarioDTO.setDataAdmissao(dataAdmissao);

                Date dataNascimento;
                dataNascimento = rset.getDate("data_nascimento");
                funcionarioDTO.setDataNascimento(dataNascimento);

                int idEta;
                idEta = rset.getInt("id_eta");
                funcionarioDTO.setIdEta(idEta);

                String nomeCargo;
                nomeCargo = rset.getString("nome_cargo");
                funcionarioDTO.setNomeCargo(nomeCargo); // campo extra do DTO

                int idCargo;
                idCargo = rset.getInt("id_cargo");
                funcionarioDTO.setIdCargo(idCargo);

                String nomeEta;
                nomeEta = rset.getString("nome_eta");
                funcionarioDTO.setNomeEta(nomeEta); // campo extra do DTO
            } else{
                return null;
            }

            return funcionarioDTO; // Retorna com os dados preenchidos no mesmo objeto obtido como parâmetro

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null; //Vazio se nao achou
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para listar os funcionários ========== //
    public List<FuncionarioDTO> listarFuncionarios() {
        List<FuncionarioDTO> funcionarios;
        funcionarios = new ArrayList<>(); //List para armazenar os dados

        Connection conn;
        conn = conexao.conectar();

        //Prepara a consulta com JOINS para pegar cargo e nome da ETA
        String comando;
        comando = "SELECT f.*, "+
                "c.nome AS nome_cargo, "+
                "e.nome AS nome_eta "+
                "FROM Funcionario f "+
                "LEFT JOIN cargo c ON f.id_cargo = c.id "+
                "LEFT JOIN eta e ON f.id_eta = e.id "
                +"ORDER BY f.nome "; //Ordena por ordem alfabética

        try (PreparedStatement pstmt = conn.prepareStatement(comando)){
            ResultSet rs;
            rs = pstmt.executeQuery();

            while (rs.next()) {
                FuncionarioDTO func;
                func = new FuncionarioDTO(); //Cria um objeto a cada repetição

                int id;
                id = rs.getInt("id");
                func.setId(id);

                String nome;
                nome = rs.getString("nome");
                func.setNome(nome);

                String email;
                email = rs.getString("email");
                func.setEmail(email);

                String senha;
                senha = rs.getString("senha");
                func.setSenha(senha);

                Date dataAdmissao;
                dataAdmissao = rs.getDate("data_admissao");
                func.setDataAdmissao(dataAdmissao);

                Date dataNascimento;
                dataNascimento = rs.getDate("data_nascimento");
                func.setDataNascimento(dataNascimento);

                int idEta;
                idEta = rs.getInt("id_eta");
                func.setIdEta(idEta);

                String nomeEta;
                nomeEta = rs.getString("nome_eta");
                func.setNomeEta(nomeEta); //Campo extra do DTO

                int idCargo;
                idCargo = rs.getInt("id_cargo");
                func.setIdCargo(idCargo);

                String nomeCargo;
                nomeCargo = rs.getString("nome_cargo");
                func.setNomeCargo(nomeCargo); //Campo extra do DTO

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


    // ========== Método para filtrar funcionário(s) ========== //
    public List<FuncionarioDTO> filtroBuscaPorColuna(String coluna, String pesquisa) {
        String tabela;
        tabela = null;

        boolean isData;
        isData = false;

        LocalDate data;
        data = null;

        if (coluna.equalsIgnoreCase("nome_eta")) {
            tabela = "ETA";
            coluna = "NOME";
        } else if (coluna.equalsIgnoreCase("data_nascimento") || coluna.equalsIgnoreCase("data_admissao")) {
            // O input type="date" envia no formato yyyy-MM-dd
            DateTimeFormatter formato;
            formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            data = LocalDate.parse(pesquisa, formato);
            tabela = "FUNCIONARIO";
            isData = true;
        } else if (coluna.equalsIgnoreCase("nome_cargo")) {
            tabela = "CARGO";
            coluna = "NOME";
        } else {
            tabela = "FUNCIONARIO";
        }

        Connection conn;
        conn = conexao.conectar();

        List<FuncionarioDTO> lista;
        lista = new ArrayList<>();

        // Aqui diferenciamos o comando SQL conforme o tipo de dado
        String comando;
        comando = "SELECT FUNCIONARIO.*, ETA.nome AS nome_eta, CARGO.nome AS nome_cargo " +
                "FROM FUNCIONARIO " +
                "LEFT JOIN ETA ON ETA.id = FUNCIONARIO.id_eta " +
                "LEFT JOIN CARGO ON CARGO.id = FUNCIONARIO.id_cargo " +
                "WHERE " + (isData
                ? "FUNCIONARIO." + coluna + " = ?"   // comparação direta com DATE
                : tabela + "." + coluna + " ILIKE ?"); // comparação textual

        try {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comando);
            ResultSet rs;

            if (isData) {
                Date sqlDate;
                sqlDate = java.sql.Date.valueOf(data); // Converte LocalDate para SQL Date
                pstmt.setDate(1, sqlDate);
            } else {
                String pesquisaFormatada;
                pesquisaFormatada = "%" + pesquisa + "%";
                pstmt.setString(1, pesquisaFormatada);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                FuncionarioDTO func;
                func = new FuncionarioDTO();

                int id;
                id = rs.getInt("id");
                func.setId(id);

                String nome;
                nome = rs.getString("nome");
                func.setNome(nome);

                String email;
                email = rs.getString("email");
                func.setEmail(email);

                String senha;
                senha = rs.getString("senha");
                func.setSenha(senha);

                Date dataAdmissao;
                dataAdmissao = rs.getDate("data_admissao");
                func.setDataAdmissao(dataAdmissao);

                Date dataNascimento;
                dataNascimento = rs.getDate("data_nascimento");
                func.setDataNascimento(dataNascimento);

                int idCargo;
                idCargo = rs.getInt("id_cargo");
                func.setIdCargo(idCargo);

                String nomeCargo;
                nomeCargo = rs.getString("nome_cargo");
                func.setNomeCargo(nomeCargo);

                int idEta;
                idEta = rs.getInt("id_eta");
                func.setIdEta(idEta);

                String nomeEta;
                nomeEta = rs.getString("nome_eta");
                func.setNomeEta(nomeEta);

                lista.add(func);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            conexao.desconectar();
        }

        return lista;
    }
}