package com.example.servletsvireya.dao;

import com.example.servletsvireya.dto.CargoDTO;
import com.example.servletsvireya.model.Cargo;
import com.example.servletsvireya.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {
    private Conexao conexao = new Conexao();

    // ➕ INSERIR CARGO (agora inclui o id_eta)
    public int inserirCargo(CargoDTO cargo) {
        String comandoSQL = "INSERT INTO Cargo(nome, acesso, id_eta) VALUES (?, ?, ?)";
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {

            pstmt.setString(1, cargo.getNome());
            pstmt.setInt(2, cargo.getAcesso());
            pstmt.setInt(3, cargo.getIdEta()); // novo campo

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int removerCargo(CargoDTO cargo) {
        String comandoSQL = "DELETE FROM Cargo WHERE id = ?";
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {

            pstmt.setInt(1, cargo.getId());
            pstmt.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public CargoDTO buscarCargo(int id) {
        String comandoSQL = "SELECT * FROM Cargo WHERE id = ?";
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                CargoDTO cargo = new CargoDTO();
                cargo.setId(rs.getInt("id"));
                cargo.setNome(rs.getString("nome"));
                cargo.setAcesso(rs.getInt("acesso"));
                cargo.setIdEta(rs.getInt("id_eta")); // adiciona o id_eta também
                return cargo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔍 LISTAR CARGOS POR ETA (sem depender de funcionário)
    public List<CargoDTO> listarCargoPorEta(int idEta) {
        List<CargoDTO> cargos = new ArrayList<>();
        String sql = "SELECT id, nome, acesso, id_eta FROM Cargo WHERE id_eta = ? ORDER BY nome";

        try (Connection conn = conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEta);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CargoDTO dto = new CargoDTO();
                dto.setId(rs.getInt("id"));
                dto.setNome(rs.getString("nome"));
                dto.setAcesso(rs.getInt("acesso"));
                dto.setIdEta(rs.getInt("id_eta"));
                cargos.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexao.desconectar();
        }
        return cargos;
    }

    public Integer buscarIdPorNome(String nomeCargo) {
        Integer id = null;
        String sql = "SELECT id FROM cargo WHERE LOWER(nome) = LOWER(?)";
        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeCargo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    // ✏️ ALTERAR CARGO (agora também permite trocar o id_eta se precisar)
    public int alterarCargo(CargoDTO cargo) {
        String comandoSQL = "UPDATE Cargo SET nome = ?, acesso = ?, id_eta = ? WHERE id = ?";
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(comandoSQL)) {

            pstmt.setString(1, cargo.getNome());
            pstmt.setInt(2, cargo.getAcesso());
            pstmt.setInt(3, cargo.getIdEta());
            pstmt.setInt(4, cargo.getId());

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
