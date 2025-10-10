package com.example.servletsvireya.util;
import java.util.List;
import java.util.ArrayList;

public class Validador {

    public static boolean ehPositivo(int valor) {
        return valor >= 0; // Zero conta como positivo
    }


    public static List<String> validarSenha(String senha) {
        List<String> erros = new ArrayList<>();

        if (senha == null) {
            erros.add("A senha não pode estar vazia.");
            return erros;
        }

        if (senha.length() < 8) {
            erros.add("A senha deve ter no mínimo 8 caracteres.");
        }

        if (!senha.matches(".*\\d.*")) {
            erros.add("A senha deve conter pelo menos um número.");
        }

        if (!senha.matches(".*[A-Z].*")) {
            erros.add("A senha deve conter pelo menos uma letra maiúscula.");
        }

        if (!senha.matches(".*[^a-zA-Z0-9].*")) {
            erros.add("A senha deve conter pelo menos um caractere especial.");
        }

        return erros;
    }
    public static boolean ehVazio(String palavra){
        return !palavra.equals("");
    }
    public static boolean ehNulloString(List<String> lista){
        return !lista.isEmpty();
    }
    public static boolean ehNulloInt(List<Integer> lista){
        return !lista.isEmpty();
    }
    public static boolean ehNulloClass(List<Class> lista){
        return !lista.isEmpty();
    }
    public static boolean ehNulloObject(List<Object> lista){
        return !lista.isEmpty();
    }
    public static boolean validarLength(String palavra,int quantidadeCaracteres){
        return palavra.length() < quantidadeCaracteres;
    }

}
