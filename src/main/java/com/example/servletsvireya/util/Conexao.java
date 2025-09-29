package com.example.servletsvireya.util;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.Objects;

public class Conexao {
    Connection conn;
    Dotenv dotenv = Dotenv.configure()
            .directory("C:\\Users\\eriksilva-ieg\\OneDrive - Instituto J&F\\Vireya\\ServletsVireya")
            .load();


    //Método para criar conexão com o banco
    public Connection conectar() {
        try {
            //Informando o drive postgreSQL
            Class.forName("org.postgresql.Driver"); //Não obrigatório

            conn = DriverManager.getConnection(
                    Objects.requireNonNull(dotenv.get("DB_URL_LOCAL")),
                    dotenv.get("DB_USERNAME_LOCAL"),
                    dotenv.get("DB_PASSWORD_LOCAL")
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
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
