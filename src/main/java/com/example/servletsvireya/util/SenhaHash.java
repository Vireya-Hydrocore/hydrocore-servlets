package com.example.servletsvireya.util;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SenhaHash {

    // Gera o hash seguro da senha antes de salvar no banco (usa fator de custo 12)
    public static String hashSenha(String senhaPura){
        return BCrypt.hashpw(senhaPura, BCrypt.gensalt(6));
    }

    // Verifica se a senha digitada confere com o hash armazenado no banco
    public static boolean verificarSenha(String senhaDigitada, String hashDoBanco){
        return BCrypt.checkpw(senhaDigitada, hashDoBanco);
    }

    // Valida se a senha é forte: contém pelo menos 1 maiúscula, 1 número, 1 símbolo e tem 8+ caracteres
    public static boolean senhaForte(String senha){
        if (senha == null) return false;

        String regex = "^(?=.[A-Z])(?=.[0-9])(?=.*[@#$%^&+=!]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(senha);
        return matcher.matches();
    }

    // Valida o formato de e-mail (ex: usuario@dominio.com)
    public static boolean emailValido(String email){
        if (email == null) return false;

        String regex = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
