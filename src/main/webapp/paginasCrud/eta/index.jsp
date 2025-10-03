<%@ page import="java.util.Scanner" %><%--
  Created by IntelliJ IDEA.
  User: gabrielmasagao-ieg
  Date: 29/09/2025
  Time: 22:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Cadastre-se</title>
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/paginasCrud/eta/img/vireya icon.png" type="image/x-icon">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/paginasCrud/eta/style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

</head>

<body>
<main>

  <div class="container-esquerdo">
    <div class="logo">
      <img src="${pageContext.request.contextPath}/paginasCrud/eta/img/vireya icon.png">
      <span>HydroCore</span>
    </div>
    <h1>Criar Conta</h1>
    <form id="form" action="${pageContext.request.contextPath}/servlet-alterar-eta" method="post">
      <label for="nome">Nome</label>
      <input type="text" id="nome" placeholder="Digite o nome da ETA" name="nome">
      <div id="erroNome" class="erro"></div>

      <label for="email">E-mail</label>
      <input type="email" id="email" placeholder="Digite o e-mail" name="email">
      <div id="erroEmail" class="erro"></div>

      <label for="cnpj">CNPJ</label>
      <input type="text" id="cnpj" placeholder="Digite seu CNPJ" name="cnpj"
             pattern="[0-9]{2}\.[0-9]{3}\.[0-9]{3}/[0-9]{4}-[0-9]{2}">
      <div id="erroCNPJ" class="erro"></div>


      <label for="senha">Senha</label>
      <div class="input-senha">
        <input type="password" id="senha" placeholder="Digite sua senha" name="senha" required
               pattern="^(?=.*[A-Z])(?=.*[!@#$%]).{8,}$"
               title="Tem que incluir pelo menos uma letra maiúscula e um caractere especial(!@#$%)">
        <span class="password-toggle" id="togglePassword">
                        <i class="fas fa-eye"></i>
                    </span>
      </div>
      <div id="erroSenha" class="erro"></div>

      <label for="telefone">Telefone</label>
      <input type="text" id="telefone" placeholder="Digite o telefone (99) 99999-9999"
             pattern="\([0-9]{2}\) [0-9]{5}-[0-9]{4}" name="telefone">
      <div id="erroTelefone" class="erro"></div>

      <label for="capacidade">Capacidade</label>
      <input type="text" id="capacidade" placeholder="Digite a capacidade da ETA" name="capacidade">
      <div id="erroCapacidade" class="erro"></div>

      <div class="checkbox">
        <input type="checkbox" id="termos">
        <label for="termos">Termos e Condições de Uso</label>
      </div>
      <button class="botao" type="submit">Cadastre-se</button>
      <p class="login-text">Já possui uma conta? <a href="#">Clique aqui</a></p>
    </form>
  </div>
  <div class="container-direito">
    <div class="sobreposicao">
      <img src="${pageContext.request.contextPath}/paginasCrud/eta/img/abstract geometric.png">
    </div>
    <div class="celular">
      <img src="${pageContext.request.contextPath}/paginasCrud/eta/img/modelo_frontal.png">
    </div>
  </div>
</main>
<script src="${pageContext.request.contextPath}/paginasCrud/eta/regex.js"></script>
<script src="${pageContext.request.contextPath}/paginasCrud/eta/script.js"></script>

</body>
</html>
