package com.example.servletsvireya.util;
import java.util.List;
import java.util.ArrayList;

public class Validador {

    public static boolean ehPositivo(int valor){
        return valor >= 0; //Zero conta como positivo
    }

    /*
     * VALIDAÇÃO DE SENHA
     */
    public static List<String> validarSenha(String senha) {
        List<String> erros = new ArrayList<>();

        if (senha == null || senha.isEmpty()) {
            erros.add("A senha não pode estar vazia.");
            return erros;
        }

        if (senha.length() < 8) {
            erros.add("A senha deve ter no mínimo 8 caracteres.");
        }

        if (!senha.matches(".\\d.")) {
            erros.add("A senha deve conter pelo menos um número.");
        }

        if (!senha.matches(".[^a-zA-Z0-9].")) {
            erros.add("A senha deve conter pelo menos um caractere especial.");
        }
        return erros;
    }



}