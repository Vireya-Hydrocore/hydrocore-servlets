package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.EstoqueDTO;
import com.example.servletsvireya.dto.ProdutoDTO;
import com.example.servletsvireya.model.Produto;
import com.example.servletsvireya.util.Conexao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private final Conexao conexao = new Conexao(); //Para os métodos de conectar() e desconectar()


    // ========== Método para cadastrar um produto no sistema ========== //
    public int cadastrarProduto(ProdutoDTO produtoDTO) {
        Connection conn = conexao.conectar(); //Cria conexão com o banco
        ResultSet rset = null; //Para pegar o id do produto

        //Prepara a instrução SQL para inserir o produto e estoque
        String comandoProduto = "INSERT INTO produto (nome, tipo, unidade_medida, concentracao) VALUES (?, ?, ?, ?)";
        String comandoEstoque = "INSERT INTO estoque (quantidade, data_validade, min_possivel_estocado, id_produto, id_eta) " +
                "VALUES (0, '2100-01-01', 100, ?, ?)"; //Insere um estoque predefinido

        try (PreparedStatement pstmtProduto = conn.prepareStatement(comandoProduto, Statement.RETURN_GENERATED_KEYS); //Retorna o id gerado
             PreparedStatement pstmtEstoque = conn.prepareStatement(comandoEstoque)){
            //Settando os valores da instrução SQL
            pstmtProduto.setString(1, produtoDTO.getNome());
            pstmtProduto.setString(2, produtoDTO.getTipo());
            pstmtProduto.setString(3, produtoDTO.getUnidadeMedida());
            pstmtProduto.setDouble(4, produtoDTO.getConcentracao());

            if (pstmtProduto.executeUpdate() == 0){
                return 0; //Não conseguiu criar
            }

            //Pegando o ID gerado do produto
            rset = pstmtProduto.getGeneratedKeys();
            int idProdutoGerado = -1; //Valor nulo
            if (rset.next()) {
                idProdutoGerado = rset.getInt(1); //Pega a primeira coluna da query
            } else {
                return 0;
            }

            //Settando o id gerado no parâmetro
            pstmtEstoque.setInt(1, idProdutoGerado); // FK produto
            pstmtEstoque.setInt(2, produtoDTO.getIdEta());

            if (pstmtEstoque.executeUpdate() > 0){
                return 1; //Conseguiu criar estoque também
            } else {
                return 0;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace(); //Registra o erro no terminal
            return -1;
        } finally {
            conexao.desconectar(); //Finaliza a conexão, mesmo se cair em exceção
        }
    }


    // ========== Método para remover um produto do sistema ========== //
    public int removerProduto(int idProduto) {
        Connection conn = conexao.conectar(); //Cria conexão com o banco
        String comandoEstoque = "DELETE FROM estoque WHERE id_produto = ?"; //Para deletar o que tiver relacionado com produto
        String comandoProduto = "DELETE FROM produto WHERE id = ?";
        String comandoUso_produto_processo="DELETE FROM USO_PRODUTO_PROCESSO WHERE ID_PRODUTO= ?";

        try {
            conn.setAutoCommit(false); // Caso de erro em algum comando, é possível voltar

            try (PreparedStatement pstmtEstoque = conn.prepareStatement(comandoEstoque);
                 PreparedStatement pstmtProduto = conn.prepareStatement(comandoProduto);
                 PreparedStatement pstmtUso = conn.prepareStatement(comandoUso_produto_processo)) {
                pstmtEstoque.setInt(1, idProduto); //No primeiro comando
                pstmtProduto.setInt(1, idProduto); //Segundo comando
                pstmtUso.setInt(1,idProduto);

                pstmtEstoque.executeUpdate(); //Primeiro: remove primeiro o estoque
                pstmtUso.executeUpdate();
                if (pstmtProduto.executeUpdate() > 0) { //Depois: remove o produto
                    conn.commit(); //Confirma a exclusão
                    return 1;
                } else {
                    conn.rollback(); //Reverte a exclusão do estoque
                    return 0;
                }
            }
        } catch (SQLException sqle) {
            try{
                conn.rollback(); //Reverte se der erro
            } catch (SQLException e){
                e.printStackTrace();
            }
            sqle.printStackTrace(); //remover no final do projetoooooooo
            return -1; //Para indicar erro no banco de dados
        } finally {
            try{
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException sqle){
                sqle.printStackTrace();
            }
            conexao.desconectar();
        }
    }


    // ========== Método para alterar um produto do sistema ========== //
    public int alterarProduto(ProdutoDTO produtoDTO) {
        Connection conn = conexao.conectar();
        String comando = "UPDATE produto SET nome = ?, tipo = ?, unidade_medida = ?, concentracao = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            //Settando valores da instrução SQL
            pstmt.setString(1, produtoDTO.getNome());
            pstmt.setString(2, produtoDTO.getTipo());
            pstmt.setString(3, produtoDTO.getUnidadeMedida());
            pstmt.setDouble(4, produtoDTO.getConcentracao());
            pstmt.setInt(5, produtoDTO.getId()); //WHERE id = ?

            if (pstmt.executeUpdate() > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para listar os produto do sistema ========== //
    public List<ProdutoDTO> listarProdutos() {
        ResultSet rset = null; //Consulta da tabela
        List<ProdutoDTO> produtos = new ArrayList<>();
        Connection conn = conexao.conectar();
        String comando = "SELECT DISTINCT ON (p.id) p.id, p.nome, p.tipo, p.unidade_medida, p.concentracao, eta.nome AS nome_eta " +
                "FROM produto p " +
                "JOIN estoque e ON p.id = e.id_produto " +
                "JOIN eta ON eta.id = e.id_eta " +
                "ORDER BY p.id;"; //ordena por id

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            rset = pstmt.executeQuery(); //Executa a consulta SQL

            //Armazena os valores em um List<>
            while (rset.next()) {
                ProdutoDTO dto = new ProdutoDTO(); //Cria um objeto a cada repetição
                dto.setId(rset.getInt("id")); //Pega cada coluna do select
                dto.setNome(rset.getString("nome"));
                dto.setTipo(rset.getString("tipo"));
                dto.setUnidadeMedida(rset.getString("unidade_medida"));
                dto.setConcentracao(rset.getDouble("concentracao"));
                dto.setNomeEta(rset.getString("nome_eta"));
                //Populando o List
                produtos.add(dto);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>();
        } finally {
            conexao.desconectar();
        }

        return produtos; //Retorna o List com os resultados, ou vazio
    }


    // ========== Método para buscar um produto pelo id ========== // ---------------------precisa settar o nome_eta?
    public ProdutoDTO buscarPorId(ProdutoDTO produtoDTO) {
        ResultSet rset = null; //Consulta da tabela
        Connection conn = conexao.conectar();
        //Prepara a consulta SQL para selecionar os produtos pelo ID
        String comando = "SELECT p.*, et.nome AS nome_eta FROM produto p " +
                "JOIN estoque es ON p.id = es.id_produto " +
                "JOIN eta et ON et.id = es.id_eta " +
                "WHERE p.id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, produtoDTO.getId());
            rset = pstmt.executeQuery(); //Executa a consulta

            //Armazenar os valores no mesmo objeto recebido como parâmetro
            //Somente um objeto pois não existe produto com o mesmo id
            if (rset.next()) {
                //Não é necessário settar o id pois já vem settado no objeto
                produtoDTO.setNome(rset.getString("nome"));
                produtoDTO.setTipo(rset.getString("tipo"));
                produtoDTO.setUnidadeMedida(rset.getString("unidade_medida"));
                produtoDTO.setConcentracao(rset.getDouble("concentracao"));
                produtoDTO.setNomeEta(rset.getString("nome_eta")); //Campo extra do DTO
            }
            return produtoDTO; //se não encontrar, volta null

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        } finally {
            conexao.desconectar();
        }
    }

//    public List<ProdutoDTO> listarProdutoPorEta(int idEta) {
//        List<ProdutoDTO> produtos = new ArrayList<>();
//        Connection conn = conexao.conectar();
//        String sql = "SELECT DISTINCT p.* " +
//                "FROM produto p\n" +
//                "JOIN estoque e ON p.id = e.id_produto " +
//                "WHERE e.id_eta = ?";
//
//        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, idEta);
//            ResultSet rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                ProdutoDTO dto = new ProdutoDTO();
//                dto.setId(rs.getInt("id"));
//                dto.setNome(rs.getString("nome"));
//                dto.setTipo(rs.getString("tipo"));
//                dto.setUnidadeMedida(rs.getString("unidade_medida"));
//                dto.setConcentracao(rs.getDouble("concentracao"));
//                produtos.add(dto);
//            }
//            return produtos; //Populando o list
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return new ArrayList<>();
//        } finally {
//            conexao.desconectar();
//        }
//    }


    // ========== Método para buscar o id de um produto pelo seu nome ========== //
    public Integer buscarIdPorNome(String nome) { //------------------- pode juntar com o metodo de baixo
        Connection conn = conexao.conectar();
        Integer id = 0; //Valor padrão caso não encontre
        String comando = "SELECT id FROM produto WHERE LOWER(nome) = LOWER(?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setString(1, nome); // busca exata
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            id = 0;
        } finally {
            conexao.desconectar();
        }
        return id;
    }


    // ========== Método para buscar um produto pelo seu nome ========== //
//    public List<ProdutoDTO> buscarPorNome(String nome) {
//        List<ProdutoDTO> lista = new ArrayList<>();
//        Connection conn = conexao.conectar();
//        String sql = "SELECT p.*, e.nome AS nome_eta FROM produto p " +
//                "JOIN estoque ON p.id = estoque.id_produto " +
//                "JOIN eta e ON e.id = estoque.id_eta " +
//                "WHERE LOWER(nome) = LOWER(?)";
//
//        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, nome);
//            ResultSet rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                ProdutoDTO dto = new ProdutoDTO();
//                dto.setId(rs.getInt("id"));
//                dto.setNome(rs.getString("nome"));
//                dto.setTipo(rs.getString("tipo"));
//                dto.setUnidadeMedida(rs.getString("unidade_medida"));
//                dto.setConcentracao(rs.getDouble("concentracao"));
//                dto.setNomeEta(rs.getString("nome_eta")); //Faz JOIN com estoque e eta
//                lista.add(dto);
//            }
//           return lista;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return new ArrayList<>();
//        }
//    }


    // ========== Método para filtrar um produto ========== //
    public List<ProdutoDTO> filtroBuscaPorColuna(String coluna, String pesquisa) {
        String tabela;
        String operador = "LIKE";
        boolean numero = false;
        boolean Double = false;

        // Define de qual tabela e tipo de dado vem a coluna
        switch (coluna.toLowerCase()) {
            case "id":
                tabela = "PRODUTO";
                operador = "=";
                numero = true;
                break;

            case "concentracao":
                tabela = "PRODUTO";
                operador = "=";
                Double = true;
                break;

            case "nome_eta":
                tabela = "ETA";
                coluna = "NOME";
                break;

            default:
                tabela = "PRODUTO";
        }

        Connection conn = conexao.conectar();
        List<ProdutoDTO> lista = new ArrayList<>(); //Para armazenar os dados da query
        //SQL com join para trazer nome da ETA
        String comando =
                "SELECT PRODUTO.*, ETA.NOME AS nome_eta " +
                        "FROM PRODUTO " +
                        "JOIN ESTOQUE ON ESTOQUE.id_produto = PRODUTO.id " +
                        "JOIN ETA ON ETA.id = ESTOQUE.id_eta " +
                        "WHERE LOWER(" + tabela + "." + coluna + ") " + operador + " LOWER(?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            //Define o tipo de dado corretamente
            if (numero) {
                pstmt.setInt(1, Integer.parseInt(pesquisa));
            } else if (Double) {
                pstmt.setDouble(1, java.lang.Double.parseDouble(pesquisa));
            } else {
                pstmt.setString(1, "%" + pesquisa + "%");
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ProdutoDTO dto = new ProdutoDTO();
                dto.setId(rs.getInt("id"));
                dto.setNome(rs.getString("nome"));
                dto.setTipo(rs.getString("tipo"));
                dto.setUnidadeMedida(rs.getString("unidade_medida"));
                dto.setConcentracao(rs.getDouble("concentracao"));
                dto.setNomeEta(rs.getString("nome_eta"));
                lista.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } catch (NumberFormatException e) { //------------------------ trocar por Exception
            System.out.println("valor invalido para número ou concentração");
        } finally {
            conexao.desconectar();
        }

        return lista; //Preenchida ou vazia
    }
}