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

            ps.setString(1, etaDTO.getBairro());
            ps.setString(2, etaDTO.getCep());
            ps.setString(3, etaDTO.getRua());
            ps.setString(4, etaDTO.getCidade());
            ps.setString(5, etaDTO.getEstado());
            ps.setInt(6, etaDTO.getNumero());

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
