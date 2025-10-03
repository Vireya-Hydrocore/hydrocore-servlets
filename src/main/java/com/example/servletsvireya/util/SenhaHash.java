//package com.example.servletsvireya.util;
//import org.mindrot.jbcrypt.BCrypt;
//
//public class SenhaHash {
//    // Gera o hash da senha antes de salvar no banco
//    public static String hashSenha(String senhaPura){
//        // O 12 é o "fator de custo" — quanto maior, mais seguro (mas também mais lento)
//        return BCrypt.hashpw(senhaPura, BCrypt.gensalt(12));
//    }
//
//    // Verifica se a senha digitada confere com o hash salvo no banco
//    public static boolean verificarSenha(String senhaDigitada, String hashDoBanco){
//        return BCrypt.checkpw(senhaDigitada, hashDoBanco);
//    }
//}
