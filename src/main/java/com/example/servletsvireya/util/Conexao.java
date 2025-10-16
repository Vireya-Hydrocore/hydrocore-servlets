package com.example.servletsvireya.util;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URL;
import java.sql.*;
import java.util.Objects;

public class Conexao {
    Connection conn;

    String url = "jdbc:postgresql://pg-26d1af5a-germinare-bf04.h.aivencloud.com:28190/fudErikdb";
    String user = "avnadmin";
    String pass = "AVNS_6laeJARLI53amL4NRl1";

    Dotenv dotenv = Dotenv.load(); //carrega o arquivo .env

    //Método para criar conexão com o banco
    public Connection conectar() {
        try {
            //Informando o drive postgreSQL
            Class.forName("org.postgresql.Driver"); //Não obrigatório

            this.conn = DriverManager.getConnection(
                    Objects.requireNonNull(dotenv.get("DB_URL")),
                    dotenv.get("DB_USER"),
                    dotenv.get("DB_PASSWORD")
            );

            System.out.println("conectou!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return conn;
    }

    //Método para desconectar do banco
    public void desconectar() {
        try {
            if (conn != null && !conn.isClosed()) { //Se a conexão estiver preenchida E aberta
                //Fechando a conexão com o banco de dados
                System.out.println("desconectou!");
                this.conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
