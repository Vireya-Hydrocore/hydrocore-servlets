package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.AdminDTO;
import com.example.servletsvireya.dto.EtaDTO;
import com.example.servletsvireya.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EtaDAO {
    Conexao conexao = new Conexao(); //Para os métodos de conectar() e desconectar()


    // ========== Método para cadastrar uma ETA no sistema ========== //
    public int cadastrarEta(EtaDTO dto, AdminDTO adminDTO) { //junto com um admin primário
        //criado fora para permitir o fechamento no finally
        int idEta;
        idEta = -1;//usei para achar achar melhor e para caso eu não pegue o id não seja realizado inserção no banco

        Connection conn;
        conn = null;

        PreparedStatement pstmtEnd;
        pstmtEnd = null;

        PreparedStatement pstmtEta;
        pstmtEta = null;

        PreparedStatement pstmtAdmin;
        pstmtAdmin = null;

        ResultSet rsEnd;
        rsEnd = null;

        ResultSet rsEta;
        rsEta = null;

        try {
            conn = conexao.conectar();
            conn.setAutoCommit(false);

            // Inserir endereço
            String sqlEnd;
            sqlEnd = "INSERT INTO endereco (rua, numero, bairro, cidade, estado, cep) " +
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

            pstmtEnd = conn.prepareStatement(sqlEnd);
            pstmtEnd.setString(1, dto.getRua());
            pstmtEnd.setInt(2, dto.getNumero());
            pstmtEnd.setString(3, dto.getBairro());
            pstmtEnd.setString(4, dto.getCidade());
            pstmtEnd.setString(5, dto.getEstado());
            pstmtEnd.setString(6, dto.getCep());


            //pego o id de endereco para colocar na eta
            rsEnd = pstmtEnd.executeQuery();

            int idEndereco;
            idEndereco = 0;
            if (rsEnd.next()) {
                idEndereco = rsEnd.getInt("id");
            }

            // Inserir ETA e pegar o id gerado
            String sqlEta;
            sqlEta = "INSERT INTO eta (nome, capacidade, cnpj, id_endereco, telefone) VALUES (?, ?, ?, ?, ?) RETURNING id";

            pstmtEta = conn.prepareStatement(sqlEta);
            pstmtEta.setString(1, dto.getNome());
            pstmtEta.setDouble(2, dto.getCapacidade());
            pstmtEta.setString(3, dto.getCnpj());
            pstmtEta.setString(5, dto.getTelefone());
            pstmtEta.setInt(4, idEndereco);

            //pego o id de eta para colocar em admin
            rsEta = pstmtEta.executeQuery();
            if (rsEta.next()) {
                idEta = rsEta.getInt("id");
            }

            // Inserir admin vinculado à ETA
            String sqlAdmin;
            sqlAdmin = "INSERT INTO admin (nome, senha, email, id_eta) VALUES (?, ?, ?, ?)";

            pstmtAdmin = conn.prepareStatement(sqlAdmin);
            pstmtAdmin.setString(1, adminDTO.getNome());
            pstmtAdmin.setString(2, adminDTO.getSenha());
            pstmtAdmin.setString(3, adminDTO.getEmail());
            pstmtAdmin.setInt(4, idEta);
            pstmtAdmin.executeUpdate();

            conn.commit();//commito no banco

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();// se tirer errado a conexão volta
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (rsEnd != null) rsEnd.close();
                if (rsEta != null) rsEta.close();
                if (pstmtEnd != null) pstmtEnd.close();
                if (pstmtEta != null) pstmtEta.close();
                if (pstmtAdmin != null) pstmtAdmin.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return idEta;
    }


    // ========== Método para listar as ETAs ========== //
    public List<EtaDTO> listarEtas() {
        Connection conn;
        conn = conexao.conectar(); //Criando conexão com o banco

        ResultSet rset; //Consulta
        rset = null;

        List<EtaDTO> etas;
        etas = new ArrayList<>(); //Criação de um List para armazenar os dados

        //Prepara a instrução SQL para listar as ETAs
        String comando;
        comando = "SELECT e.*, en.cep FROM eta e " +
                "JOIN endereco en ON en.id = e.id_endereco " +
                "ORDER BY id"; //Ordena por ID

        try {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comando);
            rset = pstmt.executeQuery(); //Executa e retorna a query

            while (rset.next()) {
                EtaDTO etaDTO;
                etaDTO = new EtaDTO();

                int id;
                id = rset.getInt("id");
                etaDTO.setId(id);

                String nome;
                nome = rset.getString("nome");
                etaDTO.setNome(nome);

                int capacidade;
                capacidade = rset.getInt("capacidade");
                etaDTO.setCapacidade(capacidade);

                String telefone;
                telefone = rset.getString("telefone");
                etaDTO.setTelefone(telefone);

                String cnpj;
                cnpj = rset.getString("cnpj");
                etaDTO.setCnpj(cnpj);

                String cep;
                cep = rset.getString("cep");
                etaDTO.setCep(cep); //De endereço

                etas.add(etaDTO); //Populando o list
            }
            return etas;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>(); //Vazio
        } finally {
            conexao.desconectar(); //Finaliza a conexão, mesmo se cair em exceção
        }
    }


    // ========== Método para alterar alguma ETA ========== //
    public int alterarEta(EtaDTO etaDTO) {
        Connection conn;
        conn = conexao.conectar();

        String comandoEta;
        comandoEta = "UPDATE eta SET nome = ?, capacidade = ?, telefone = ? WHERE id = ?"; //Não pode mudar CNPJ

        String comandoEndereco;
        comandoEndereco = "UPDATE endereco SET rua = ?, bairro = ?, cidade = ?, estado = ?, numero = ?, cep = ? WHERE id = ?";

        try  {
            PreparedStatement pstmtEndereco;
            PreparedStatement pstmtEta;
            pstmtEndereco = conn.prepareStatement(comandoEndereco);
            pstmtEta = conn.prepareStatement(comandoEta);
            conn.setAutoCommit(false); //Tem que verificar se os dois atualizaram

            //Primeiro: altera endereço
            pstmtEndereco.setString(1, etaDTO.getRua());
            pstmtEndereco.setString(2, etaDTO.getBairro());
            pstmtEndereco.setString(3, etaDTO.getCidade());
            pstmtEndereco.setString(4, etaDTO.getEstado());
            pstmtEndereco.setInt(5, etaDTO.getNumero());
            pstmtEndereco.setString(6, etaDTO.getCep());
            pstmtEndereco.setInt(7, etaDTO.getId()); //mesmo id

            int resultadoEndereco;
            resultadoEndereco = pstmtEndereco.executeUpdate();

            if (resultadoEndereco <= 0){
                return 0;
            }

            //Depois: em eta
            pstmtEta.setString(1, etaDTO.getNome());
            pstmtEta.setInt(2, etaDTO.getCapacidade());
            pstmtEta.setString(3, etaDTO.getTelefone());
            pstmtEta.setInt(4, etaDTO.getId()); //WHERE id = ?

            int resultadoEta;
            resultadoEta = pstmtEta.executeUpdate();

            if (resultadoEta > 0) {
                conn.commit(); //Deu certo
                return 1;
            } else {
                conn.rollback();
                return 0;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            try {
                if (conn != null) conn.rollback(); //Volta pois algum não deu certo

                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true); //Setta true novamente
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            conexao.desconectar();
        }
    }


    // ========== Método para remover uma ETA ========== //
    public int removerEta(EtaDTO etaDTO) {
        Connection conn;
        conn = conexao.conectar();

        String comando;
        comando = "DELETE FROM eta WHERE id = ?";

        try  {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comando);

            pstmt.setInt(1, etaDTO.getId());

            int resultado;
            resultado = pstmt.executeUpdate();

            if (resultado > 0) {
                return 1; //Deleção bem sucedida
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


    // ========== Método para buscar uma ETA por ID ========== //
    public EtaDTO buscarPorId(EtaDTO etaDTO) {
        ResultSet rset; //Consulta da tabela
        rset = null;

        Connection conn;
        conn = conexao.conectar();

        //Prepara a consulta SQL para selecionar os produtos por ordem de ID
        String comando;
        comando = "SELECT eta.*, endereco.* FROM eta " +
                "JOIN endereco on endereco.id = eta.id_endereco" +
                " WHERE eta.id = ?";

        try {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comando);
            pstmt.setInt(1, etaDTO.getId());
            rset = pstmt.executeQuery(); //Executa a consulta com Query

            //Armazenar os valores em um objeto etaDTO
            //Somente um pois nao existe id repetido
            if (rset.next()) {
                String nome;
                nome = rset.getString("nome");
                etaDTO.setNome(nome);

                int capacidade;
                capacidade = rset.getInt("capacidade");
                etaDTO.setCapacidade(capacidade);

                String telefone;
                telefone = rset.getString("telefone");
                etaDTO.setTelefone(telefone);

                String cnpj;
                cnpj = rset.getString("cnpj");
                etaDTO.setCnpj(cnpj);

                String rua;
                rua = rset.getString("rua");
                etaDTO.setRua(rua); //endereço

                String bairro;
                bairro = rset.getString("bairro");
                etaDTO.setBairro(bairro);

                String cidade;
                cidade = rset.getString("cidade");
                etaDTO.setCidade(cidade);

                String estado;
                estado = rset.getString("estado");
                etaDTO.setEstado(estado);

                String cep;
                cep = rset.getString("cep");
                etaDTO.setCep(cep);

                String numeroStr;
                numeroStr = rset.getString("numero");

                int numero;
                numero = Integer.parseInt(numeroStr);
                etaDTO.setNumero(numero);
            }
            return etaDTO; //se não encontrar, volta null

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        } finally {
            conexao.desconectar();
        }
    }


    // ========== Método para buscar um id da ETA pelo nome ========== //
    public Integer buscarIdPorNome(String nome) {
        Connection conn;
        conn = conexao.conectar();

        Integer idEta;
        idEta = 0; //Valor padrão caso não encontre

        String sql;
        sql = "SELECT id FROM eta WHERE LOWER(nome) = LOWER(?)";

        try {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nome);

            ResultSet rs;
            rs = pstmt.executeQuery();

            if (rs.next()) {
                idEta = rs.getInt("id");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            idEta = 0;
        } finally {
            conexao.desconectar();
        }

        return idEta; //Retorna o id ou 0
    }


    // ========== Método para filtrar uma ETA ========== //
    public List<EtaDTO> filtroBuscaPorColuna(String coluna, String pesquisa) {
        String tabela;
        tabela = null;

        String operador;
        operador = "LIKE";

        boolean numero;
        numero = false; //Inteiro

        boolean Double;
        Double = false; // Variável não usada, mas mantida a declaração

        // Define de qual tabela e tipo de dado vem a coluna
        switch (coluna.toLowerCase()) {
            case "capacidade":
                tabela = "eta";
                operador = "=";
                numero = true;
                break;
            case "cep":
                tabela = "endereco";
                operador = "LIKE";
                numero = true;
                break;
            default:
                tabela = "eta";
        }

        Connection conn;
        conn = conexao.conectar();

        List<EtaDTO> lista;
        lista = new ArrayList<>();

        // SQL com join para trazer o CEP de endereço
        String comando;
        comando = "SELECT eta.*, endereco.cep FROM eta " +
                "JOIN endereco ON endereco.id = eta.id_endereco " +
                "WHERE " + tabela + "." + coluna + " " + operador + " ?";

        try  {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(comando);
            // Define o tipo de dado corretamente
            if (numero) {
                int valorNumerico;
                valorNumerico = Integer.parseInt(pesquisa);
                pstmt.setInt(1, valorNumerico);
            } else { //É string
                String pesquisaFormatada;
                pesquisaFormatada = "%" + pesquisa + "%";
                pstmt.setString(1, pesquisaFormatada);
            }

            ResultSet rs;
            rs = pstmt.executeQuery();

            while (rs.next()) {
                EtaDTO eta;
                eta = new EtaDTO();

                int id;
                id = rs.getInt("id");
                eta.setId(id);

                String nome;
                nome = rs.getString("nome");
                eta.setNome(nome);

                String telefone;
                telefone = rs.getString("telefone");
                eta.setTelefone(telefone);

                String cnpj;
                cnpj = rs.getString("cnpj");
                eta.setCnpj(cnpj);

                String cep;
                cep = rs.getString("cep");
                eta.setCep(cep); //endereço

                lista.add(eta);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>();
        } catch (NumberFormatException e) {
            System.out.println("valor inválido para número ou concentração");
        } finally {
            conexao.desconectar();
        }

        return lista; //Preenchida ou vazia
    }
}