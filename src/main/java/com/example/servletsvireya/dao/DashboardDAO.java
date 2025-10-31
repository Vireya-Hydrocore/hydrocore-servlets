package com.example.servletsvireya.dao;

import com.example.servletsvireya.util.Conexao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DashboardDAO {


    Conexao conexao = new Conexao();

    public Map<String, Integer> contagemGeral() {
        Connection conn;
        conn = conexao.conectar();

        // Chave é String, valor é Integer
        Map<String, Integer> quantidadesPorTabela;
        quantidadesPorTabela = new HashMap<>();

        //Prepara o comando SQL com JOIN
        String comandoAdm;
        comandoAdm = "SELECT COUNT(*) AS total FROM ADMIN";

        String comandoCarg;
        comandoCarg = "SELECT COUNT(*) AS total FROM CARGO";

        String comandoEstq;
        comandoEstq = "SELECT COUNT(*) AS total FROM ESTOQUE";

        String comandoEta;
        comandoEta = "SELECT COUNT(*) AS total FROM ETA";

        String comandoFuncion;
        comandoFuncion = "SELECT COUNT(*) AS total FROM FUNCIONARIO";

        String comandoProdut;
        comandoProdut = "SELECT COUNT(*) AS total FROM PRODUTO";

        try{
            Statement stmt;
            stmt = conn.createStatement();

            ResultSet rs1;
            rs1 = stmt.executeQuery(comandoAdm);
            if (rs1.next()) {
                quantidadesPorTabela.put("admin",rs1.getInt("total"));
            }
            rs1.close(); // importante fechar antes de executar outro comando

            // Segundo comando
            ResultSet rs2;
            rs2 = stmt.executeQuery(comandoCarg);
            if (rs2.next()) {
                quantidadesPorTabela.put("cargo",rs2.getInt("total"));
            }
            rs2.close(); // importante fechar antes de executar outro comando

            ResultSet rs3;
            rs3 = stmt.executeQuery(comandoEstq);
            if (rs3.next()) {
                quantidadesPorTabela.put("estoque",rs3.getInt("total"));
            }
            rs3.close(); // importante fechar antes de executar outro comando

            ResultSet rs4;
            rs4 = stmt.executeQuery(comandoEta);
            if (rs4.next()) {
                quantidadesPorTabela.put("eta",rs4.getInt("total"));
            }
            rs4.close(); // importante fechar antes de executar outro comando

            ResultSet rs5;
            rs5 = stmt.executeQuery(comandoFuncion);
            if (rs5.next()) {
                quantidadesPorTabela.put("funcionario",rs5.getInt("total"));
            }
            rs5.close(); // importante fechar antes de executar outro comando

            ResultSet rs6;
            rs6 = stmt.executeQuery(comandoProdut);
            if (rs6.next()) {
                quantidadesPorTabela.put("produto",rs6.getInt("total"));
            }
            rs6.close(); // importante fechar antes de executar outro comando

            return quantidadesPorTabela;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null; // Retorna nulo em caso de exceção
        } finally {
            conexao.desconectar();
        }
    }
}