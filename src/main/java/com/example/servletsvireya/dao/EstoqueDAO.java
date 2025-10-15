package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.EstoqueDTO;
import com.example.servletsvireya.dto.EtaDTO;
import com.example.servletsvireya.util.Conexao;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDAO { //erik

    private final Conexao conexao = new Conexao(); //Para os métodos de conectar e desconectar


    // ========== Método para inserir um produto NO ESTOQUE ========== //
    public int inserirEstoque(EstoqueDTO estoqueDTO) {
        Connection conn = conexao.conectar(); //Conecta ao banco de dados
        //Preparando String do comandoSQL
        String comando = "INSERT INTO estoque(quantidade, data_validade, " +
                "min_possivel_estocado, id_eta, id_produto) VALUES(?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            //Settando valores usando a classe model
            pstmt.setInt(1, estoqueDTO.getQuantidade());
            pstmt.setDate(2, new java.sql.Date (estoqueDTO.getDataValidade().getTime()));
            pstmt.setInt(3, estoqueDTO.getMinPossivelEstocado());
            pstmt.setInt(4, estoqueDTO.getIdEta());
            pstmt.setInt(5, estoqueDTO.getIdProduto());

            if (pstmt.executeUpdate() > 0) { //Se modificar alguma linha
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
        Connection conn = conexao.conectar();
        String comando = "DELETE FROM estoque WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, estoqueDTO.getId());

            if (pstmt.executeUpdate() > 0) {
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
        Connection conn = conexao.conectar();
        String comando = "UPDATE estoque SET quantidade = ?, data_validade = ?, min_possivel_estocado = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, estoqueDTO.getQuantidade());
            pstmt.setDate(2, new java.sql.Date (estoqueDTO.getDataValidade().getTime()));
            pstmt.setInt(3, estoqueDTO.getMinPossivelEstocado());
            pstmt.setInt(4, estoqueDTO.getId());

            if (pstmt.executeUpdate() > 0) {
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
        ResultSet rset = null; //Consulta da tabela
        List<EstoqueDTO> estoques = new ArrayList<>(); //Instanciando uma lista de estoqueDTOs
        Connection conn = conexao.conectar();
        //Prepara a consulta SQL para selecionar os produtos
        String comando = "SELECT e.*, p.nome AS nome_produto, et.nome AS nome_eta FROM estoque e " +
                "JOIN produto p ON p.id = e.id_produto " +
                "JOIN eta et ON et.id= e.id_eta " +
                "ORDER BY nome_produto";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            rset = pstmt.executeQuery(); //Executa a query

            //Armazenar os valores em um List<>
            while (rset.next()) {
                EstoqueDTO estoqueDTO = new EstoqueDTO(); //Instanciando um objeto estoqueDTO a cada repetição

                estoqueDTO.setId(rset.getInt("id")); //Pega por coluna do ResultSet
                estoqueDTO.setQuantidade(rset.getInt("quantidade"));
                estoqueDTO.setDataValidade(rset.getDate("data_validade"));
                estoqueDTO.setMinPossivelEstocado(rset.getInt("min_possivel_estocado"));
                estoqueDTO.setIdEta(rset.getInt("id_eta"));
                estoqueDTO.setIdProduto(rset.getInt("id_produto"));
                estoqueDTO.setNomeProduto(rset.getString("nome_produto"));
                estoqueDTO.setNomeEta(rset.getString("nome_eta"));

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
        ResultSet rset = null; //Consulta da tabela
        Connection conn = conexao.conectar();
        String comando = "SELECT * FROM estoque WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, estoqueDTO.getId());
            rset = pstmt.executeQuery(); //Executa a query

            //Armazenar os valores no mesmo objeto tipo estoqueDTO
            //Somente um objeto pois nao existe id repetido
            if (rset.next()) {
                estoqueDTO.setQuantidade(rset.getInt("quantidade"));
                estoqueDTO.setDataValidade(rset.getDate("data_validade"));
                estoqueDTO.setMinPossivelEstocado(rset.getInt("min_possivel_estocado"));
                estoqueDTO.setIdEta(rset.getInt("id_eta"));
                estoqueDTO.setIdProduto(rset.getInt("id_produto"));
            }
            return estoqueDTO; //Retorna contendo os produtos NO estoque

        } catch (SQLException e) {
            e.printStackTrace();
            return null; //Vazio
        } finally {
            conexao.desconectar();
        }
    }


//    public List<EstoqueDTO> listarEstoquePorEta(int id) {
//        List<EstoqueDTO> estoques = new ArrayList<>();
//        Connection conn = conexao.conectar();
//        String comando = "SELECT p.nome AS nome_produto, e.* FROM estoque e " +
//                "JOIN produto p ON p.id = e.id_produto " +
//                "WHERE e.id_eta = ?";
//
//        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
//            pstmt.setInt(1, id);
//
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//                EstoqueDTO e = new EstoqueDTO();
//
//                e.setNomeProduto(rs.getString("nome_produto"));
//                e.setId(rs.getInt("id"));
//                e.setQuantidade(rs.getInt("quantidade"));
//                e.setDataValidade(rs.getDate("data_validade"));
//                e.setMinPossivelEstocado(rs.getInt("min_possivel_estocado"));
//                e.setIdProduto(rs.getInt("id_produto"));
//                estoques.add(e);
//            }
//            return estoques;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return new ArrayList<>(); //Lista vazia
//        } finally {
//            conexao.desconectar();
//        }
//    }


//    public List<EstoqueDTO> buscarPorNome(String nomeProduto) {
//        List<EstoqueDTO> lista = new ArrayList<>();
//        String sql = "SELECT e.*, eta.nome AS nome_eta FROM estoque e " +
//                "JOIN eta ON eta.id = e.id_eta " +
//                "WHERE LOWER(nome) LIKE LOWER(?)";
//
//        try (Connection conn = conexao.conectar();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setString(1, "%" + nomeProduto + "%");
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                EstoqueDTO dto = new EstoqueDTO();
//                dto.setId(rs.getInt("id"));
//                dto.setNomeProduto(rs.getString("nome"));
//                dto.setQuantidade(rs.getInt("quantidade"));
//                dto.setDataValidade(rs.getDate("validade"));
//                dto.setMinPossivelEstocado(rs.getInt("min_possivel_estocado"));
//                dto.setIdProduto(rs.getInt("id_produto"));
//                dto.setNomeEta(rs.getString("nome_eta"));
//                lista.add(dto);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return lista;
//    }


    // ========== Método para filtrar um estoque ========== //
    public List<EstoqueDTO> filtroBuscaPorColuna(String coluna, String pesquisa) {
        String tabela;
        String operador = "LIKE";
        boolean numero = false;
        boolean isData=false;
        LocalDate data= null;

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
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            data= LocalDate.parse(pesquisa, formato);
            tabela = "ESTOQUE";
            isData = true;
        } else {
            tabela = "ESTOQUE";
        }

        Connection conn = conexao.conectar();
        List<EstoqueDTO> lista = new ArrayList<>();
        //SQL com JOINs para trazer produto e ETA
        String comando =
                "SELECT ESTOQUE.*, PRODUTO.NOME AS nome_produto, ETA.NOME AS nome_eta " +
                        "FROM ESTOQUE " +
                        "JOIN PRODUTO ON PRODUTO.id = ESTOQUE.id_produto " +
                        "JOIN ETA ON ETA.id = ESTOQUE.id_eta " +
                        "WHERE " + (isData
                        ? "ESTOQUE." + coluna + " = ?"   // comparação direta com DATE
                        : tabela + "." + coluna + " ILIKE ?"); // comparação textual
        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            // Define o tipo de dado corretamente
            if (numero) {
                pstmt.setInt(1, Integer.parseInt(pesquisa));
            } else if (isData) {
                pstmt.setDate(1, java.sql.Date.valueOf(data));
            } else {
                pstmt.setString(1, "%" + pesquisa + "%");
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                EstoqueDTO etaDTO = new EstoqueDTO();
                etaDTO.setId(rs.getInt("id"));
                etaDTO.setIdProduto(rs.getInt("id_produto"));
                etaDTO.setNomeProduto(rs.getString("nome_produto"));
                etaDTO.setQuantidade(rs.getInt("quantidade"));
                etaDTO.setDataValidade(rs.getDate("data_validade"));
                etaDTO.setMinPossivelEstocado(rs.getInt("min_possivel_estocado"));
                etaDTO.setIdEta(rs.getInt("id_eta"));
                etaDTO.setNomeEta(rs.getString("nome_eta")); //Campo extra do etaDTO
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
