package com.example.servletsvireya.dao;

import com.example.servletsvireya.model.Estoque;
import com.example.servletsvireya.util.Conexao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDAO { //erik
    private final Conexao conexao = new Conexao(); //Para os métodos de conectar e desconectar

    //Método para inserir um produto NO ESTOQUE
    public int inserirEstoque(Estoque estoque) {
        Connection conn = conexao.conectar(); //Conecta ao banco de dados
        //Preparando String do comandoSQL
        String comando = "INSERT INTO estoque(quantidade, data_validade, " +
                "min_possiv_estocado, idEta, idProduto) VALUES(?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            //Setando valores usando a classe model
            pstmt.setInt(1, estoque.getQuantidade());
            pstmt.setDate(2, Date.valueOf(estoque.getData_validade()));
            pstmt.setInt(3, estoque.getMin_possiv_estocado());
            pstmt.setInt(4, estoque.getId_eta());
            pstmt.setInt(5, estoque.getId_produto());

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
        Connection conn = conexao.conectar(); //Cria conexão com o banco
        String comando = "DELETE FROM estoque WHERE id = ?"; //Prepara o ocmando SQL para deletar o produto

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, estoque.getId());

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
                return 1; //Deleção bem sucedida
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace(); //remover no final do projetoooooooo
            return -1; //Para indicar erro no banco de dados
        } finally {
            conexao.desconectar();
        }
    }


    //Método alterar estoque
    public int alterarEstoque(Estoque original, Estoque modificado) {
        List<Object> inputs = new ArrayList<>();

        //Criando uma String construtora
        StringBuilder comando = new StringBuilder("UPDATE estoque SET ");

        //Pegando os valores do model do talvez modificado
        int id = modificado.getId();
        int quantidade = modificado.getQuantidade();
        LocalDate dataValidade = modificado.getData_validade();
        int minPossivEstocado = modificado.getMin_possiv_estocado();
        int idEta = modificado.getId_eta();
        int idProduto = modificado.getId_produto();

        //Checando se foi modificado ou não, em relação ao original
        if (quantidade != original.getQuantidade()) {
            inputs.add(quantidade); //Adiciona o valor que foi modificado
            comando.append("quantidade = ?, "); //Concatena a string do comandoSQL
        }
        if (!dataValidade.equals(original.getData_validade())) { //Não pode ser elseif
            inputs.add(dataValidade);
            comando.append("data_validade = ?, ");
        }
        if (minPossivEstocado != original.getMin_possiv_estocado()) {
            inputs.add(minPossivEstocado);
            comando.append("min_possiv_estocado = ?, ");
        }
        if (idEta != original.getId_eta()) {
            inputs.add(idEta);
            comando.append("id_eta = ?, ");
        }
        if (idProduto != original.getId_produto()) {
            inputs.add(idProduto);
            comando.append("id_produto = ?, ");
        }

        comando.setLength(comando.length() - 2); //Remove a ultima virgula

        comando.append(" WHERE id = ?");
        inputs.add(id);

        if (inputs.size() <= 1) { //Não mudou nada
            return 0;
        }

        Connection conn = conexao.conectar(); //Criando conexão com o banco
        try (PreparedStatement pstmt = conn.prepareStatement(String.valueOf(comando))) {

            //Settando valores
            for (int i = 0; i < inputs.size(); i++) {
                pstmt.setObject(i + 1, inputs.get(i));
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


    //Método para buscar um produto NO ESTOQUE
    public List<Estoque> listarEstoque() {
        ResultSet rset = null; //Consulta da tabela
        List<Estoque> estoques = new ArrayList<>();

        Connection conn = conexao.conectar();
        //Prepara a consulta SQL para selecionar os produtos por ordem de ID
        String comando = "SELECT * FROM estoque ORDER BY id";
        try (PreparedStatement pstmt = conn.prepareStatement(comando)){
            rset = pstmt.executeQuery(); //Executa a consulta com Query

            //Armazenar os valores em um List<>
            while (rset.next()) {
                int id = rset.getInt(1); //Pega a primeira coluna do select
                int quantidade = rset.getInt(2);
                Date data_validade = rset.getDate(3); //??????
                int min_possiv_estocado = rset.getInt(4);
                int id_eta = rset.getInt(5);
                int id_produto = rset.getInt(6);

                //Populando o List
                estoques.add(new Estoque(id, quantidade, data_validade.toLocalDate(), min_possiv_estocado, id_eta, id_produto));
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
        Estoque estoque = null;

        Connection conn = conexao.conectar();
        //Prepara a consulta SQL para selecionar os produtos por ordem de ID
        String comando = "SELECT * FROM estoque WHERE id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, idProcurado);
            rset = pstmt.executeQuery(); //Executa a consulta com Query

            if (rset == null) {
                return null; //Não tem registro com esse id
            }

            //Armazenar os valores em uma variável tipo estoque
            //variavel pois nao existe id repetido
            if (rset.next()) {
                int id = rset.getInt(1); //Pega a primeira coluna do select
                int quantidade = rset.getInt(2);
                LocalDate data_validade = rset.getDate(3).toLocalDate(); //??????
                int min_possiv_estocado = rset.getInt(4);
                int id_eta = rset.getInt(5);
                int id_produto = rset.getInt(6);

                estoque = new Estoque(id, quantidade, data_validade, min_possiv_estocado, id_eta, id_produto);
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