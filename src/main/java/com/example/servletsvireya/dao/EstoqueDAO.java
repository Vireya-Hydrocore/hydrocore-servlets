package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.EstoqueDTO;
import com.example.servletsvireya.util.Conexao;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDAO {

    Conexao conexao = new Conexao(); //Para os métodos de conectar e desconectar


    // ========== Método para inserir um produto NO ESTOQUE ========== //
    public int inserirEstoque(EstoqueDTO estoqueDTO) {
        Connection conn;
        conn = conexao.conectar(); //Conecta ao banco de dados

        //Preparando String do comandoSQL
        String comando;
        comando = "INSERT INTO estoque(quantidade, data_validade, " +
                "min_possivel_estocado, id_eta, id_produto) VALUES(?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            //Settando valores usando a classe model
            pstmt.setInt(1, estoqueDTO.getQuantidade());

            Date sqlDate;
            sqlDate = new java.sql.Date(estoqueDTO.getDataValidade().getTime());
            pstmt.setDate(2, sqlDate);

            pstmt.setInt(3, estoqueDTO.getMinPossivelEstocado());
            pstmt.setInt(4, estoqueDTO.getIdEta());
            pstmt.setInt(5, estoqueDTO.getIdProduto());

            int resultado;
            resultado = pstmt.executeUpdate();

            if (resultado > 0) { //Se modificar alguma linha
                return 1; //Inserção bem sucedida
            } else {
                return 0; //Não foi possível inserir
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace(); //remover no final do projetoooooooo
            return -1; //Erro no banco de dados
        } finally {
            conexao.desconectar(); //Desconecta, mesmo após exceção
        }
    }


    // ========== Método para remover um estoque ========== //
    public int removerEstoque(EstoqueDTO estoqueDTO) {
        Connection conn;
        conn = conexao.conectar();

        String comando;
        comando = "DELETE FROM estoque WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, estoqueDTO.getId());

            int resultado;
            resultado = pstmt.executeUpdate();

            if (resultado > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace(); //remover no final do projetoooooooo
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para alterar um estoque ========== //
    public int alterarEstoque(EstoqueDTO estoqueDTO) {
        Connection conn;
        conn = conexao.conectar();

        String comando;
        comando = "UPDATE estoque SET quantidade = ?, data_validade = ?, min_possivel_estocado = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, estoqueDTO.getQuantidade());

            Date sqlDate;
            sqlDate = new java.sql.Date(estoqueDTO.getDataValidade().getTime());
            pstmt.setDate(2, sqlDate);

            pstmt.setInt(3, estoqueDTO.getMinPossivelEstocado());
            pstmt.setInt(4, estoqueDTO.getId());

            int resultado;
            resultado = pstmt.executeUpdate();

            if (resultado > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para listar os estoques de produtos ========== //
    public List<EstoqueDTO> listarEstoque() {
        ResultSet rset; //Consulta da tabela
        rset = null;

        List<EstoqueDTO> estoques;
        estoques = new ArrayList<>(); //Instanciando uma lista de estoqueDTOs

        Connection conn;
        conn = conexao.conectar();

        //Prepara a consulta SQL para selecionar os produtos
        String comando;
        comando = "SELECT e.*, p.nome AS nome_produto, et.nome AS nome_eta FROM estoque e " +
                "JOIN produto p ON p.id = e.id_produto " +
                "JOIN eta et ON et.id= e.id_eta " +
                "ORDER BY nome_produto";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            rset = pstmt.executeQuery(); //Executa a query

            //Armazenar os valores em um List<>
            while (rset.next()) {
                EstoqueDTO estoqueDTO;
                estoqueDTO = new EstoqueDTO(); //Instanciando um objeto estoqueDTO a cada repetição

                int id;
                id = rset.getInt("id");
                estoqueDTO.setId(id); //Pega por coluna do ResultSet

                int quantidade;
                quantidade = rset.getInt("quantidade");
                estoqueDTO.setQuantidade(quantidade);

                Date dataValidade;
                dataValidade = rset.getDate("data_validade");
                estoqueDTO.setDataValidade(dataValidade);

                int minPossivelEstocado;
                minPossivelEstocado = rset.getInt("min_possivel_estocado");
                estoqueDTO.setMinPossivelEstocado(minPossivelEstocado);

                int idEta;
                idEta = rset.getInt("id_eta");
                estoqueDTO.setIdEta(idEta);

                int idProduto;
                idProduto = rset.getInt("id_produto");
                estoqueDTO.setIdProduto(idProduto);

                String nomeProduto;
                nomeProduto = rset.getString("nome_produto");
                estoqueDTO.setNomeProduto(nomeProduto);

                String nomeEta;
                nomeEta = rset.getString("nome_eta");
                estoqueDTO.setNomeEta(nomeEta);

                estoques.add(estoqueDTO); //Populando o List
            }
            return estoques; //Retorna list contendo os produtos NO estoque

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>(); //Vazio
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para buscar um estoque por ID ========== //
    public EstoqueDTO buscarPorId(EstoqueDTO estoqueDTO) {
        ResultSet rset; //Consulta da tabela
        rset = null;

        Connection conn;
        conn = conexao.conectar();

        String comando;
        comando = "SELECT e.*, p.nome AS nome_produto FROM estoque e " +
                "JOIN produto p ON p.id = e.id_produto " +
                "WHERE e.id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, estoqueDTO.getId());
            rset = pstmt.executeQuery(); //Executa a query

            //Armazenar os valores no mesmo objeto tipo estoqueDTO
            //Somente um objeto pois nao existe id repetido
            if (rset.next()) {
                int quantidade;
                quantidade = rset.getInt("quantidade");
                estoqueDTO.setQuantidade(quantidade);

                Date dataValidade;
                dataValidade = rset.getDate("data_validade");
                estoqueDTO.setDataValidade(dataValidade);

                int minPossivelEstocado;
                minPossivelEstocado = rset.getInt("min_possivel_estocado");
                estoqueDTO.setMinPossivelEstocado(minPossivelEstocado);

                int idEta;
                idEta = rset.getInt("id_eta");
                estoqueDTO.setIdEta(idEta);

                int idProduto;
                idProduto = rset.getInt("id_produto");
                estoqueDTO.setIdProduto(idProduto);

                String nomeProduto;
                nomeProduto = rset.getString("nome_produto");
                estoqueDTO.setNomeProduto(nomeProduto); //Campo extra do DTO
            }
            return estoqueDTO; //Retorna contendo os produtos NO estoque

        } catch (SQLException e) {
            e.printStackTrace();
            return null; //Vazio
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para filtrar um estoque ========== //
    public List<EstoqueDTO> filtroBuscaPorColuna(String coluna, String pesquisa) {
        String tabela;
        tabela = null;

        String operador;
        operador = "LIKE";

        boolean numero;
        numero = false;

        boolean isData;
        isData=false;

        LocalDate data;
        data = null;

        //Define de qual tabela e tipo de dado vem a coluna
        if (coluna.equals("id") || coluna.equals("quantidade") || coluna.equals("min_possivel_estocado")) {
            tabela = "ESTOQUE";
            operador = "=";
            numero = true;
        } else if (coluna.equals("nome_produto")) {
            tabela = "PRODUTO";
            coluna = "NOME";
        } else if (coluna.equals("nome_eta")) {
            tabela = "ETA";
            coluna = "NOME";
        } else if (coluna.equals("data_validade"))  {
            DateTimeFormatter formato;
            formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            data = LocalDate.parse(pesquisa, formato);
            tabela = "ESTOQUE";
            isData = true;
        } else {
            tabela = "ESTOQUE";
        }

        Connection conn;
        conn = conexao.conectar();

        List<EstoqueDTO> lista;
        lista = new ArrayList<>();

        //SQL com JOINs para trazer produto e ETA
        String comando;
        comando = "SELECT ESTOQUE.*, PRODUTO.NOME AS nome_produto, ETA.NOME AS nome_eta " +
                "FROM ESTOQUE " +
                "JOIN PRODUTO ON PRODUTO.id = ESTOQUE.id_produto " +
                "JOIN ETA ON ETA.id = ESTOQUE.id_eta " +
                "WHERE " + (isData
                ? "ESTOQUE." + coluna + " = ?"   // comparação direta com DATE
                : tabela + "." + coluna + " ILIKE ?"); // comparação textual

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            // Define o tipo de dado corretamente
            if (numero) {
                int valorNumerico;
                valorNumerico = Integer.parseInt(pesquisa);
                pstmt.setInt(1, valorNumerico);
            } else if (isData) {
                Date sqlDate;
                sqlDate = java.sql.Date.valueOf(data);
                pstmt.setDate(1, sqlDate);
            } else {
                pstmt.setString(1, "%" + pesquisa + "%");
            }

            ResultSet rs;
            rs = pstmt.executeQuery();

            while (rs.next()) {
                EstoqueDTO etaDTO;
                etaDTO = new EstoqueDTO();

                int id;
                id = rs.getInt("id");
                etaDTO.setId(id);

                int idProduto;
                idProduto = rs.getInt("id_produto");
                etaDTO.setIdProduto(idProduto);

                String nomeProduto;
                nomeProduto = rs.getString("nome_produto");
                etaDTO.setNomeProduto(nomeProduto);

                int quantidade;
                quantidade = rs.getInt("quantidade");
                etaDTO.setQuantidade(quantidade);

                Date dataValidade;
                dataValidade = rs.getDate("data_validade");
                etaDTO.setDataValidade(dataValidade);

                int minPossivelEstocado;
                minPossivelEstocado = rs.getInt("min_possivel_estocado");
                etaDTO.setMinPossivelEstocado(minPossivelEstocado);

                int idEta;
                idEta = rs.getInt("id_eta");
                etaDTO.setIdEta(idEta);

                String nomeEta;
                nomeEta = rs.getString("nome_eta");
                etaDTO.setNomeEta(nomeEta); //Campo extra do etaDTO

                lista.add(etaDTO);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>();
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido para número");
        }
        return lista;
    }
}