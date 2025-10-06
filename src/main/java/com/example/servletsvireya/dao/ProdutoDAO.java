package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.ProdutoDTO;
import com.example.servletsvireya.model.Produto;
import com.example.servletsvireya.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public int removerProduto(Produto produto) {
        Connection conn = conexao.conectar(); //Cria conexão com o banco
        String comando = "DELETE FROM produto WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, produto.getId());
            System.out.println("ID recebido para exclusão: " + produto.getId());

            //Aqui precisa verificar se o id do produto existe !!!

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
    public int alterarConcentracao(Produto produto) { //Altera a concentração (por ID)
        try {
            Connection conn = conexao.conectar();

            PreparedStatement pstmt = conn.prepareStatement("UPDATE produto SET concentracao = ? WHERE id = ?");
            pstmt.setDouble(1, produto.getConcentracao());
            pstmt.setInt(2, produto.getId());

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

    //Altera unidade de medida do produto
    public int alterar(Produto original, Produto modificado) {
        List<Object> inputs = new ArrayList<>();

        //Criando uma String construtora
        StringBuilder comando = new StringBuilder("UPDATE produto SET ");

        //Pegando os valores do model do talvez modificado
        int id = modificado.getId();
        String nome = modificado.getNome();
        String tipo = modificado.getTipo();
        String unidadeMedida = modificado.getUnidadeMedida();
        double concentracao = modificado.getConcentracao();

        //Checando se foi modificado ou não, em relação ao original
        if (!nome.equals(original.getNome())) {
            inputs.add(nome); //Adiciona o valor que foi modificado
            comando.append("nome = ?, "); //Concatena a string do comandoSQL
        }
        if (!tipo.equals(original.getTipo())) { //Não pode ser elseif
            inputs.add(tipo);
            comando.append("tipo = ?, ");
        }
        if (!unidadeMedida.equals(original.getUnidadeMedida())) {
            inputs.add(unidadeMedida);
            comando.append("unidade_medida = ?, ");
        }
        if (concentracao != original.getConcentracao()) {
            inputs.add(concentracao); // ✅ aqui colocamos o valor correto
            comando.append("concentracao = ?, ");
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

    //Método para listar produtos
    public List<Produto> listarProduto() {
        ResultSet rset = null; //Consulta da tabela
        List<Produto> produtos = new ArrayList<>();

        Connection conn = conexao.conectar();
        String comando = "SELECT * FROM produto ORDER BY nome"; //ordena em ordem alfabética
        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            rset = pstmt.executeQuery(); //Executa a consulta com Query

            //Armazenar os valores em um List<>
            while (rset.next()) {
                int id = rset.getInt(1); //Pega a primeira coluna do select
                String nome = rset.getString(2);
                String tipo = rset.getString(3);
                String unidadeMedida = rset.getString(4);
                double concentracao = rset.getDouble(5);

                //Populando o List
                produtos.add(new Produto(id, nome, tipo, unidadeMedida, concentracao));
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
    public Produto selecionarProduto(Produto produto) { //Seria um filtro né???
        ResultSet rset = null; //Consulta da tabela
        Connection conn = conexao.conectar();

        //Prepara a consulta SQL para selecionar os produtos por ordem de ID
        String comando = "SELECT * FROM produto WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, produto.getId());
            rset = pstmt.executeQuery(); //Executa a consulta com Query

            //Armazenar os valores em uma variável tipo produto
            //variavel pois nao existe id repetido
            if (rset.next()) {
//                int id = rset.getInt("id"); //Pega a primeira coluna do select
                produto.setNome(rset.getString("nome"));
                produto.setTipo(rset.getString("tipo"));
                produto.setUnidadeMedida(rset.getString("unidade_medida"));
                produto.setConcentracao(rset.getDouble("concentracao"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexao.desconectar();
        }
        return produto; //se não encontrar, volta null
    }

    public List<ProdutoDTO> listarProdutoPorEta(int idEta) {
        List<ProdutoDTO> produtos = new ArrayList<>();
        Connection conn = conexao.conectar();

        String sql = "        SELECT DISTINCT p.id, p.nome, p.tipo, p.unidade_medida, p.concentracao\n" +
                "        FROM produto p\n" +
                "        JOIN estoque e ON e.id_produtos = p.id\n" +
                "        WHERE e.id_eta = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEta);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ProdutoDTO dto = new ProdutoDTO();
                    dto.setId(rs.getInt("id"));
                    dto.setNome(rs.getString("nome"));
                    dto.setTipo(rs.getString("tipo"));
                    dto.setUnidadeMedida(rs.getString("unidade_medida"));
                    dto.setConcentracao(rs.getDouble("concentracao"));
                    produtos.add(dto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 👈 Ajuda a ver o erro no console
        } finally {
            conexao.desconectar();
        }

        return produtos;
    }

}