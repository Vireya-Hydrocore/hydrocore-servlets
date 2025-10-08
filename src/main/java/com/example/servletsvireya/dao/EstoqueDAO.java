package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.EstoqueDTO;
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
            if (buscarPorId(estoqueDTO) == null){
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
        String comando = "SELECT * FROM estoque";
        try (PreparedStatement pstmt = conn.prepareStatement(comando)){
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

        try(PreparedStatement pstmt = conn.prepareStatement(comando)) {
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
}