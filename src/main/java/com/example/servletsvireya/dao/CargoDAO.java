package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.CargoDTO;
import com.example.servletsvireya.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {

    Conexao conexao = new Conexao();


    // ========== Método para inserir um cargo ========== //
    public int inserirCargo(CargoDTO cargo) {
        Connection conn;
        conn = conexao.conectar(); //Criando conexão com o banco

        String comando;
        comando = "INSERT INTO cargo (nome, acesso, id_eta) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            //Settando nos parâmetros do comando
            pstmt.setString(1, cargo.getNome());
            pstmt.setInt(2, cargo.getAcesso());
            pstmt.setInt(3, cargo.getIdEta()); // Novo campo

            int resultado;
            resultado = pstmt.executeUpdate();

            if (resultado > 0) {
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
        Connection conn;
        conn = conexao.conectar();

        String comando;
        comando = "DELETE FROM Cargo WHERE id = ?";

        String comando2;
        comando2 = "UPDATE FUNCIONARIO SET ID_CARGO=NULL WHERE id_cargo = ?";


        try (PreparedStatement pstmt = conn.prepareStatement(comando);
             PreparedStatement pstmt2 = conn.prepareStatement(comando2)) {

            pstmt.setInt(1, cargo.getId());
            pstmt2.setInt(1, cargo.getId());

            pstmt2.executeUpdate();

            int resultado;
            resultado = pstmt.executeUpdate();

            return (resultado > 0) ? 1 : 0; //Forma simplificada para retornar

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para buscar um cargo pelo ID ========== //
    public CargoDTO buscarPorId(CargoDTO cargo) {
        Connection conn;
        conn = conexao.conectar();

        String comando;
        comando = "SELECT * FROM cargo WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, cargo.getId());

            ResultSet rs;
            rs = pstmt.executeQuery(); //Armazena os dados da query

            if (rs.next()) { //Se houver registro
                //Setta no mesmo objeto vindo como parâmetro
                int id;
                id = rs.getInt("id");
                cargo.setId(id);

                String nome;
                nome = rs.getString("nome");
                cargo.setNome(nome);

                int acesso;
                acesso = rs.getInt("acesso");
                cargo.setAcesso(acesso);

                int idEta;
                idEta = rs.getInt("id_eta");
                cargo.setIdEta(idEta); // Adiciona o id_eta também
            }
            return cargo;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para buscar o ID de cargo pelo nome ========== //
    public Integer buscarIdPorNome(String nomeCargo) {
        Connection conn;
        conn = conexao.conectar();

        Integer id;
        id = 0;

        String comando;
        comando = "SELECT id FROM cargo WHERE LOWER(nome) = LOWER(?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setString(1, nomeCargo);

            ResultSet rs;
            rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            id = 0;
        } finally{
            conexao.desconectar();
        }

        return id; //Retorna o id ou 0
    }


    // ========== Método para alterar um cargo ========== //
    public int alterarCargo(CargoDTO cargo) {
        Connection conn;
        conn = conexao.conectar();

        String comando;
        comando = "UPDATE cargo SET nome = ?, acesso = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setString(1, cargo.getNome());
            pstmt.setInt(2, cargo.getAcesso());
            pstmt.setInt(3, cargo.getId()); //WHERE id = ?

            int resultado;
            resultado = pstmt.executeUpdate();

            return (resultado > 0) ? 1 : 0;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        }
    }


    // ========== Método para listar os cargos ========== //
    public List<CargoDTO> listarCargos() {
        Connection conn;
        conn = conexao.conectar();

        List<CargoDTO> cargos;
        cargos = new ArrayList<>();

        //Prepara o comando SQL com JOIN
        String comando;
        comando = "SELECT c.*, e.nome AS nome_eta FROM cargo c " +
                "JOIN eta e ON e.id = c.id_eta " +
                "ORDER BY c.acesso DESC";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)){
            ResultSet rs;
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CargoDTO cargo;
                cargo = new CargoDTO(); //Cria um objeto a cada repetição

                int id;
                id = rs.getInt("id");
                cargo.setId(id);

                String nome;
                nome = rs.getString("nome");
                cargo.setNome(nome);

                int acesso;
                acesso = rs.getInt("acesso");
                cargo.setAcesso(acesso);

                int idEta;
                idEta = rs.getInt("id_eta");
                cargo.setIdEta(idEta);

                String nomeEta;
                nomeEta = rs.getString("nome_eta");
                cargo.setNomeEta(nomeEta);

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
        int novapesquisa;
        novapesquisa = 0;

        String tabela;
        tabela = null;

        String operador;
        operador = "LIKE"; // Por padrão

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

        Connection conn;
        conn = conexao.conectar(); //Criando conexão com o banco

        List<CargoDTO> lista;
        lista = new ArrayList<>();

        String comando;
        comando = "SELECT CARGO.*, ETA.NOME AS nome_eta " +
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

            ResultSet rs;
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CargoDTO cargo;
                cargo = new CargoDTO();

                int id;
                id = rs.getInt("id");
                cargo.setId(id);

                String nome;
                nome = rs.getString("nome");
                cargo.setNome(nome);

                int acesso;
                acesso = rs.getInt("acesso");
                cargo.setAcesso(acesso);

                int idEta;
                idEta = rs.getInt("id_eta");
                cargo.setIdEta(idEta);

                String nomeEta;
                nomeEta = rs.getString("nome_eta");
                cargo.setNomeEta(nomeEta); //Campo extra do DTO

                lista.add(cargo);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>(); //List vazio
        }

        return lista; //Retorna a list contendo os resultados ou vazia
    }
}