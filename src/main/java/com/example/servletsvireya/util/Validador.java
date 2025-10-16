package com.example.servletsvireya.util;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class Validador {

    // GERAIS

    public static boolean ehPositivo(int valor) {
        return valor >= 0; // Zero conta como positivo
    }

    public static boolean naoVazio(String texto) {
        return texto != null && !texto.trim().isEmpty();
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
    public static boolean ehNulloObject(List<Object> lista) {
        return !lista.isEmpty();
    }
    public static boolean validarLength(String palavra,int quantidadeCaracteres){
        return palavra.length() < quantidadeCaracteres;
    }

    public static boolean validarEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public static boolean validarTelefone(String telefone) {
        return telefone != null && telefone.matches("\\([0-9]{2}\\) [0-9]{5}-[0-9]{4}");
    }

    public static boolean validarCNPJ(String cnpj) {
        return cnpj != null && cnpj.matches("^[0-9]{2}\\.[0-9]{3}\\.[0-9]{3}/[0-9]{4}-[0-9]{2}$");
    }

    public static boolean validarData(LocalDate data) {
        return data != null && !data.isAfter(LocalDate.now().plusYears(100));
    }
    public static boolean validarCep(String cep) {
        return cep!= null && cep.matches("[0-9]{5}-[0-9]{3}");
    }

    // VALIDAÇÃO DE SENHA

    public static List<String> validarSenha(String senha) {
        List<String> erros = new ArrayList<>();

        if (senha == null || senha.isEmpty()) {
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

    // VALIDAÇÕES ESPECÍFICAS

    public static List<String> validarAdmin(String nome, String email, String senha) {
        List<String> erros = new ArrayList<>();

        if (!naoVazio(nome))
            erros.add("O nome do administrador não pode estar vazio.");

        if (!validarEmail(email))
            erros.add("O e-mail informado é inválido.");

        erros.addAll(validarSenha(senha));

        return erros;
    }

    public static List<String> validarCargo(String nome, int acesso) {
        List<String> erros = new ArrayList<>();

        if (!naoVazio(nome))
            erros.add("O nome do cargo não pode estar vazio.");

        if (!ehPositivo(acesso))
            erros.add("O nível de acesso deve ser um número positivo.");

        return erros;
    }

    public static List<String> validarEta(String nome, int capacidade, String telefone, String cnpj,String cep) {
        List<String> erros = new ArrayList<>();

        if (!naoVazio(nome))
            erros.add("O nome da ETA não pode estar vazio.");

        if (!ehPositivo(capacidade))
            erros.add("A capacidade deve ser positiva.");

        if (!validarTelefone(telefone))
            erros.add("Telefone inválido.");

        if (!validarCNPJ(cnpj))
            erros.add("CNPJ inválido.");

        if (!validarCep(cep)){
            erros.add("Cep inválido");
        }

        return erros;
    }

    public static List<String> validarProduto(String nome, String tipo, double concentracao) {
        List<String> erros = new ArrayList<>();

        if (!naoVazio(nome))
            erros.add("O nome do produto não pode estar vazio.");

        if (!naoVazio(tipo))
            erros.add("O tipo do produto não pode estar vazio.");

        if (concentracao <= 0)
            erros.add("A concentração deve ser maior que zero.");

        return erros;
    }
}