package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.EstoqueDTO;
import com.example.servletsvireya.dto.EtaDTO;
import com.example.servletsvireya.util.Conexao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDAO { //erik

    private final Conexao conexao = new Conexao(); //Para os métodos de conectar e desconectar

    //Método para inserir um produto NO estoque - Precisa verificar se o id do produto existe ?????????????
    public int inserirEstoque(EstoqueDTO estoqueDTO) {
        Connection conn = conexao.conectar(); //Conecta ao banco de dados
        //Preparando String do comandoSQL
        String comandoSQL = "INSERT INTO estoque(quantidade, data_validade, " +
                "min_possivel_estocado, id_eta, id_produto) VALUES(?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {
            //Settando valores usando a classe model
            pstmt.setInt(1, estoqueDTO.getQuantidade());
            pstmt.setDate(2, (Date) (estoqueDTO.getDataValidade()));
            pstmt.setInt(3, estoqueDTO.getMinPossivelEstocado());
            pstmt.setInt(4, estoqueDTO.getIdEta());
            pstmt.setInt(5, estoqueDTO.getIdProduto());

            if (pstmt.executeUpdate() > 0) { //Se modificar alguma linha
                return 1; //Inserção bem sucedida
            } else {
                return 0; //Não foi possível inserir
            }
        } catch (SQLException e) {
            e.printStackTrace(); //remover no final do projetoooooooo
            return -1; //Erro no banco de dados
        } finally {
            conexao.desconectar(); //Desconecta, mesmo após exceção
        }
    }


    //Método para remover um produto
    public int removerEstoque(EstoqueDTO estoqueDTO) {
        Connection conn = conexao.conectar();
        String comandoSQL = "DELETE FROM estoque WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {
            pstmt.setInt(1, estoqueDTO.getId());

//            Verificando se o estoque existe
            if (buscarPorId(estoqueDTO) == null) {
                return 0; //Se não tiver, retorna 0
            }

            if (pstmt.executeUpdate() > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace(); //remover no final do projetoooooooo
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    // Método alterar (update)
    public int alterarEstoque(EstoqueDTO estoqueDTO) {
        Connection conn = conexao.conectar();
        String comando = "UPDATE estoque SET quantidade = ?, data_validade = ?, min_possivel_estocado = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, estoqueDTO.getQuantidade());
            pstmt.setDate(2, (Date) estoqueDTO.getDataValidade());
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


    //Método para listar os produtos NO estoqueDTO
    public List<EstoqueDTO> listarEstoque() {
        ResultSet rset = null; //Consulta da tabela
        List<EstoqueDTO> estoques = new ArrayList<>(); //Instanciando uma lista de estoqueDTOs
        Connection conn = conexao.conectar();

        //Prepara a consulta SQL para selecionar os produtos
        String comando = "SELECT e.*, p.nome AS nome_produto, et.nome AS nome_eta FROM estoque e " +
                "JOIN produto p ON p.id = e.id_produto " +
                "JOIN eta et ON et.id= e.id_eta";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            rset = pstmt.executeQuery(); //Executa a consulta com Query

            //Armazenar os valores em um List<>
            while (rset.next()) {
                EstoqueDTO estoqueDTO = new EstoqueDTO(); //Instanciando um objeto estoqueDTO

                estoqueDTO.setId(rset.getInt("id")); //Pega a primeira coluna do select (pode ser pelo index ou nome)
                estoqueDTO.setQuantidade(rset.getInt("quantidade"));
                estoqueDTO.setDataValidade(rset.getDate("data_validade")); //Converte para LocalDate
                estoqueDTO.setMinPossivelEstocado(rset.getInt("min_possivel_estocado"));
                estoqueDTO.setIdEta(rset.getInt("id_eta"));
                estoqueDTO.setIdProduto(rset.getInt("id_produto"));
                estoqueDTO.setNomeProduto(rset.getString("nome_produto"));
                estoqueDTO.setNomeEta(rset.getString("nome_eta"));

                estoques.add(estoqueDTO); //Populando o List
            }
            return estoques; //Retorna list contendo os produtos NO estoque

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); //Vazio
        } finally {
            conexao.desconectar();
        }
    }


    //Buscar pelo ID
    public EstoqueDTO buscarPorId(EstoqueDTO estoqueDTO) { //Seria um filtro né???
        ResultSet rset = null; //Consulta da tabela
        Connection conn = conexao.conectar();
        String comando = "SELECT * FROM estoque WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, estoqueDTO.getId());
            rset = pstmt.executeQuery(); //Executa a consulta com Query

            //Armazenar os valores em no mesmo objeto tipo estoqueDTO
            //somente objeto pois nao existe id repetido
            if (rset.next()) {
                estoqueDTO.setQuantidade(rset.getInt("quantidade")); //Pega a primeira coluna do select (pode ser pelo index ou nome)
                estoqueDTO.setDataValidade(rset.getDate("data_validade")); //Converte para LocalDate
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

    public List<EstoqueDTO> listarEstoquePorEta(int id) {
        List<EstoqueDTO> estoques = new ArrayList<>();
        Connection conn = conexao.conectar();
        String comando = "SELECT p.nome AS nome_produto, e.* FROM estoque e " +
                "JOIN produto p ON p.id = e.id_produto " +
                "WHERE e.id_eta = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                EstoqueDTO e = new EstoqueDTO();

                e.setNomeProduto(rs.getString("nome_produto"));
                e.setId(rs.getInt("id"));
                e.setQuantidade(rs.getInt("quantidade"));
                e.setDataValidade(rs.getDate("data_validade"));
                e.setMinPossivelEstocado(rs.getInt("min_possivel_estocado"));
                e.setIdProduto(rs.getInt("id_produto"));
                estoques.add(e);
            }
            return estoques;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); //Lista vazia
        } finally {
            conexao.desconectar();
        }
    }


    public List<EstoqueDTO> buscarPorNome(String nomeProduto) {
        List<EstoqueDTO> lista = new ArrayList<>();
        String sql = "SELECT e.*, eta.nome AS nome_eta FROM estoque e " +
                "JOIN eta ON eta.id = e.id_eta " +
                "WHERE LOWER(nome) LIKE LOWER(?)";

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nomeProduto + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                EstoqueDTO dto = new EstoqueDTO();
                dto.setId(rs.getInt("id"));
                dto.setNomeProduto(rs.getString("nome"));
                dto.setQuantidade(rs.getInt("quantidade"));
                dto.setDataValidade(rs.getDate("validade"));
                dto.setMinPossivelEstocado(rs.getInt("min_possivel_estocado"));
                dto.setIdProduto(rs.getInt("id_produto"));
                dto.setNomeEta(rs.getString("nome_eta"));
                lista.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<EstoqueDTO> filtroBuscaPorColuna(String coluna, String pesquisa) {
        String tabela;
        String operador = "LIKE";
        boolean numero = false;

        // Define de qual tabela e tipo de dado vem a coluna
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
        } else {
            tabela = "ESTOQUE";
        }

        // SQL com JOINs para trazer produto e ETA
        String sql =
                "SELECT ESTOQUE.*, PRODUTO.NOME AS nome_produto, ETA.NOME AS nome_eta " +
                        "FROM ESTOQUE " +
                        "JOIN PRODUTO ON PRODUTO.id = ESTOQUE.id_produto " +
                        "JOIN ETA ON ETA.id = ESTOQUE.id_eta " +
                        "WHERE " + tabela + "." + coluna + " " + operador + " ?";

        List<EstoqueDTO> lista = new ArrayList<>();

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o tipo de dado corretamente
            if (numero) {
                stmt.setInt(1, Integer.parseInt(pesquisa));
            } else {
                stmt.setString(1, "%" + pesquisa + "%");
            }
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                EstoqueDTO dto = new EstoqueDTO();
                dto.setId(rs.getInt("id"));
                dto.setIdProduto(rs.getInt("id_produto"));
                dto.setNomeProduto(rs.getString("nome_produto"));
                dto.setQuantidade(rs.getInt("quantidade"));
                dto.setDataValidade(rs.getDate("data_validade"));
                dto.setMinPossivelEstocado(rs.getInt("min_possivel_estocado"));
                dto.setIdEta(rs.getInt("id_eta"));
                dto.setNomeEta(rs.getString("nome_eta"));
                lista.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido para número");
        }

        return lista;
    }
}
