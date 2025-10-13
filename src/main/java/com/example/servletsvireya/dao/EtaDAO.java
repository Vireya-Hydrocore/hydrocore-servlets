package com.example.servletsvireya.dao;



import com.example.servletsvireya.dto.AdminDTO;
import com.example.servletsvireya.dto.EtaDTO;
import com.example.servletsvireya.dto.ProdutoDTO;
import com.example.servletsvireya.model.Eta;
import com.example.servletsvireya.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EtaDAO {
    Conexao conexao;

    public EtaDAO() {
        this.conexao = new Conexao();
    }

    // Método inserirEta()
    public int inserirEta(EtaDTO etaDTO) {
        Connection conn = conexao.conectar();
        String comando = "INSERT INTO eta (nome, capacidade, telefone, cnpj,id_eta) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setString(1, etaDTO.getNome());
            pstmt.setInt(2, etaDTO.getCapacidade());
            pstmt.setString(3, etaDTO.getTelefone());
            pstmt.setString(4, etaDTO.getCnpj());

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

    // Método listar as ETAs
    public List<EtaDTO> listarEta() {
        ResultSet rset = null;
        List<EtaDTO> etas = new ArrayList<>();

        Connection conn = conexao.conectar();
        String comando = "SELECT e.*, en.cep FROM eta e " +
                "JOIN endereco en ON en.id = e.id_endereco " +
                "ORDER BY id";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            rset = pstmt.executeQuery();

            while (rset.next()) {
                EtaDTO etaDTO = new EtaDTO();
                etaDTO.setId(rset.getInt("id"));
                etaDTO.setNome(rset.getString("nome"));
                etaDTO.setCapacidade(rset.getInt("capacidade"));
                etaDTO.setTelefone(rset.getString("telefone"));
                etaDTO.setCnpj(rset.getString("cnpj"));
                etaDTO.setCep(rset.getString("cep")); //Endereço

                //Populando o list
                etas.add(etaDTO);
            }
            return etas;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new ArrayList<>();
        } finally {
            conexao.desconectar();
        }
    }


    // Método alterarEta()
    public int alterarEta(EtaDTO etaDTO) {
        Connection conn = conexao.conectar();
        String comandoEta = "UPDATE eta SET nome = ?, capacidade = ?, telefone = ? WHERE id = ?"; //Não pode mudar cnpj
        String comandoEndereco = "UPDATE endereco SET rua = ?, bairro = ?, cidade = ?, estado = ?, numero = ?, cep = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comandoEndereco);
             PreparedStatement pstmt2 = conn.prepareStatement(comandoEta)) {

            conn.setAutoCommit(false); //Tem que verificar se os dois atualizaram

            //Primeiro altera endereço
            pstmt.setString(1, etaDTO.getRua());
            pstmt.setString(2, etaDTO.getBairro());
            pstmt.setString(3, etaDTO.getCidade());
            pstmt.setString(4, etaDTO.getEstado());
            pstmt.setInt(5, etaDTO.getNumero());
            pstmt.setString(6, etaDTO.getCep());
            pstmt.setInt(7, etaDTO.getId()); //mesmo id

            if (pstmt.executeUpdate() <= 0){
                return 0;
            }

            //Em eta
            pstmt2.setString(1, etaDTO.getNome());
            pstmt2.setInt(2, etaDTO.getCapacidade());
            pstmt2.setString(3, etaDTO.getTelefone());
            pstmt2.setInt(4, etaDTO.getId()); //WHERE id = ?

            if (pstmt2.executeUpdate() > 0) {
                conn.commit(); //Deu certo
                return 1;
            } else {
                conn.rollback();
                return 0;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            try {
                if (conn != null) conn.rollback(); //Volta pois algum nao deu certo
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true); //Setta true novamente
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conexao.desconectar();
        }
    }


    // Método remover()
    public int removerEta(EtaDTO etaDTO) {
        Connection conn = conexao.conectar();
        String comando = "DELETE FROM eta WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, etaDTO.getId());

            if (pstmt.executeUpdate() > 0) {
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

    public int inserir(EtaDTO dto, AdminDTO adminDTO) {
        //criado fora para permitir o fechamento no finally
        int idEta = -1;//usei para achar achar melhor e para caso eu não pegue o id não seja realizado inserção no banco
        Connection conn = null;
        PreparedStatement pstmtEnd = null;
        PreparedStatement pstmtEta = null;
        PreparedStatement pstmtAdmin = null;
        ResultSet rsEnd = null;
        ResultSet rsEta = null;

        try {
            conn = conexao.conectar();
            conn.setAutoCommit(false);

            // Inserir endereço
            String sqlEnd = "INSERT INTO endereco (rua, numero, bairro, cidade, estado, cep) " +
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
            int idEndereco = 0;
            if (rsEnd.next()) {
                idEndereco = rsEnd.getInt("id");
            }

            // Inserir ETA e pegar o id gerado
            String sqlEta = "INSERT INTO eta (nome, capacidade, cnpj, id_endereco, telefone) VALUES (?, ?, ?, ?, ?) RETURNING id";
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
            String sqlAdmin = "INSERT INTO admin (nome, senha, email, id_eta) VALUES (?, ?, ?, ?)";
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
    //Buscar pelo ID
    public EtaDTO buscarPorId(EtaDTO etaDTO) {
        ResultSet rset = null; //Consulta da tabela
        Connection conn = conexao.conectar();
        //Prepara a consulta SQL para selecionar os produtos por ordem de ID
        String comando = "SELECT eta.*, endereco.* FROM eta " +
                "JOIN endereco on endereco.id = eta.id_endereco" +
                " WHERE eta.id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setInt(1, etaDTO.getId());
            rset = pstmt.executeQuery(); //Executa a consulta com Query

            //Armazenar os valores em uma variável tipo eta
            //variavel pois nao existe id repetido
            if (rset.next()) {
                etaDTO.setNome(rset.getString("nome"));
                etaDTO.setCapacidade(rset.getInt("capacidade"));
                etaDTO.setTelefone(rset.getString("telefone"));
                etaDTO.setCnpj(rset.getString("cnpj"));
                etaDTO.setRua(rset.getString("rua")); //endereço
                etaDTO.setBairro(rset.getString("bairro"));
                etaDTO.setCidade(rset.getString("cidade"));
                etaDTO.setEstado(rset.getString("estado"));
                etaDTO.setCep(rset.getString("cep"));
                etaDTO.setNumero(Integer.parseInt(rset.getString("numero")));
            }
            return etaDTO; //se não encontrar, volta null

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        } finally {
            conexao.desconectar();
        }
    }
    public Integer buscarIdPorNome(String nome) {
        Integer idEta = null;
        String sql = "SELECT id FROM eta WHERE LOWER(nome) = LOWER(?)";

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                idEta = rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idEta;
    }


    public EtaDTO buscarPorNome(String nome) {
        EtaDTO eta = null;
        String sql = "SELECT * FROM eta WHERE LOWER(nome) = LOWER(?)";

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                eta = new EtaDTO();
                eta.setId(rs.getInt("id"));
                eta.setNome(rs.getString("nome"));
                eta.setCapacidade(rs.getInt("capacidade"));
                eta.setTelefone(rs.getString("telefone"));
                eta.setCnpj(rs.getString("cnpj"));
                eta.setIdEndereco(rs.getInt("id_endereco"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eta;
    }


    public List<EtaDTO> filtroBuscaPorColuna(String coluna, String pesquisa) {
        String tabela;
        String operador = "LIKE";
        boolean numero = false; //Inteiro
        boolean Double = false;

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

        // SQL com join para trazer nome da ETA
        String sql =
                "SELECT eta.*, endereco.cep FROM eta " +
                        "JOIN endereco ON endereco.id = eta.id_endereco" +
                        "WHERE " + tabela + "." + coluna + " " + operador + " ?";

        List<EtaDTO> lista = new ArrayList<>();
        Connection conn = conexao.conectar();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o tipo de dado corretamente
            if (numero) {
                stmt.setInt(1, Integer.parseInt(pesquisa));
            } else { //É string
                stmt.setString(1, "%" + pesquisa + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                EtaDTO dto = new EtaDTO();
                dto.setId(rs.getInt("id"));
                dto.setNome(rs.getString("nome"));
                dto.setTelefone(rs.getString("telefone"));
                dto.setCnpj(rs.getString("cnpj"));
                dto.setCep(rs.getString("cep")); //endereço
                lista.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("valor inválido para número ou concentração");
        }

        return lista;
    }





































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































}


    // Método removerDuplicadas() ---> É necessário??????

//    public int removerDuplicadas(){
//        Connection conn = conexao.conectar();
//        String comando = "DELETE FROM ETA WHERE id NOT IN (SELECT MIN(id) FROM ETA GROUP BY nome, capacidade)";
//
//        try(PreparedStatement pstmt = conn.prepareStatement(comando)){
//
//            int qtdRemovida = pstmt.executeUpdate();
//            return qtdRemovida;
//        }
//        catch (SQLException sqle){
//            sqle.printStackTrace();
//            return -1;
//        }
//        finally {
//            conexao.desconectar();
//        }
