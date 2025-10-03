package com.example.servletsvireya.dao;

import com.example.servletsvireya.model.Estoque;
import com.example.servletsvireya.util.Conexao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDAO { //erik
    private final Conexao conexao = new Conexao(); //Para os métodos de conectar e desconectar


    //Método para inserir um produto NO ESTOQUE - Precisa verificar se o id do produto existe ?????????????
    public int inserirEstoque(Estoque estoque) {
        Connection conn = conexao.conectar(); //Conecta ao banco de dados
        //Preparando String do comandoSQL
        String comandoSQL = "INSERT INTO estoque(quantidade, data_validade, " +
                "min_possiv_estocado, idEta, idProduto) VALUES(?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {
            //Settando valores usando a classe model
            pstmt.setInt(1, estoque.getQuantidade());
            pstmt.setDate(2, Date.valueOf(estoque.getDataValidade()));
            pstmt.setInt(3, estoque.getMinPossivEstocado());
            pstmt.setInt(4, estoque.getIdEta());
            pstmt.setInt(5, estoque.getIdProduto());

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
    public int removerEstoque(Estoque estoque) {
        Connection conn = conexao.conectar();
        String comandoSQL = "DELETE FROM estoque WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {
            pstmt.setInt(1, estoque.getId());

//            Verificando se o estoque existe
            if (buscarPorId(estoque.getId()) == null){
                return 0; //Se não tiver, retorna 0
            }

            //Verificar se o id_produto existe
//            PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM estoque WHERE id_produto = ?");
//            pstmt.setInt(1, estoque.getId());
//            ResultSet rset = pstmt.executeQuery();
//            rset.next();
//
//            if (rset.getInt(1) == 0) {
//                rset.close(); //Fecha o rset
//                return 0; //Não existe
//            }
//            rset.close(); //Fecha o rset se o ID existir

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



    //Método para alterar o estoque
    public int alterarEstoque(Estoque original, Estoque modificado) {
        List<Object> inputs = new ArrayList<>(); //Instanciando um List dos campos que talvez foram alterados

        String comandoSQL = "UPDATE estoque SET ";

        //Pegando os valores do model do talvez modificado
        int id = modificado.getId();
        int quantidade = modificado.getQuantidade();
        LocalDate dataValidade = modificado.getDataValidade();
        int minPossivEstocado = modificado.getMinPossivEstocado();
        int idEta = modificado.getIdEta();
        int idProduto = modificado.getIdProduto();

        //Checando se foi modificado ou não, em relação ao original
        if (quantidade != original.getQuantidade()) {
            inputs.add(quantidade); //Adiciona o valor que foi modificado
            comandoSQL += "quantidade = ?, "; //Concatena a string do comandoSQL
        }
        if (!dataValidade.equals(original.getDataValidade())) { //Não pode ser elseif
            inputs.add(dataValidade);
            comandoSQL += "data_validade = ?, ";
        }
        if (minPossivEstocado != original.getMinPossivEstocado()) {
            inputs.add(minPossivEstocado);
            comandoSQL += "min_possiv_estocado = ?, ";
        }
        if (idEta != original.getIdEta()) {
            inputs.add(idEta);
            comandoSQL += "id_eta = ?, ";
        }
        if (idProduto != original.getIdProduto()) {
            inputs.add(idProduto);
            comandoSQL += "id_produto = ?, ";
        }

        comandoSQL = comandoSQL.substring(0, comandoSQL.length() - 2); //Remove a ultima virgula

        comandoSQL += " WHERE id = ?";
        inputs.add(id);

        if (inputs.size() <= 1) { //Não mudou nada
            return 0;
        }

        Connection conn = conexao.conectar(); //Criando conexão com o banco
        try (PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {

            //Settando valores
            for (int i = 0; i < inputs.size(); i++) {
                pstmt.setObject(i + 1, inputs.get(i)); //Object é o mais geral
            }

            if (pstmt.executeUpdate() > 0) {
                return 1; //Se modificou alguma linha
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


    //Método para listar os produtos NO ESTOQUE
    public List<Estoque> listarEstoque() {
        ResultSet rset = null; //Consulta da tabela
        List<Estoque> estoques = new ArrayList<>(); //Instanciando uma lista de estoques
        Connection conn = conexao.conectar();

        //Prepara a consulta SQL para selecionar os produtos
        String comando = "SELECT * FROM estoque";
        try (PreparedStatement pstmt = conn.prepareStatement(comando)){
            rset = pstmt.executeQuery(); //Executa a consulta com Query

            //Armazenar os valores em um List<>
            while (rset.next()) {
                Estoque estoque = new Estoque(); //Instanciando um objeto Estoque

                estoque.setId(rset.getInt("id")); //Pega a primeira coluna do select (pode ser pelo index ou nome)
                estoque.setQuantidade(rset.getInt("quantidade"));
                estoque.setDataValidade(rset.getDate("data_validade").toLocalDate()); //Converte para LocalDate
                estoque.setMinPossivEstocado(rset.getInt("min_possiv_estocado"));
                estoque.setIdEta(rset.getInt("id_eta"));
                estoque.setIdProduto(rset.getInt("id_produto"));

                estoques.add(estoque); //Populando o List
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
    public Estoque buscarPorId(int idProcurado) { //Seria um filtro né???
        ResultSet rset = null; //Consulta da tabela
        Connection conn = conexao.conectar();
        String comando = "SELECT * FROM estoque WHERE id = ?";

        try(PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, idProcurado);
            rset = pstmt.executeQuery(); //Executa a consulta com Query

            if (rset == null) {
                return null; //Não tem registro com esse id
            }

            //Armazenar os valores em um objeto tipo estoque
            //somente objeto pois nao existe id repetido
            Estoque estoque = new Estoque();

            if (rset.next()) {
                estoque.setId(rset.getInt("id")); //Pega a primeira coluna do select (pode ser pelo index ou nome)
                estoque.setQuantidade(rset.getInt("quantidade"));
                estoque.setDataValidade(rset.getDate("data_validade").toLocalDate()); //Converte para LocalDate
                estoque.setMinPossivEstocado(rset.getInt("min_possiv_estocado"));
                estoque.setIdEta(rset.getInt("id_eta"));
                estoque.setIdProduto(rset.getInt("id_produto"));
            }
            return estoque; //Retorna contendo os produtos NO estoque

        } catch (SQLException e) {
            e.printStackTrace();
            return null; //Vazio
        } finally {
            conexao.desconectar();
        }
    }
}