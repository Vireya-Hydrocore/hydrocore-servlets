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
    private final Conexao conexao = new Conexao(); //Só para os método de conectar() e desconectar()


    //Método para cadastrar um produto no sistema
    public int cadastrarProduto(ProdutoDTO produtoDTO, int idEta) {
        Connection conn = conexao.conectar();
        ResultSet rset = null;

        String comandoProduto = "INSERT INTO produto (nome, tipo, unidade_medida, concentracao) VALUES (?, ?, ?, ?)";
        String comandoEstoque = "INSERT INTO estoque (quantidade, data_validade, min_possivel_estocado, id_produto, id_eta) VALUES (0, '01-01-2100', 0, ?, ?)";

        try (PreparedStatement pstmtProduto = conn.prepareStatement(comandoProduto, Statement.RETURN_GENERATED_KEYS); //Retorna o id gerado
             PreparedStatement pstmtEstoque = conn.prepareStatement(comandoEstoque)){
            pstmtProduto.setString(1, produtoDTO.getNome());
            pstmtProduto.setString(2, produtoDTO.getTipo());
            pstmtProduto.setString(3, produtoDTO.getUnidadeMedida());
            pstmtProduto.setDouble(4, produtoDTO.getConcentracao());

            if (pstmtProduto.executeUpdate() == 0){
                return 0; //Não conseguiu criar
            }

            //Pegando o ID gerado do produto
            rset = pstmtProduto.getGeneratedKeys();
            int idProdutoGerado = -1;
            if (rset.next()) {
                idProdutoGerado = rset.getInt(1);
                System.out.println("id: " + idProdutoGerado);
            } else {
                System.out.println("nao");
                return 0;
            }

            //Settando o id gerado no parâmetro
            pstmtEstoque.setInt(1, idProdutoGerado); // FK produto
            pstmtEstoque.setInt(2, idEta); // id_eta fixo por enquanto (pode vir do formulário depois)

            if (pstmtEstoque.executeUpdate() > 0){
                System.out.println("pstmtEstoque 1");
                return 1; //Conseguiu criar estoque também
            } else {
                System.out.println("nao2");
                return 0;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }
    }


    //Método para remover um produto
    public int removerProduto(ProdutoDTO produtoDTO) {
        Connection conn = conexao.conectar(); //Cria conexão com o banco
        String comando = "DELETE FROM estoque WHERE id_produto = ?"; //Tem que deletar o que tiver relacionado também
        String comando2 = "DELETE FROM produto WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando);
             PreparedStatement pstmt2 = conn.prepareStatement(comando2)) {
            pstmt.setInt(1, produtoDTO.getId()); //No primeiro comando
            pstmt2.setInt(1, produtoDTO.getId()); //Segundo comando

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

//    Método para listar produtos
    public List<ProdutoDTO> listarProduto() {
        ResultSet rset = null; //Consulta da tabela
        List<ProdutoDTO> produtos = new ArrayList<>();
        Connection conn = conexao.conectar();
        String comando = "SELECT DISTINCT ON (p.id) p.id, p.nome, p.tipo, p.unidade_medida, p.concentracao, eta.nome AS nome_eta FROM produto p JOIN estoque e ON p.id = e.id_produto JOIN eta ON eta.id = e.id_eta ORDER BY p.id;"; //ordena em ordem alfabética

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            rset = pstmt.executeQuery(); //Executa a consulta com Query

            //Armazenar os valores em um List<>
            while (rset.next()) {
                ProdutoDTO dto = new ProdutoDTO();
                dto.setId(rset.getInt("id")); //Pega a primeira coluna do select
                dto.setNome(rset.getString("nome"));
                dto.setTipo(rset.getString("tipo"));
                dto.setUnidadeMedida(rset.getString("unidade_medida"));
                dto.setConcentracao(rset.getDouble("concentracao"));
                dto.setNomeEta(rset.getString("nome_eta"));
                //Populando o List
                produtos.add(dto);
            }
            return produtos; //Retorna o List com os resultados

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); //Retorna lista vazia
        } finally {
            conexao.desconectar();
        }
    }
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
        String sql = "SELECT DISTINCT p.* " +
                "FROM produto p\n" +
                "JOIN estoque e ON p.id = e.id_produto " +
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

    public int buscarIdPorNome(String nome) {
        Connection conn = conexao.conectar();
        int id = -1; // valor padrão caso não encontre
        String sql = "SELECT id FROM produto WHERE nome = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome); // busca exata
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexao.desconectar();
        }
        return id;
    }

    public List<ProdutoDTO> buscarPorNome(String nome) {
        List<ProdutoDTO> lista = new ArrayList<>();
        Connection conn = conexao.conectar();
        String sql = "SELECT p.*, e.nome AS nome_eta FROM produto p " +
                "JOIN estoque ON p.id = estoque.id_produto " +
                "JOIN eta e ON e.id = estoque.id_eta " +
                "WHERE LOWER(nome) = LOWER(?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ProdutoDTO dto = new ProdutoDTO();
                dto.setId(rs.getInt("id"));
                dto.setNome(rs.getString("nome"));
                dto.setTipo(rs.getString("tipo"));
                dto.setUnidadeMedida(rs.getString("unidade_medida"));
                dto.setConcentracao(rs.getDouble("concentracao"));
                dto.setNomeEta(rs.getString("nome_eta")); //Faz JOIN com estoque e eta
                lista.add(dto);
            }
           return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}