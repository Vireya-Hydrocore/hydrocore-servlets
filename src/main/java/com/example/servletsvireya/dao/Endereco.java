package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.EtaDTO;
import com.example.servletsvireya.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Endereco {
    private final Conexao conexao = new Conexao();
    public int inserirEndereco(EtaDTO etaDTO) {
        Connection conn = conexao.conectar();
        String sql = "INSERT INTO endereco (bairro,cep,rua,cidade,estado,numero) VALUES (?, ?, ?, ?,?,?) ";

        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, etaDTO.getBairroEndereco());
            ps.setString(2, etaDTO.getCepEndereco());
            ps.setString(3, etaDTO.getRuaEndereco());
            ps.setString(4, etaDTO.getCidadeEndereco());
            ps.setString(5, etaDTO.getEstadoEndereco());
            ps.setInt(6, etaDTO.getNumeroEndereco());

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // retorna o id_endereco gerado
                    }
                }
            }
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            conexao.desconectar();
        }

    }
}
