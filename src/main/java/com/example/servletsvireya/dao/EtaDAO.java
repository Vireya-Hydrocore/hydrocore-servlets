package com.example.servletsvireya.dao;

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
    private final Conexao conexao = new Conexao();

    // Método inserirEta()
    public int inserirEta(EtaDTO etaDTO) {
        Connection conn = conexao.conectar();
        String comando = "INSERT INTO eta (nome, capacidade, telefone, cnpj) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setString(1, etaDTO.getNome());
            pstmt.setInt(2, etaDTO.getCapacidade());
            pstmt.setString(3, etaDTO.getTelefone());
            pstmt.setString(4, etaDTO.getCnpj());

            if (pstmt.executeUpdate() > 0){
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
        String comando = "SELECT * FROM eta ORDER BY id";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            rset = pstmt.executeQuery();

            while (rset.next()) {
                EtaDTO etaDTO = new EtaDTO();
                etaDTO.setId(rset.getInt("id"));
                etaDTO.setNome(rset.getString("nome"));
                etaDTO.setCapacidade(rset.getInt("capacidade"));
                etaDTO.setTelefone(rset.getString("telefone"));

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


    //Buscar pelo ID
    public EtaDTO buscarPorId(EtaDTO etaDTO) {
        ResultSet rset = null; //Consulta da tabela
        Connection conn = conexao.conectar();
        //Prepara a consulta SQL para selecionar os produtos por ordem de ID
        String comando = "SELECT eta.*, endereco.rua, endereco.estado FROM eta " +
                "JOIN endereco on endereco.id = eta.id_endereco" +
                "WHERE id = ?";

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
                etaDTO.setRuaEndereco(rset.getString("rua")); //endereço
                etaDTO.setEstadoEndereco(rset.getString("estado"));
            }
            return etaDTO; //se não encontrar, volta null

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        } finally {
            conexao.desconectar();
        }
    }


    // Método alterarETA()
    public int alterarEta(EtaDTO etaDTO) {
        Connection conn = conexao.conectar();
        String comando = "UPDATE eta SET nome = ?, capacidade = ?, telefone = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(comando)) {
            pstmt.setString(1, etaDTO.getNome());
            pstmt.setInt(2, etaDTO.getCapacidade());
            pstmt.setString(3, etaDTO.getTelefone());
            pstmt.setInt(4, etaDTO.getId());

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
//    }
}
