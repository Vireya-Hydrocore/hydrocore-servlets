package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.ProdutoDTO;
import com.example.servletsvireya.model.Produto;
import com.example.servletsvireya.util.Conexao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private final Conexao conexao = new Conexao(); //Só para os método de conectar() e desconectar()

    //Método para cadastrar um produto no sistema
    public int cadastrarProduto(Produto produto) {
        Connection conn = conexao.conectar();
        PreparedStatement pstmtProduto = null;
        PreparedStatement pstmtEstoque = null;
        ResultSet generatedKeys = null;

        String comandoProduto = "INSERT INTO produto (nome, tipo, unidade_medida, concentracao) VALUES (?, ?, ?, ?)";
        String comandoEstoque = "INSERT INTO estoque (quantidade, data_validade, min_possivel_estocado, id_produtos, id_eta) VALUES (?, ?, ?, ?, ?)";

        try {
            // Desligar auto-commit para poder fazer transação
            conn.setAutoCommit(false);

            // 1️⃣ Inserir Produto
            pstmtProduto = conn.prepareStatement(comandoProduto, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmtProduto.setString(1, produto.getNome());
            pstmtProduto.setString(2, produto.getTipo());
            pstmtProduto.setString(3, produto.getUnidadeMedida());
            pstmtProduto.setDouble(4, produto.getConcentracao());

            int linhasAfetadas = pstmtProduto.executeUpdate();
            if (linhasAfetadas == 0) {
                conn.rollback();
                return 0;
            }

            // 2️⃣ Pegar o ID gerado do produto
            generatedKeys = pstmtProduto.getGeneratedKeys();
            int idProdutoGerado = -1;
            if (generatedKeys.next()) {
                idProdutoGerado = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                return 0;
            }

            // 3️⃣ Inserir no estoque com quantidade 0
            pstmtEstoque = conn.prepareStatement(comandoEstoque);
            pstmtEstoque.setInt(1, 0); // quantidade
            pstmtEstoque.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now().plusYears(5)));
            // 👆 data_validade fictícia futura (pode mudar conforme sua regra)
            pstmtEstoque.setInt(3, 0); // min_possivel_estocado
            pstmtEstoque.setInt(4, idProdutoGerado); // FK produto
            pstmtEstoque.setInt(5, 1); // id_eta fixo por enquanto (pode vir do formulário depois)

            pstmtEstoque.executeUpdate();

            // 4️⃣ Se tudo deu certo, confirmar a transação
            conn.commit();
            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback(); // desfaz tudo se deu erro
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return -1;
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (pstmtProduto != null) pstmtProduto.close();
                if (pstmtEstoque != null) pstmtEstoque.close();
                conn.setAutoCommit(true);
                conexao.desconectar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    //Método para remover um produto
    public int removerProduto(ProdutoDTO produtoDTO) {
        Connection conn = conexao.conectar(); //Cria conexão com o banco
        String comando = "DELETE FROM estoque WHERE id_produtos = ?"; //Tem que deletar o que tiver relacionado também
        String comando2 = "DELETE FROM produto WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando);
             PreparedStatement pstmt2 = conn.prepareStatement(comando2)) {
            pstmt.setInt(1, produtoDTO.getId()); //No primeiro comando
            pstmt.setInt(1, produtoDTO.getId()); //Segundo comando

            if (pstmt.executeUpdate() > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace(); //remover no final do projetoooooooo
            return -1; //Para indicar erro no banco de dados
        } finally {
            conexao.desconectar();
        }
    }

    //Métodos para alterar concentração ////////Pode mudar né?
//    public int alterarConcentracao(Produto produto) { //Altera a concentração (por ID)
//        try {
//            Connection conn = conexao.conectar();
//
//            PreparedStatement pstmt = conn.prepareStatement("UPDATE produto SET concentracao = ? WHERE id = ?");
//            pstmt.setDouble(1, produto.getConcentracao());
//            pstmt.setInt(2, produto.getId());
//
//            if (pstmt.executeUpdate() > 0) {
//                return 1;
//            } else {
//                return 0;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return -1;
//        } finally {
//            conexao.desconectar();
//        }
//    }

    //Altera unidade de medida do produto
    public int alterarProduto(ProdutoDTO produtoDTO) {
        Connection conn = conexao.conectar();
        String comando = "UPDATE produto SET nome = ?, tipo = ?, unidade_medida = ?, concentracao = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setString(1, produtoDTO.getNome());
            pstmt.setString(2, produtoDTO.getTipo());
            pstmt.setString(3, produtoDTO.getUnidadeMedida());
            pstmt.setDouble(4, produtoDTO.getConcentracao());
            pstmt.setInt(5, produtoDTO.getId());

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

    //Método para listar produtos
//    public List<Produto> listarProduto() {
//        ResultSet rset = null; //Consulta da tabela
//        List<Produto> produtos = new ArrayList<>();
//
//        Connection conn = conexao.conectar();
//        String comando = "SELECT * FROM produto ORDER BY nome"; //ordena em ordem alfabética
//        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
//            rset = pstmt.executeQuery(); //Executa a consulta com Query
//
//            //Armazenar os valores em um List<>
//            while (rset.next()) {
//                int id = rset.getInt(1); //Pega a primeira coluna do select
//                String nome = rset.getString(2);
//                String tipo = rset.getString(3);
//                String unidadeMedida = rset.getString(4);
//                double concentracao = rset.getDouble(5);
//
//                //Populando o List
//                produtos.add(new Produto(id, nome, tipo, unidadeMedida, concentracao));
//            }
//            return produtos; //Retorna o List com os resultados
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return new ArrayList<>(); //Retorna lista vazia
//        } finally {
//            conexao.desconectar();
//        }
//    }


    //Buscar pelo ID
    public ProdutoDTO buscarPorId(ProdutoDTO produtoDTO) {
        ResultSet rset = null; //Consulta da tabela
        Connection conn = conexao.conectar();
        //Prepara a consulta SQL para selecionar os produtos por ordem de ID
        String comando = "SELECT * FROM produto WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, produtoDTO.getId());
            rset = pstmt.executeQuery(); //Executa a consulta com Query

            //Armazenar os valores em uma variável tipo produto
            //variavel pois nao existe id repetido
            if (rset.next()) {
//                int id = rset.getInt("id"); //Pega a primeira coluna do select
                produtoDTO.setNome(rset.getString("nome"));
                produtoDTO.setTipo(rset.getString("tipo"));
                produtoDTO.setUnidadeMedida(rset.getString("unidade_medida"));
                produtoDTO.setConcentracao(rset.getDouble("concentracao"));
            }
            return produtoDTO; //se não encontrar, volta null

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        } finally {
            conexao.desconectar();
        }
    }

    public List<ProdutoDTO> listarProdutoPorEta(int idEta) {
        List<ProdutoDTO> produtos = new ArrayList<>();
        Connection conn = conexao.conectar();
        String sql = "SELECT DISTINCT p.id, p.nome, p.tipo, p.unidade_medida, p.concentracao " +
                "FROM produto p\n" +
                "JOIN estoque e ON e.id_produtos = p.id " +
                "WHERE e.id_eta = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEta);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ProdutoDTO dto = new ProdutoDTO();
                dto.setId(rs.getInt("id"));
                dto.setNome(rs.getString("nome"));
                dto.setTipo(rs.getString("tipo"));
                dto.setUnidadeMedida(rs.getString("unidade_medida"));
                dto.setConcentracao(rs.getDouble("concentracao"));
                produtos.add(dto);
            }
            return produtos; //Populando o list

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            conexao.desconectar();
        }
    }
}